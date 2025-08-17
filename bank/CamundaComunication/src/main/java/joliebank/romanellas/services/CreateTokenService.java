package joliebank.romanellas.services;

import com.example.soapclient.BANKGATEWAY2;
import com.example.soapclient.BANKGATEWAY2Service;
import io.camunda.zeebe.client.ZeebeClient;
import jakarta.xml.ws.Holder;

import java.util.Map;

public class CreateTokenService {

    private ZeebeClient client;

    public CreateTokenService(ZeebeClient client) {
        this.client = client;
    }

    public void jolieCheckAccountExists() {
        //Worker per il primo service task
        client.newWorker()
                .jobType("jolie-check-account-exists")
                .handler((jobClient, job) -> {
                    try {
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

                        Map<String, Object> vars = job.getVariablesAsMap();
                        int account = (int) vars.get("account");

                        Holder<Integer> exists = new Holder<>();
                        Holder<Integer> status = new Holder<>();

                        port.getAccountExists(account, exists, status);
                        System.out.println("E - Result: " + exists.value);

                        boolean isExists = exists.value != 0;

                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(Map.of("exists", isExists))
                                .send()
                                .join();

                    } catch (Throwable e) {
                        System.out.println("Errore: " + e.getClass().getName() + ": " + e.getMessage());
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP createToken: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();
    }

    public void jolieCreateToken() {
        // Worker per il secondo Service Task
        client.newWorker()
                .jobType("jolie-create-token")  // Deve matchare il Task Type nel BPMN
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("called jolie-create-token");
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

                        Map<String, Object> vars = job.getVariablesAsMap();
                        double amount = (double) vars.get("amount");
                        int account = (int) vars.get("account");

                        String response = port.postPay(amount, account);
                        System.out.println("Created token: " + response);

                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(Map.of("token", response))
                                .send()
                                .join();

                    } catch (Throwable e) {
                        System.out.println("Errore: " + e.getClass().getName() + ": " + e.getMessage());
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP createToken: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();
    }
}
