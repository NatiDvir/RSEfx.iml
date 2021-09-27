package rse;

import java.text.SimpleDateFormat;

public class RseOffer {

    public static final int MKT_CODE = -1;
    public static final int BUY =1;
    public static final int SELL =2;

    protected String dateOfDeal;

    protected RseUser user;
    protected RseUser secondUser;
    protected Integer amount;
    protected Integer price;
    protected boolean isMKT;


    public RseOffer(){}

    public RseOffer( RseUser user, Integer amount, Integer price, boolean isMKT) {
        this.dateOfDeal = new SimpleDateFormat("HH:mm:ss:SSS").format(System.currentTimeMillis());
        this.user = user;
        this.amount = amount;
        this.price = price;
        this.isMKT = isMKT;
    }



    public RseOffer(RseUser user,RseUser user2, String dateOfDeal, Integer amount, Integer price, boolean isMKT) {
        this.user = user;
        this.secondUser = user2;
        this.dateOfDeal = dateOfDeal;
        this.amount = amount;
        this.price = price;
        this.isMKT = isMKT;
    }

    public RseUser getUser() {
        return user;
    }


    public String getDateOfDeal() {
        return dateOfDeal;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean isMKT() {
        return isMKT;
    }

    @Override
    public String toString() {
        return "Date: " + dateOfDeal
                + ", Deal type: " + (isMKT ? "MKT":"LMT")
                + ", Amount of Stocks in deal: " + amount
                + ", Price of each Stock: " + price
                + ", Total price of deal: " + price * amount
                + ", Initiating User: "+user.getName()
                + ((secondUser != null)? ", Closing User: "+secondUser.getName():"");
    }
}
