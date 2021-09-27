package rse;//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.05.31 at 07:45:33 PM IDT 
//


import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}rse-holdings"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rseHoldings"
})
@XmlRootElement(name = "rse-user")
public class RseUser {

    @XmlElement(name = "rse-holdings", required = true)
    protected RseHoldings rseHoldings;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlTransient
    protected SimpleBooleanProperty isChanged;

    /**
     * Gets the value of the rseHoldings property.
     * 
     * @return
     *     possible object is
     *     {@link RseHoldings }
     *     
     */
    public RseHoldings getRseHoldings() {
        return rseHoldings;
    }

    /**
     * Sets the value of the rseHoldings property.
     * 
     * @param value
     *     allowed object is
     *     {@link RseHoldings }
     *     
     */
    public void setRseHoldings(RseHoldings value) {
        this.rseHoldings = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isIsChanged() {
        return isChanged.get();
    }

    public SimpleBooleanProperty getIsChangedProperty() {
        return isChanged;
    }

    public void setIsChanged(SimpleBooleanProperty isChanged) {
        this.isChanged = isChanged;
    }

    public boolean checkSale(Integer amount, String sym) {
        RseItem item = rseHoldings.findItem(sym);
        return (item.getQuantity() - item.getAwaitingSale()) >= amount;
    }

    public void offerAccepted(String symbol,int amount,int dtype) {
        if (dtype == RseOffer.BUY) {
            RseItem item = rseHoldings.findItem(symbol);
            item.setAwaitingSale(item.getAwaitingSale() - amount);
            item.setQuantity(item.getQuantity() - amount);
            if (item.getQuantity() == 0)
                rseHoldings.getRseItem().remove(item);

        }
        if (dtype == RseOffer.SELL) {
            RseItem item = rseHoldings.findItem(symbol);
            if (item == null) {
                rseHoldings.getRseItem().add(new RseItem(symbol, amount));
            } else {
                item.setQuantity(item.getQuantity() + amount);
            }
        }
        isChanged.set(isChanged.not().get());
    }
}