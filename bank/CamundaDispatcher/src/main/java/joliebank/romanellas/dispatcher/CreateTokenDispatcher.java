package joliebank.romanellas.dispatcher;

import io.camunda.zeebe.client.ZeebeClient;

public class CreateTokenDispatcher {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder().gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext().build();

        // 1. AVVIO DEL PROCESSO VIA MESSAGE START EVENT
        client.newPublishMessageCommand()
                .messageName("CreateTokenRequest")
                .correlationKey("start") // oppure un vero valore se il BPMN lo richiede
                .variables("{\"amount\": 5.12, \"account\": 3}")
                .send()
                .join();

        System.out.println("Inviato il messaggio per far partire il processo - VALID...");


        // 2. AVVIO DEL PROCESSO VIA MESSAGE START EVENT
        client.newPublishMessageCommand()
                .messageName("CreateTokenRequest")
                .correlationKey("start") // oppure un vero valore se il BPMN lo richiede
                .variables("{\"amount\": 5.12, \"account\": 4555555}")
                .send()
                .join();

        System.out.println("Inviato il messaggio per far partire il processo - INVALID...");
    }


}
