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
                .variables("{\"token\": \"fbd3e02b380f364bc909b9918219b89ec635f682e2f90a15255538134ed18b10bb934cea7cd8930473733454b9f711b1\"}")
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
