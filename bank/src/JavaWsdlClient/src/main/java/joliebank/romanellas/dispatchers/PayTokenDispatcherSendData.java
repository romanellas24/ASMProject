package joliebank.romanellas.dispatchers;

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
        String token = "82bd81700d9b067e18310405718ea210dcf84563cf46af64eefe6d97bd320cbadc0476ea18160db8b74e1078659feb3e"; // Replace this with actual token value
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
