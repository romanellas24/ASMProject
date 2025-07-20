package joliebank.romanellas.dispatchers;

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
                                "\"token\": \"fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1\"" +
                                "}"
                )
                .send()
                .join();

        System.out.println("Inviato il messaggio per far partire il processo - VALID...");
    }


}
