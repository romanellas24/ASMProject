package joliebank.romanellas.services;

import com.example.soapclient.BANKGATEWAY2;
import com.example.soapclient.BANKGATEWAY2Service;
import com.google.common.collect.Maps;
import io.camunda.zeebe.client.ZeebeClient;
import jakarta.xml.ws.Holder;
import joliebank.romanellas.utils.HttpSendCallback;

import java.util.HashMap;
import java.util.Map;

public class PayTokenService {

    private ZeebeClient client;
    private HttpSendCallback httpClient;

    public PayTokenService(ZeebeClient client) {
        this.client = client;
        this.httpClient = new HttpSendCallback();
    }

    public void jolieCheckToken() {
        client.newWorker()
                .jobType("jolie-check-token")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("called: jolie-check-token");
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

                        Map<String, Object> vars = job.getVariablesAsMap();

                        String token = (String) vars.get("token");

                        Holder<Double> amountHolder = new Holder<>();
                        Holder<Integer> code = new Holder<>();
                        Holder<String> beneficiaryHolder = new Holder<>();
                        Holder<String> statusHolder = new Holder<>();

                        port.getCheckPay(token, amountHolder, code, beneficiaryHolder, statusHolder);

                        Map<String, Object> outVars = Maps.newHashMap();
                        outVars.put("beneficiaryPayment", beneficiaryHolder.value);
                        outVars.put("statusCheckToken", statusHolder.value);
                        outVars.put("amountToken", amountHolder.value);

                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP jolieCheckToken: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();
    }

    public void jolieCheckPayData() {
        client.newWorker()
                .jobType("jolie-check-pay-data")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("called: jolie-check-pay-data");
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

                        Map<String, Object> vars = job.getVariablesAsMap();

                        String token = (String) vars.get("token");
                        int cvv = Integer.parseInt((String) vars.get("cvv"));
                        int expireMonth = Integer.parseInt((String) vars.get("expire_month"));
                        int expireYear = Integer.parseInt((String) vars.get("expire_year"));
                        String pan = (String) vars.get("pan");
                        String cardHolderFirstName = (String) vars.get("card_holder_first_name");
                        String cardHolderLastName = (String) vars.get("card_holder_last_name");


                        Holder<Integer> codeHolder = new Holder<>();
                        Holder<String> statusHolder = new Holder<>();

                        port.checkPaymentData(cvv, expireMonth, cardHolderFirstName, expireYear, pan, cardHolderLastName, token, codeHolder, statusHolder);

                        Map<String, Object> outVars = Maps.newHashMap();
                        outVars.put("verifyPaymentDataHttpStatusCode", codeHolder.value);

                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();
    }

    public void joliePayData() {
        client.newWorker()
                .jobType("jolie-pay-token")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("called: jolie-pay-token");
                        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
                        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

                        Map<String, Object> vars = job.getVariablesAsMap();

                        String token = (String) vars.get("token");
                        int cvv = Integer.parseInt((String) vars.get("cvv"));
                        int expireMonth = Integer.parseInt((String) vars.get("expire_month"));
                        int expireYear = Integer.parseInt((String) vars.get("expire_year"));
                        String pan = (String) vars.get("pan");
                        String cardHolderFirstName = (String) vars.get("card_holder_first_name");
                        String cardHolderLastName = (String) vars.get("card_holder_last_name");


                        Holder<Integer> codeHolder = new Holder<>();
                        Holder<String> statusHolder = new Holder<>();

                        port.putPay(cvv, expireMonth, cardHolderFirstName, expireYear, pan, cardHolderLastName, token, codeHolder, statusHolder);

                        Map<String, Object> outVars = Maps.newHashMap();
                        outVars.put("verifyPaymentDataHttpStatusCode", codeHolder.value);

                        if (codeHolder.value != 200) {
                            jobClient.newThrowErrorCommand(job.getKey())
                                    .errorCode("PostCheckPaymentError")
                                    .errorMessage("Error paying token: " + token)
                                    .send()
                                    .join();
                            return;
                        }

                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore SOAP jolieCheckToken: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();
    }

    public void handleResponses() {
        client.newWorker()
                .jobType("jolie-pay-invalid-token")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("Called:jolie-pay-invalid-token");
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "INVALID_TOKEN");

                        httpClient.sendCallback(token, (String) outputVars.get("result"));

                        client.newPublishMessageCommand()
                                .messageName("PayTokenResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-pay-invalid-token: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

        client.newWorker()
                .jobType("jolie-pay-invalid-payment-data")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("Called:jolie-pay-invalid-payment-data");
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "INVALID_PAYMENT_DATA");

                        httpClient.sendCallback(token, (String) outputVars.get("result"));

                        client.newPublishMessageCommand()
                                .messageName("PayTokenResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-pay-invalid-payment-data: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();


        client.newWorker()
                .jobType("jolie-pay-exception")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("Called:jolie-pay-exception");
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "PAYMENT_EXCEPTION");

                        httpClient.sendCallback(token, (String) outputVars.get("result"));

                        client.newPublishMessageCommand()
                                .messageName("PayTokenResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-pay-exception: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

        client.newWorker()
                .jobType("jolie-pay-success")
                .handler((jobClient, job) -> {
                    try {
                        System.out.println("Called: jolie-pay-success");
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "PAYMENT_SUCCESS");

                        httpClient.sendCallback(token, (String) outputVars.get("result"));

                        client.newPublishMessageCommand()
                                .messageName("PayTokenResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-pay-success: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

        client.newWorker()
                .jobType("jolie-check-token-success")
                .handler((jobClient, job) -> {
                    System.out.println("Called:jolie-check-token-success");
                    try {
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "PAYMENT_EXECUTED");

                        client.newPublishMessageCommand()
                                .messageName("CheckTokenResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-check-token-success: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();


        client.newWorker()
                .jobType("jolie-check-token-fail")
                .handler((jobClient, job) -> {
                    System.out.println("Called:jolie-check-token-fail");
                    try {
                        Map<String, Object> processVars = job.getVariablesAsMap();
                        String token = (String) processVars.get("token");

                        Map<String, Object> outputVars = new HashMap<>();
                        outputVars.put("token", token);
                        outputVars.put("result", "PAYMENT_NOT_EXECUTED_YET");

                        client.newPublishMessageCommand()
                                .messageName("CheckTokenResponseTest")
                                .correlationKey("start")
                                .variables(outputVars)
                                .send()
                                .join();


                        // Completa job
                        jobClient.newCompleteCommand(job.getKey())
                                .variables(outputVars)
                                .send()
                                .join();

                    } catch (Exception e) {
                        e.printStackTrace();
                        jobClient.newFailCommand(job.getKey())
                                .retries(0)
                                .errorMessage("Errore jolie-check-token-fail: " + e.getMessage())
                                .send()
                                .join();
                    }
                })
                .open();

    }

}
