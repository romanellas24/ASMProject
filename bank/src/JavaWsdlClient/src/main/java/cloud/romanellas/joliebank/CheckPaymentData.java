
package cloud.romanellas.joliebank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cvv" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="expire_month" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="card_holder_first_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="expire_year" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="pan" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="card_holder_last_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cvv",
    "expireMonth",
    "cardHolderFirstName",
    "expireYear",
    "pan",
    "cardHolderLastName",
    "token"
})
@XmlRootElement(name = "checkPaymentData")
public class CheckPaymentData {

    protected int cvv;
    @XmlElement(name = "expire_month")
    protected int expireMonth;
    @XmlElement(name = "card_holder_first_name", required = true)
    protected String cardHolderFirstName;
    @XmlElement(name = "expire_year")
    protected int expireYear;
    @XmlElement(required = true)
    protected String pan;
    @XmlElement(name = "card_holder_last_name", required = true)
    protected String cardHolderLastName;
    @XmlElement(required = true)
    protected String token;

    /**
     * Gets the value of the cvv property.
     * 
     */
    public int getCvv() {
        return cvv;
    }

    /**
     * Sets the value of the cvv property.
     * 
     */
    public void setCvv(int value) {
        this.cvv = value;
    }

    /**
     * Gets the value of the expireMonth property.
     * 
     */
    public int getExpireMonth() {
        return expireMonth;
    }

    /**
     * Sets the value of the expireMonth property.
     * 
     */
    public void setExpireMonth(int value) {
        this.expireMonth = value;
    }

    /**
     * Gets the value of the cardHolderFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardHolderFirstName() {
        return cardHolderFirstName;
    }

    /**
     * Sets the value of the cardHolderFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardHolderFirstName(String value) {
        this.cardHolderFirstName = value;
    }

    /**
     * Gets the value of the expireYear property.
     * 
     */
    public int getExpireYear() {
        return expireYear;
    }

    /**
     * Sets the value of the expireYear property.
     * 
     */
    public void setExpireYear(int value) {
        this.expireYear = value;
    }

    /**
     * Gets the value of the pan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPan() {
        return pan;
    }

    /**
     * Sets the value of the pan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPan(String value) {
        this.pan = value;
    }

    /**
     * Gets the value of the cardHolderLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardHolderLastName() {
        return cardHolderLastName;
    }

    /**
     * Sets the value of the cardHolderLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardHolderLastName(String value) {
        this.cardHolderLastName = value;
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

}
