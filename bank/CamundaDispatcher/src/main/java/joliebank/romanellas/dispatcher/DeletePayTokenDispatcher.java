package joliebank.romanellas.dispatcher;

import io.camunda.zeebe.client.ZeebeClient;

public class DeletePayTokenDispatcher {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder().gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext().build();
        client.newPublishMessageCommand()
                .messageName("DeletePayRequest")
                .correlationKey("start")
                .variables("{\"token\": \"ANAS\"}")
                .send()
                .join();
        System.out.println("TOKEN INESISTENTE");


        client.newPublishMessageCommand()
                .messageName("DeletePayRequest")
                .correlationKey("start")
                .variables("{\"token\": \"162feaacbda7e9977623c9be0d4c9e28390e9c3c3081fad04efef38e642026d0c079b4cf82bfc5809c229ad46e9f37f7\"}")
                .send()
                .join();
        System.out.println("TOKEN PAGATO");

        client.newPublishMessageCommand()
                .messageName("DeletePayRequest")
                .correlationKey("start")
                .variables("{\"token\": \"ad5afb86b4e0d6668c98aa379755dd16de2a924e72ff69c5cbcd3dd3b70c11bdfc6c6bcb1e2e7408636e62f58c4225a4\"}")
                .send()
                .join();
        System.out.println("TOKEN NON PAGATO");
    }


}
