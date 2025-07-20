package joliebank.romanellas.tests;

import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2;
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2Service;

import javax.xml.ws.Holder;

public class Test {
    public static void main(String[] args) {
        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

        Holder<Integer> exists = new Holder<>();
        Holder<Integer> status = new Holder<>();

        port.getAccountExists(4, exists, status);
    }
}
