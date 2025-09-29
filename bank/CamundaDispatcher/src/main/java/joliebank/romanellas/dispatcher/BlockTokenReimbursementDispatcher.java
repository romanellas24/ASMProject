package joliebank.romanellas.dispatcher;

import io.camunda.zeebe.client.ZeebeClient;

public class BlockTokenReimbursementDispatcher {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder().gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext().build();
        client.newPublishMessageCommand()
                .messageName("MakeTokenNotRefundableRequest")
                .correlationKey("start")
                .variables("{\"token\": \"ANAS\"}")
                .send()
                .join();
        System.out.println("TOKEN INESISTENTE");


        client.newPublishMessageCommand()
                .messageName("MakeTokenNotRefundableRequest")
                .correlationKey("start")
                .variables("{\"token\": \"534bbdef2b7e05cb2583a7d80bb54631c16d539cc88fce579804ecfe6ecba4dd9abba26ab347e1df2899db2b518f7bc2\"}")
                .send()
                .join();
        System.out.println("TOKEN PAGATO");

        client.newPublishMessageCommand()
                .messageName("MakeTokenNotRefundableRequest")
                .correlationKey("start")
                .variables("{\"token\": \"ad5afb86b4e0d6668c98aa379755dd16de2a924e72ff69c5cbcd3dd3b70c11bdfc6c6bcb1e2e7408636e62f58c4225a4\"}")
                .send()
                .join();
        System.out.println("TOKEN NON PAGATO");
    }


}
