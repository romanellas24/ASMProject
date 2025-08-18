package joliebank.romanellas.services;

import com.example.soapclient.BANKGATEWAY2;
import com.example.soapclient.BANKGATEWAY2Service;
import com.google.common.collect.Maps;
import io.camunda.zeebe.client.ZeebeClient;
import jakarta.xml.ws.Holder;

import java.util.HashMap;
import java.util.Map;

public class ReimbursementService {

    private ZeebeClient client;

    public ReimbursementService(ZeebeClient client) {
        this.client = client;
    }

    public void reimburseToken() {
        this.client.newWorker()
                .jobType("jolie-delete-token")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("called: jolie-delete-token");
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();
                        Map<String, Object> vars = job.getVariablesAsMap();
                        String token = (String) vars.get("token");

                        Holder<Integer> codeHolder = new Holder<>();
                        Holder<String> statusHolder = new Holder<>();

                        port.deletePay(token, codeHolder, statusHolder);

                        Map<String, Object> outVars = Maps.newHashMap();
                        outVars.put("deletePayCode", codeHolder.value);
                        outVars.put("deletePayStatus", statusHolder.value);

                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP deletePay: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();
    }

    public void makeNotRefundable() {
        this.client.newWorker()
                .jobType("jolie-make-token-not-refundable")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("called: jolie-make-token-not-refundable");
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();
                        Map<String, Object> vars = job.getVariablesAsMap();
                        String token = (String) vars.get("token");

                        Holder<Integer> statusHolder = new Holder<>();
                        Holder<String> msgHolder = new Holder<>();

                        port.putNotRefaundable(token, msgHolder, statusHolder);

                        Map<String, Object> outVars = Maps.newHashMap();
                        outVars.put("makeNotRefundableMsg", msgHolder.value);
                        outVars.put("makeNotRefundableStatus", statusHolder.value);

                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP deletePay: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();
    }

    /**
     * jolie-delete-pay-success
     * jolie-delete-pay-error
     * jolie-not-refundable-success
     * jolie-not-refundable-error
     */

    public void handleResponses(){
        client.newWorker()
                .jobType("jolie-delete-pay-success")  // Deve matchare il Task Type nel BPMN
                .handler((jobClient, job) -> {
                    try {
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "PAYMENT_DELETED");

                        client.newPublishMessageCommand()
                                .messageName("DeletePayResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Throwable e) {
                        System.out.println("Errore: " + e.getClass().getName() + ": " + e.getMessage());
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-delete-pay-success: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

        client.newWorker()
                .jobType("jolie-delete-pay-error")  // Deve matchare il Task Type nel BPMN
                .handler((jobClient, job) -> {
                    try {
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "ERROR_DELETING_PAYMENT");

                        client.newPublishMessageCommand()
                                .messageName("DeletePayResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Throwable e) {
                        System.out.println("Errore: " + e.getClass().getName() + ": " + e.getMessage());
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-delete-pay-error: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();


        client.newWorker()
                .jobType("jolie-not-refundable-success")  // Deve matchare il Task Type nel BPMN
                .handler((jobClient, job) -> {
                    try {
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "ERROR_DELETING_PAYMENT");

                        client.newPublishMessageCommand()
                                .messageName("DeletePayResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Throwable e) {
                        System.out.println("Errore: " + e.getClass().getName() + ": " + e.getMessage());
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-not-refundable-success: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

        client.newWorker()
                .jobType("jolie-not-refundable-error")  // Deve matchare il Task Type nel BPMN
                .handler((jobClient, job) -> {
                    try {
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "ERROR_DELETING_PAYMENT");

                        client.newPublishMessageCommand()
                                .messageName("DeletePayResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Throwable e) {
                        System.out.println("Errore: " + e.getClass().getName() + ": " + e.getMessage());
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-not-refundable-error: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();
    }

}
