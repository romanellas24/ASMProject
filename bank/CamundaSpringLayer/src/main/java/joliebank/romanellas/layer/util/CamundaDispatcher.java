package joliebank.romanellas.layer.util;

import io.camunda.zeebe.client.ZeebeClient;
import joliebank.romanellas.layer.dto.PaymentRequest;

import java.util.Map;

public class CamundaDispatcher {
    private final ZeebeClient client;

    public CamundaDispatcher() {
        this.client = ZeebeClient.newClientBuilder().gatewayAddress("116.203.198.188:26500").usePlaintext().build();
    }

    public void startPaymentProcess(String token) {
        client.newPublishMessageCommand()
                .messageName("PayTokenRequest")
                .correlationKey("start")
                .variables(Map.of("token", String.valueOf(token)))
                .send()
                .join();
    }

    public void insertPaymentData(PaymentRequest req) {
        String month = req.expire_month().toString();
        String year = req.expire_year().toString();
        if(year.length() > 2)
            year = year.substring(Math.max(year.length() - 2, 0));

        if(month.length() == 1)
            month = "0" + month;

        Map<String, Object> variables = Map.of(
                "token", req.token(),
                "pan", req.pan(),
                "cvv", req.cvv().toString(),
                "card_holder_first_name", req.card_holder_first_name(),
                "card_holder_last_name", req.card_holder_last_name(),
                "expire_month", month,
                "expire_year", year
        );

        // Send the message to the boundary event
        client.newPublishMessageCommand()
                .messageName("JolieInsertPaymentData") // must match BPMN message name
                .correlationKey(req.token())                 // must match process variable in instance
                .variables(variables)
                .send()
                .join();
    }
}
