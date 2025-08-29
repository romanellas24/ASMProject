package joliebank.romanellas.dispatcher;

import io.camunda.zeebe.client.ZeebeClient;

public class CheckTokenDispatcher {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder().gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext().build();
        client.newPublishMessageCommand()
                .messageName("CheckTokenRequest")
                .correlationKey("start")
                .variables("{\"token\": \"ANAS\"}")
                .send()
                .join();
        System.out.println("TOKEN INESISTENTE");


        client.newPublishMessageCommand()
                .messageName("CheckTokenRequest")
                .correlationKey("start")
                .variables("{\"token\": \"d73008524911e236c3e842c70d18a3174a959e4251686c92d3dc5ee1c28b8fd63077c0cf5b49f1f0a3fb464627dc0428\"}")
                .send()
                .join();
        System.out.println("TOKEN PAGATO");

        client.newPublishMessageCommand()
                .messageName("CheckTokenRequest")
                .correlationKey("start")
                .variables("{\"token\": \"3cb5fdf51e06328474880abeb4df83105ed9b3d95099ff880a086d66016b12b16b738860657675f285b15bab5d12ead4\"}")
                .send()
                .join();
        System.out.println("TOKEN NON PAGATO");
    }


}
