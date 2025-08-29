package joliebank.romanellas.dispatcher;

import io.camunda.zeebe.client.ZeebeClient;

public class PayTokenDispatcherInit {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder().gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext().build();

        // 1. AVVIO DEL PROCESSO VIA MESSAGE START EVENT
        client.newPublishMessageCommand()
                .messageName("PayTokenRequest")
                .correlationKey("start") // oppure un vero valore se il BPMN lo richiede
                .variables(
                        "{" +
                                "\"token\": \"af5b0ab7c015ba7e36e961f8f0141f96768256a3ee94505199f887a22b44fee4c87bd9de6553f4fc3e67835b68ef04d2\"" +
                                "}"
                )
                .send()
                .join();

        System.out.println("Inviato il messaggio per far partire il processo - VALID...");
    }


}
