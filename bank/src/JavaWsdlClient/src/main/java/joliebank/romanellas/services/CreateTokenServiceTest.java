package joliebank.romanellas.services;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.Map;

// Importa le classi generate da wsimport
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2;
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2Service;

import javax.xml.ws.Holder;

public class CreateTokenServiceTest {

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


        // 1. AVVIO DEL PROCESSO VIA MESSAGE START EVENT
        client.newPublishMessageCommand()
                .messageName("CreateTokenRequest")
                .correlationKey("start") // oppure un vero valore se il BPMN lo richiede
                .variables("{\"amount\": 5.12, \"account\": 1}")
                .send()
                .join();

        System.out.println("Inviato il messaggio per far partire il processo...");

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
