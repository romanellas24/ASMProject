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
                                "\"token\": \"7d6f9932c0be8af9377c7def68bebdb522c24730c7a6ba73ce1aea0bf5324851e23c02743986f87421c227ac28bf904e\"" +
                                "}"
                )
                .send()
                .join();

        System.out.println("Inviato il messaggio per far partire il processo - VALID...");
    }


}
