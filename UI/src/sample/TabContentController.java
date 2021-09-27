package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import rse.RizpaStockExchangeDescriptor;
import rse.RseItem;
import rse.RseOffer;
import rse.RseUser;

import java.util.List;

public class TabContentController {


    //region members
    @FXML
    private CheckBox mktCB;
    @FXML
    private TextField amountTB;
    @FXML
    private TextField priceTB;
    @FXML
    private TextField symbolTB;

    @FXML
    private ToggleGroup buySell;
    @FXML
    private RadioButton sellTS;
    @FXML
    private RadioButton buyTS;

    @FXML
    private Label holdingsWorth;
    @FXML
    private Label clientName;

    @FXML
    private ListView<RseItem> holdingsLV;

    private RseUser user;
    private RizpaStockExchangeDescriptor descriptor;
    private SimpleBooleanProperty isChanged = new SimpleBooleanProperty(true);
    //endregion

    public void setClient(RseUser u, RizpaStockExchangeDescriptor d) {
        user = u;
        descriptor = d;
        user.setIsChanged(this.isChanged);

        clientName.setText(user.getName());
        user.getIsChangedProperty().addListener((observable, oldValue, newValue) -> {
            populateHoldingsList();
        });
        user.getIsChangedProperty().set(user.getIsChangedProperty().not().get());

        holdingsLV.setOnMouseClicked(event -> {
            ((ListView) event.getSource()).getId();
            RseItem holding = holdingsLV.getSelectionModel().getSelectedItem();
            if (holding != null) {
                symbolTB.setText(holding.getSymbol());
                sellTS.setSelected(true);
            }
        });

        amountTB.textProperty().addListener((observable, oldValue, newValue) -> {
            checkInput(amountTB, newValue);
        });
        priceTB.textProperty().addListener((observable, oldValue, newValue) -> {
            checkInput(priceTB, newValue);
        });
    }

    private void checkInput(TextField textBox, String input) {
        if (!input.matches("\\d*")) {
            textBox.setText(input.replaceAll("[^\\d]", ""));
        }
    }

    private void populateHoldingsList() {
        int res = 0;
        holdingsLV.getItems().clear();
        for (RseItem item : user.getRseHoldings().getRseItem()) {
            holdingsLV.getItems().add(item);
            res += item.getQuantity() * descriptor.findStock(item.getSymbol()).getRsePrice();
        }
        holdingsWorth.setText(Integer.toString(res));
    }

    public TextField getSymbolTB() {
        buyTS.setSelected(true);
        return this.symbolTB;
    }

    public void executeTradeBtnClicked(ActionEvent actionEvent) {
        Alert alert;
        if (!isFormValid())
            return;

        boolean sale = buySell.getSelectedToggle().equals(sellTS);
        int price = -1;
        String symbol = symbolTB.getText();
        int amount = Integer.parseInt(amountTB.getText());

        if (amount <= 0) {
            alert = new Alert(Alert.AlertType.ERROR, "Filled amount too low", ButtonType.OK);
            alert.show();
            return;
        }

        if (sale) {
            if (!user.checkSale(amount, symbol)) {
                alert = new Alert(Alert.AlertType.ERROR, "Filled amount too high", ButtonType.OK);
                alert.show();
                return;
            }
        }

        if (!priceTB.getText().isEmpty() && !mktCB.isSelected())
            price = Integer.parseInt(priceTB.getText());

        makeTrade(sale ? RseOffer.SELL : RseOffer.BUY,price,symbol,amount);
    }

    public void makeTrade(int dealType, int price, String symbol, int amount) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.CANCEL);
        alert.setContentText("Complete " + (dealType == RseOffer.SELL ? "sale" : "purchase") +
                " of stock " + symbol +
                " for " + amount + " shares" +
                (mktCB.isSelected() ? "?" : " at " + price + " per share?"));


        alert.showAndWait().ifPresent(res -> {
            if (ButtonType.YES.equals(res)) {
                if (dealType == RseOffer.SELL) {
                    user.getRseHoldings().findItem(symbol).addToAwaitingSale(amount);
                }

                List<String> strList = descriptor.findStock(symbol)
                        .addOffer(dealType
                                , new RseOffer(user, amount,  price, mktCB.isSelected()));

                String resStr = "";
                for (String str : strList) {
                    resStr += str + "\n";
                }
                descriptor.getIsChangedProperty().set(descriptor.getIsChangedProperty().not().get());

                Alert resAlert = new Alert(Alert.AlertType.INFORMATION, "Command Completed");
                HBox hBox = new HBox();
                resAlert.getDialogPane().setExpandableContent(new Label(resStr));
                resAlert.setResizable(true);
                resAlert.setWidth(800.0);
                resAlert.show();

                populateHoldingsList();
            }
        });
    }

    private boolean isFormValid() {
        String errorStr = "";
        errorStr += (buySell.getSelectedToggle() == null) ? "-No Symbol selected\n" : "";
        errorStr += amountTB.getText().isEmpty() ? "-No amount filled\n" : "";
        errorStr += (priceTB.getText().isEmpty() && !mktCB.isSelected()) ? "-No price filled\n" : "";
        if (!errorStr.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, errorStr, ButtonType.OK);
            alert.show();
            return false;
        }
        return true;
    }
}
