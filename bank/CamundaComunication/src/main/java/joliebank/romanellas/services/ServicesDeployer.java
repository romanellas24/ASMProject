package joliebank.romanellas.services;

import io.camunda.zeebe.client.ZeebeClient;

public class ServicesDeployer {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext()
                .build();


        CreateTokenService createTokenService = new CreateTokenService(client);
        PayTokenService payTokenService = new PayTokenService(client);
        ReimbursementService reimbursementService = new ReimbursementService(client);

        createTokenService.jolieCheckAccountExists();
        createTokenService.jolieCreateToken();
        createTokenService.handleResponses();
        payTokenService.jolieCheckToken();
        payTokenService.jolieCheckPayData();
        payTokenService.joliePayData();
        payTokenService.handleResponses();

        reimbursementService.reimburseToken();
        reimbursementService.makeNotRefundable();
        reimbursementService.handleResponses();

        System.out.println("SOAP Workers avviati...");

        // Mantieni il worker in esecuzione
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        client.close();
    }
}
