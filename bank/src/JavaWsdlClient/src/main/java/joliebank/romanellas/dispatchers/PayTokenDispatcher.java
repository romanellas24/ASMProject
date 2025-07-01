package joliebank.romanellas.dispatchers;

import io.camunda.zeebe.client.ZeebeClient;

public class PayTokenDispatcher {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder().gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext().build();

        // 1. AVVIO DEL PROCESSO VIA MESSAGE START EVENT
        client.newPublishMessageCommand()
                .messageName("PayTokenRequest")
                .correlationKey("start") // oppure un vero valore se il BPMN lo richiede
                .variables(
                        "{" +
                                "\"token\": \"82bd81700d9b067e18310405718ea210dcf84563cf46af64eefe6d97bd320cbadc0476ea18160db8b74e1078659feb3e\"," +
                                "\"pan\": \"5353530123456789\"," +
                                "\"cvv\": \"123\"," +
                                "\"card_holder_first_name\": \"Daniele\"," +
                                "\"card_holder_last_name\": \"Romanella\"," +
                                "\"expire_month\": \"12\"," +
                                "\"expire_year\": \"29\"" +
                                "}"
                )
                .send()
                .join();

        System.out.println("Inviato il messaggio per far partire il processo - VALID...");
    }


}
