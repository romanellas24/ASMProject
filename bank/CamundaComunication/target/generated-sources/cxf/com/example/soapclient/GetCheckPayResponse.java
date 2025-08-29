
package com.example.soapclient;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="amount" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="code" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="beneficiary" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "amount",
    "code",
    "beneficiary",
    "status"
})
@XmlRootElement(name = "getCheckPayResponse")
public class GetCheckPayResponse {

    protected double amount;
    protected int code;
    @XmlElement(required = true)
    protected String beneficiary;
    @XmlElement(required = true)
    protected String status;

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
     * Gets the value of the code property.
     * 
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     */
    public void setCode(int value) {
        this.code = value;
    }

    /**
     * Gets the value of the beneficiary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiary() {
        return beneficiary;
    }

    /**
     * Sets the value of the beneficiary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiary(String value) {
        this.beneficiary = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

}
