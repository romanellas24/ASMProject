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
        String token = "7d6f9932c0be8af9377c7def68bebdb522c24730c7a6ba73ce1aea0bf5324851e23c02743986f87421c227ac28bf904e"; // Replace this with actual token value
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
