package rse;//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.05.31 at 07:45:33 PM IDT 
//


import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}rse-stocks"/>
 *         &lt;element ref="{}rse-users"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "rizpa-stock-exchange-descriptor")
public class RizpaStockExchangeDescriptor {

    @XmlElement(name = "rse-stocks", required = true)
    protected RseStocks rseStocks;
    @XmlElement(name = "rse-users", required = true)
    protected RseUsers rseUsers;

    @XmlTransient
    protected SimpleBooleanProperty isChanged;

    public RizpaStockExchangeDescriptor(InputStream in)throws JAXBException {
        deserializeFrom(in);
    }
    public RizpaStockExchangeDescriptor(){}
    /**
     * Gets the value of the rseStocks property.
     *
     * @return
     *     possible object is
     *     {@link RseStocks }
     *
     */
    public RseStocks getRseStocks() {
        return rseStocks;
    }
    /**
     * Sets the value of the rseStocks property.
     *
     * @param value
     *     allowed object is
     *     {@link RseStocks }
     *
     */
    public void setRseStocks(RseStocks value) {
        this.rseStocks = value;
    }

    /**
     * Gets the value of the rseUsers property.
     *
     * @return
     *     possible object is
     *     {@link RseUsers }
     *
     */
    public RseUsers getRseUsers() {
        return rseUsers;
    }

    /**
     * Sets the value of the rseUsers property.
     *
     * @param value
     *     allowed object is
     *     {@link RseUsers }
     *
     */
    public void setRseUsers(RseUsers value) {
        this.rseUsers = value;
    }

    public SimpleBooleanProperty getIsChangedProperty() {
        return isChanged;
    }

    public void setIsChanged(SimpleBooleanProperty isChanged) {
        this.isChanged = isChanged;
    }

    public String checkRSED() {
        List<RseStock> stockList = new ArrayList<RseStock>();
        List<RseUser> userList = new ArrayList<RseUser>();

        //check stocks
        for (RseStock r : rseStocks.getRseStock()) {
            for (RseStock s : stockList) {
                if (r.getRseSymbol().equalsIgnoreCase(s.getRseSymbol()))
                    return "2 or more companies have same symbol";
                if (r.getRseCompanyName().equalsIgnoreCase(s.getRseCompanyName()))
                    return "company appears on 2 or more stocks";
            }
            stockList.add(r);
        }
        RseStock stock;
        //check users
        for (RseUser u:rseUsers.getRseUser()) {
            for (RseUser t : userList) {
                if (u.getName().equalsIgnoreCase(t.getName())) {
                    return "2 or more users have same name";
                }
            }
            for (RseItem item:u.getRseHoldings().getRseItem()) {
                stock = findStock(item.getSymbol());
                if(stock == null)
                    return "A user has a stock that doesn't exist";
            }
            userList.add(u);
        }
        return null;
    }
    public void deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("rse");
        Unmarshaller u = jc.createUnmarshaller();
        RizpaStockExchangeDescriptor r = ((RizpaStockExchangeDescriptor) u.unmarshal(in));
        this.rseStocks = r.rseStocks;
        this.rseUsers = r.rseUsers;
    }
    public RseStock findStock(String str) {
        for (RseStock r : getRseStocks().getRseStock()) {
            if (r.getRseSymbol().equalsIgnoreCase(str)) {
                return r;
            }
        }
        return null;
    }
}
