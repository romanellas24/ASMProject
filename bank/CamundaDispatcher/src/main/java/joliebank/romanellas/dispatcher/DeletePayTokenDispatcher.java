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
                .variables("{\"token\": \"af5b0ab7c015ba7e36e961f8f0141f96768256a3ee94505199f887a22b44fee4c87bd9de6553f4fc3e67835b68ef04d2\"}")
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
