package joliebank.romanellas.services;

import com.google.common.collect.Maps;
import io.camunda.zeebe.client.ZeebeClient;
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2;
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2Service;

import javax.xml.ws.Holder;
import java.util.Map;

public class PayTokenService {

    private ZeebeClient client;

    public PayTokenService(ZeebeClient client) {
        this.client = client;
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
                                .errorMessage("Errore SOAP jolieCheckToken: " + e.getMessage())
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

                        if(codeHolder.value != 200){
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
}
