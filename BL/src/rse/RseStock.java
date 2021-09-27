package rse;//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.05.31 at 07:45:33 PM IDT 
//


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element ref="{}rse-symbol"/>
 *         &lt;element ref="{}rse-company-name"/>
 *         &lt;element ref="{}rse-price"/>
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
@XmlRootElement(name = "rse-stock")
public class RseStock {

    @XmlElement(name = "rse-symbol", required = true)
    protected String rseSymbol;
    @XmlElement(name = "rse-company-name", required = true)
    protected String rseCompanyName;
    @XmlElement(name = "rse-price")
    protected int rsePrice;

    protected RseOffers sellOffers = new RseOffers();
    protected RseOffers buyOffers = new RseOffers();
    protected RseOffers dealsMade = new RseOffers();

    public RseOffers getBuyOffers() {
        return buyOffers;
    }

    public RseOffers getDealsMade() {
        return dealsMade;
    }

    public RseOffers getSellOffers() {
        return sellOffers;
    }


    /**
     * Gets the value of the rseSymbol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRseSymbol() {
        return rseSymbol;
    }

    /**
     * Sets the value of the rseSymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRseSymbol(String value) {
        this.rseSymbol = value;
    }

    /**
     * Gets the value of the rseCompanyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRseCompanyName() {
        return rseCompanyName;
    }

    /**
     * Sets the value of the rseCompanyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRseCompanyName(String value) {
        this.rseCompanyName = value;
    }

    /**
     * Gets the value of the rsePrice property.
     * 
     */
    public int getRsePrice() {
        return rsePrice;
    }

    /**
     * Sets the value of the rsePrice property.
     * 
     */
    public void setRsePrice(int value) {
        this.rsePrice = value;
    }

    @Override
    public String toString() {
        return  "Symbol = '" + rseSymbol + '\'' +
                ", Name = '" + rseCompanyName + '\'' +
                ", Price = " + rsePrice;
    }

    public List<String> addOffer(int action, RseOffer offer) {
        List<String> res = new ArrayList<>();

        if (action == RseOffer.BUY) {
            res = makeSale(offer,
                    false,
                    sellOffers.getRseOffer(),
                    buyOffers.getRseOffer(),
                    "Added to Buy Offers");
        } else if (action == RseOffer.SELL) {
            res = makeSale(offer,
                    true,
                    buyOffers.getRseOffer(),
                    sellOffers.getRseOffer(),
                    "Added to Sell Offers");
        }

        buyOffers.getRseOffer().sort((o1, o2) -> {
            if (!o1.getPrice().equals(o2.getPrice()))
                return o2.getPrice().compareTo(o1.getPrice());
            if (!o1.getAmount().equals(o2.getAmount()))
                return o2.getAmount().compareTo(o1.getAmount());
            return o1.getDateOfDeal().compareTo(o2.getDateOfDeal());
        });
        sellOffers.getRseOffer().sort(( o1,  o2) ->{
            if (!o1.getPrice().equals(o2.getPrice()))
                return o1.getPrice().compareTo(o2.getPrice());
            if (!o1.getAmount().equals(o2.getAmount()))
                return o2.getAmount().compareTo(o1.getAmount());
            return o1.getDateOfDeal().compareTo(o2.getDateOfDeal());
        });
        return res;
    }

    private List<String> makeSale(RseOffer offer,
                                  boolean isSell,
                                  List<RseOffer> rseOfferList,
                                  List<RseOffer> addToList,
                                  String lstStr) {

        List<String> res = new ArrayList<>();
        List<RseOffer> toBeDeleted = new ArrayList<>();
        int newPrice = -1;

        res.add("Deal Made: ");
        for (RseOffer rseOffer : rseOfferList) {
            if ((offer.getPrice() == RseOffer.MKT_CODE)//MKT
                    || ((offer.getPrice() >= rseOffer.getPrice()) && !isSell)//LMT buy
                    || ((offer.getPrice() <= rseOffer.getPrice() )&& isSell)) //LMT sell
            {
                if (newPrice < rseOffer.getPrice())
                    rsePrice = newPrice = rseOffer.getPrice();

                if (offer.getAmount().compareTo(rseOffer.getAmount()) >= 0) {
                    offer.setAmount(offer.getAmount() - rseOffer.getAmount());
                    offer.getUser().offerAccepted(rseSymbol,rseOffer.getAmount(),isSell? RseOffer.BUY:RseOffer.SELL);
                    rseOffer.getUser().offerAccepted(rseSymbol,rseOffer.getAmount(),isSell?RseOffer.SELL : RseOffer.BUY);
                    toBeDeleted.add(rseOffer);
                    res.add(addToDealsMade(rseOffer.getUser(),offer.getUser(),offer.getDateOfDeal(), rseOffer.getAmount(), rseOffer.getPrice(), rseOffer.isMKT()).toString());
                } else {
                    rseOffer.setAmount(rseOffer.getAmount() - offer.getAmount());
                    offer.getUser().offerAccepted(rseSymbol,offer.getAmount(),isSell? RseOffer.BUY:RseOffer.SELL);
                    rseOffer.getUser().offerAccepted(rseSymbol,offer.getAmount(),isSell?RseOffer.SELL : RseOffer.BUY);
                    res.add(addToDealsMade(rseOffer.getUser(),offer.getUser(), offer.getDateOfDeal(), offer.getAmount(), rseOffer.getPrice(), rseOffer.isMKT()).toString());
                    offer.setAmount(0);
                }

                if (offer.getAmount() == 0) {
                    rseOfferList.removeAll(toBeDeleted);
                    return res;
                }
            }
        }

        rseOfferList.removeAll(toBeDeleted);
        if(offer.getPrice() == -1)
            offer = new RseOffer(offer.getUser(),null,offer.getDateOfDeal(),offer.getAmount(),rsePrice, offer.isMKT());
        addToList.add(offer);

        if (res.size() == 1)
            res = new ArrayList<>();
        res.add(lstStr);
        res.add(offer.toString());
        return res;
    }

    private RseOffer addToDealsMade(RseUser user,RseUser user2, String date, int amount, int price,boolean isMKT) {
        RseOffer rseOffer = new RseOffer(user,user2,date, amount, price,isMKT);
        dealsMade.addRseOffer(rseOffer);
        return rseOffer;

    }
}
