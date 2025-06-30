package joliebank.romanellas.services;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.Map;

// Importa le classi generate da wsimport
import io.camunda.zeebe.model.bpmn.BpmnModelException;
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2;
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2Service;

import javax.xml.ws.Holder;

public class CreateTokenService {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext()
                .build();

        //Worker per il primo service task
        client.newWorker()
                .jobType("jolie-check-account-exists")  // Deve matchare il Task Type nel BPMN
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("Triggerato ST 1");
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

                        Map<String, Object> vars = job.getVariablesAsMap();
                        int account = (int) vars.get("account");

                        Holder<Integer> exists = new Holder<>();
                        Holder<Integer> status = new Holder<>();

                        port.getAccountExists(account, exists, status);

                        if(exists.value == 0) {
                            jobClient.newThrowErrorCommand(job.getKey())
                                    .errorCode("CreateTokenDestinationAccountNotFound")
                                    .errorMessage("Destination Account not found: " + account)
                                    .send()
                                    .join();
                            return;
                        }


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(Map.of("exists", exists.value))
                                .send()
                                .join();

                    } catch (Exception e) {
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP createToken: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

        // Worker per il secondo Service Task
        client.newWorker()
                .jobType("jolie-create-token")  // Deve matchare il Task Type nel BPMN
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("Triggerato ST 2");
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

                        Map<String, Object> vars = job.getVariablesAsMap();
                        double amount = (double) vars.get("amount");
                        int account = (int) vars.get("account");

                        String response = port.postPay(amount, account);

                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(Map.of("token", response))
                                .send()
                                .join();

                    } catch (Exception e) {
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP createToken: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

        System.out.println("SOAP Workers avviati...");

        // Mantieni il worker in esecuzione
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        client.close();
    }
}
