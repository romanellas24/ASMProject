package joliebank.romanellas.dispatcher;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.Map;

public class PayTokenDispatcherSendData {

    public static void main(String[] args) {
        // Create Zeebe client
        ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress("116.203.198.188:26500") // Replace with your actual address
                .usePlaintext() // Or use secure config for Camunda SaaS
                .build();

        // The token value must match the process variable used in the running instance
        String token = "af5b0ab7c015ba7e36e961f8f0141f96768256a3ee94505199f887a22b44fee4c87bd9de6553f4fc3e67835b68ef04d2"; // Replace this with actual token value
        Map<String, Object> variables = Map.of(
                "token", token,
                "pan", "5353530123456789",
                "cvv", "123",
                "card_holder_first_name", "Daniele",
                "card_holder_last_name", "Romanella",
                "expire_month", "12",
                "expire_year", "29"
        );

        // Send the message to the boundary event
        client.newPublishMessageCommand()
                .messageName("JolieInsertPaymentData") // must match BPMN message name
                .correlationKey(token)                 // must match process variable in instance
                .variables(variables)
                .send()
                .join();

        System.out.println("Message 'JolieInsertPaymentData' sent with token = " + token);
        client.close();
    }


}
