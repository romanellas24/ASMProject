
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
 *         <element name="cvv" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="expire_month" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="expire_year" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="pan" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="acc_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "cvv",
    "expireMonth",
    "expireYear",
    "pan",
    "accId"
})
@XmlRootElement(name = "postCreateCard")
public class PostCreateCard {

    protected int cvv;
    @XmlElement(name = "expire_month")
    protected int expireMonth;
    @XmlElement(name = "expire_year")
    protected int expireYear;
    @XmlElement(required = true)
    protected String pan;
    @XmlElement(name = "acc_id")
    protected int accId;

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
     * Gets the value of the accId property.
     * 
     */
    public int getAccId() {
        return accId;
    }

    /**
     * Sets the value of the accId property.
     * 
     */
    public void setAccId(int value) {
        this.accId = value;
    }

}
