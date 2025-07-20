package cloud.romanellas.joliebank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"code", "status"}
)
@XmlRootElement(
        name = "checkPaymentDataResponse"
)
public class CheckPaymentDataResponse {
    protected int code;
    @XmlElement(
            required = true
    )
    protected String status;

    public CheckPaymentDataResponse() {
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int var1) {
        this.code = var1;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String var1) {
        this.status = var1;
    }
}
