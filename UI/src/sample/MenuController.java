package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rse.RizpaStockExchangeDescriptor;
import rse.RseStock;
import rse.RseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuController {
    @FXML
    private ProgressBar taskPB;

    @FXML
    private Button loadFileBtn;
    @FXML
    private Button clearFileBtn;
    @FXML
    private TabPane myTabs;
    @FXML
    private ScrollPane mySP;
    @FXML
    private ListView<RseStock> allStocksLV;
    @FXML
    private Label historyErrorLbl;
    private final String EXAMPLE_TAB_ID = "Example";

    private RizpaStockExchangeDescriptor descriptor;
    final private SimpleBooleanProperty isLoaded;
    List<TabContentController> tabContentControllerList;
    private SimpleBooleanProperty isChanged = new SimpleBooleanProperty(true);

    public MenuController() {
        descriptor = null;
        isLoaded =new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        tabContentControllerList = new ArrayList<>();
        mySP.visibleProperty().bind(isLoaded.not().not());
        clearFileBtn.disableProperty().bind(isLoaded.not());
        loadFileBtn.disableProperty().bind(isLoaded.not().not());
        clearFileBtn.setOnMouseClicked(event->{
            clearDescriptor();
        });
        allStocksLV.setOnMouseClicked(event->{
            int index = myTabs.getSelectionModel().getSelectedIndex();
            String symbol = allStocksLV.getSelectionModel().getSelectedItem().getRseSymbol();
            tabContentControllerList.get(index).getSymbolTB().setText(symbol);
        });
    }
    public ProgressBar getTaskPB() {
        return taskPB;
    }

    public void setDescriptor(RizpaStockExchangeDescriptor descriptor) {
        this.descriptor = descriptor;
        if(descriptor == null)
            return;
        descriptor.setIsChanged(this.isChanged);
        descriptor.getIsChangedProperty().addListener((observable, oldValue, newValue) -> {
            unPopulateAllStocksList();
            populateAllStocksList();
        });
        descriptor.getIsChangedProperty().set(descriptor.getIsChangedProperty().not().get());
        isLoaded.set(true);
        populateTabs();
    }
    public void clearDescriptor(){
        isLoaded.set(false);
        removeAllTabs();
        unPopulateAllStocksList();
        Main.clearFile();
    }

    @FXML
    public void onLoadFileClicked(ActionEvent e){
        Main.loadFromFile();
    }

    @FXML
    public void onShowHistoryClicked() {
        RseStock stock = allStocksLV.getSelectionModel().getSelectedItem();
        if (stock == null) {
            historyErrorLbl.setVisible(true);
            historyErrorLbl.setText("Please select stock from list above");
            return;
        }
        historyErrorLbl.setVisible(false);

        Stage stage = new Stage();
        stage.setTitle("History");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("history.fxml"));

        try {
            Parent p = loader.load();
            stage.setScene(new Scene(p));
            ((HistoryController) loader.getController()).showHistory(stock);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateAllStocksList() {
        for (RseStock s:descriptor.getRseStocks().getRseStock()) {
            allStocksLV.getItems().add(s);
        }
    }

    private void unPopulateAllStocksList() {
        allStocksLV.getItems().clear();
    }

    private void populateTabs() {
        for (RseUser u:descriptor.getRseUsers().getRseUser()) {
            addTab(u);
        }
    }

    public void addTab(RseUser user){
        Tab newt = new Tab(user.getName());
        newt.setClosable(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clienttab.fxml"));

        try {
            newt.setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tabContentControllerList.add(loader.getController());
        tabContentControllerList.get(tabContentControllerList.size()-1).setClient(user,descriptor);
        myTabs.getTabs().add(newt);
    }
    public void removeAllTabs(){
        myTabs.getTabs().clear();
        tabContentControllerList.clear();
    }

    public void loadFail(String errStr,String errTtl){
        Alert a = new Alert(Alert.AlertType.ERROR,errStr);
        a.setTitle(errTtl);
        a.show();
    }
}
