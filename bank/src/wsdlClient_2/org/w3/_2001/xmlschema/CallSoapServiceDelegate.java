package org.w3._2001.xmlschema;

import javax.xml.namespace.QName;
import java.net.URL;
import java.security.Provider;

public class CallSoapServiceDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        BANKGATEWAYService service = new BANKGATEWAYService();
        BANKGATEWAY port = service.getBANKGATEWAYServicePort();

        // Chiama il servizio
        String response = port.postPay(15.23, 1);

        System.out.println(response);
    }
}
