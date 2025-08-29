
package com.example.soapclient;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransactionInfo complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="TransactionInfo">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="amount" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="dest_account" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="payment_request_time" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="src_owner" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="src_account" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="deletable" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="transaction_on" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="dest_owner" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="token" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionInfo", propOrder = {
    "amount",
    "destAccount",
    "paymentRequestTime",
    "srcOwner",
    "srcAccount",
    "deletable",
    "transactionOn",
    "destOwner",
    "token"
})
public class TransactionInfo {

    protected double amount;
    @XmlElement(name = "dest_account")
    protected int destAccount;
    @XmlElement(name = "payment_request_time", required = true)
    protected String paymentRequestTime;
    @XmlElement(name = "src_owner", required = true)
    protected String srcOwner;
    @XmlElement(name = "src_account")
    protected int srcAccount;
    protected int deletable;
    @XmlElement(name = "transaction_on", required = true)
    protected String transactionOn;
    @XmlElement(name = "dest_owner", required = true)
    protected String destOwner;
    @XmlElement(required = true)
    protected String token;

    /**
     * Gets the value of the amount property.
     * 
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     */
    public void setAmount(double value) {
        this.amount = value;
    }

    /**
     * Gets the value of the destAccount property.
     * 
     */
    public int getDestAccount() {
        return destAccount;
    }

    /**
     * Sets the value of the destAccount property.
     * 
     */
    public void setDestAccount(int value) {
        this.destAccount = value;
    }

    /**
     * Gets the value of the paymentRequestTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentRequestTime() {
        return paymentRequestTime;
    }

    /**
     * Sets the value of the paymentRequestTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentRequestTime(String value) {
        this.paymentRequestTime = value;
    }

    /**
     * Gets the value of the srcOwner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcOwner() {
        return srcOwner;
    }

    /**
     * Sets the value of the srcOwner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcOwner(String value) {
        this.srcOwner = value;
    }

    /**
     * Gets the value of the srcAccount property.
     * 
     */
    public int getSrcAccount() {
        return srcAccount;
    }

    /**
     * Sets the value of the srcAccount property.
     * 
     */
    public void setSrcAccount(int value) {
        this.srcAccount = value;
    }

    /**
     * Gets the value of the deletable property.
     * 
     */
    public int getDeletable() {
        return deletable;
    }

    /**
     * Sets the value of the deletable property.
     * 
     */
    public void setDeletable(int value) {
        this.deletable = value;
    }

    /**
     * Gets the value of the transactionOn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionOn() {
        return transactionOn;
    }

    /**
     * Sets the value of the transactionOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionOn(String value) {
        this.transactionOn = value;
    }

    /**
     * Gets the value of the destOwner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestOwner() {
        return destOwner;
    }

    /**
     * Sets the value of the destOwner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestOwner(String value) {
        this.destOwner = value;
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
