package cloud.romanellas.joliebank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"cvv", "expireMonth", "cardHolderFirstName", "expireYear", "pan", "cardHolderLastName", "token"}
)
@XmlRootElement(
        name = "checkPaymentData"
)
public class CheckPaymentData {
    protected int cvv;
    @XmlElement(
            name = "expire_month"
    )
    protected int expireMonth;
    @XmlElement(
            name = "card_holder_first_name",
            required = true
    )
    protected String cardHolderFirstName;
    @XmlElement(
            name = "expire_year"
    )
    protected int expireYear;
    @XmlElement(
            required = true
    )
    protected String pan;
    @XmlElement(
            name = "card_holder_last_name",
            required = true
    )
    protected String cardHolderLastName;
    @XmlElement(
            required = true
    )
    protected String token;

    public CheckPaymentData() {
    }

    public int getCvv() {
        return this.cvv;
    }

    public void setCvv(int var1) {
        this.cvv = var1;
    }

    public int getExpireMonth() {
        return this.expireMonth;
    }

    public void setExpireMonth(int var1) {
        this.expireMonth = var1;
    }

    public String getCardHolderFirstName() {
        return this.cardHolderFirstName;
    }

    public void setCardHolderFirstName(String var1) {
        this.cardHolderFirstName = var1;
    }

    public int getExpireYear() {
        return this.expireYear;
    }

    public void setExpireYear(int var1) {
        this.expireYear = var1;
    }

    public String getPan() {
        return this.pan;
    }

    public void setPan(String var1) {
        this.pan = var1;
    }

    public String getCardHolderLastName() {
        return this.cardHolderLastName;
    }

    public void setCardHolderLastName(String var1) {
        this.cardHolderLastName = var1;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String var1) {
        this.token = var1;
    }
}
