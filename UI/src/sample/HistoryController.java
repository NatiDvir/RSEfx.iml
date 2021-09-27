package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import rse.RseOffer;
import rse.RseStock;

public class HistoryController {
    private static final String EMPTY_LIST_STR = " No items in this list";

    @FXML
    private Label stockNameLbl;
    @FXML
    private ListView<RseOffer> selllistLV;
    @FXML
    private ListView<RseOffer> buyListLV;
    @FXML
    private ListView<RseOffer> dealsMadeListLV;

    @FXML
    private Label awaitingSellLbl;
    @FXML
    private Label awaitingBuyLbl;
    @FXML
    private Label dealsMadeLbl;

    public HistoryController(){}
    public void showHistory(RseStock stock) {
        stockNameLbl.setText(stock.getRseSymbol());
        if (!stock.getSellOffers().getRseOffer().isEmpty()) {
            for (RseOffer offer : stock.getSellOffers().getRseOffer())
                selllistLV.getItems().add(offer);
        } else {
            selllistLV.setVisible(false);
            awaitingSellLbl.setText(awaitingSellLbl.getText() + EMPTY_LIST_STR);
        }
        if (!stock.getBuyOffers().getRseOffer().isEmpty()) {
            for (RseOffer offer : stock.getBuyOffers().getRseOffer())
                buyListLV.getItems().add(offer);
        } else {
            buyListLV.setVisible(false);
            awaitingBuyLbl.setText(awaitingBuyLbl.getText() + EMPTY_LIST_STR);
        }
        if (!stock.getDealsMade().getRseOffer().isEmpty()) {
            for (RseOffer offer : stock.getDealsMade().getRseOffer())
                dealsMadeListLV.getItems().add(offer);
        } else {
            dealsMadeListLV.setVisible(false);
            dealsMadeLbl.setText(dealsMadeLbl.getText() + EMPTY_LIST_STR);
        }
    }
}
