package joliebank.romanellas;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import org.w3._2001.xmlschema.BANKGATEWAY;
import org.w3._2001.xmlschema.BANKGATEWAYService;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;

public class SoapWorker {
    public static void main(String[] args) {

        ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress("116.203.198.188:26500") // Zeebe broker address
                .usePlaintext()
                .build();

        client.newWorker()
                .jobType("soap-call")  // Match the jobType in your BPMN
                .handler((JobClient jobClient, ActivatedJob job) -> {
                    try {
                        // Qui chiami il tuo SOAP client generato da wsimport
                        BANKGATEWAYService service = new BANKGATEWAYService();
                        BANKGATEWAY port = service.getBANKGATEWAYServicePort();

                        /*
                        PostPay request = new PostPay();
                        request.setAmount(3.41);
                        request.setDestAccount(1);
                         */

                        // PostPayResponse response = port.postPay(3.41, 1)
                        String response = port.postPay(3.41, 1);

                        jobClient.newCompleteCommand(job.getKey())
                                .variables("{\"result\": \"" + response + "\"}")
                                .send()
                                .join();

                    } catch (Exception e) {
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage(e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

        System.out.println("Worker started for type 'soap-call'");
    }
}
