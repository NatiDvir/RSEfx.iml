package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import rse.RizpaStockExchangeDescriptor;
import javafx.application.Platform;
import javafx.concurrent.Task;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoadFileTask extends Task<Boolean> {
    private final int SLEEP_TIME = 10;
    MenuController menuController;
    RizpaStockExchangeDescriptor descriptor;
    File xmlFile;

    public LoadFileTask(File xmlFile, RizpaStockExchangeDescriptor descriptor, MenuController menuController)
    {
        this.xmlFile = xmlFile;
        this.descriptor = descriptor;
        this.menuController = menuController;
    }

    @Override
    protected Boolean call() throws Exception {
        updateMessage("Fetching file...");
        updateProgress(0, 100);
        makeSleep(1, 10);
        if(xmlFile == null) {
            Platform.runLater(() -> menuController.loadFail("No file selected","File Error"));
            updateMessage("");
            updateProgress(0, 100);
            return false;
        }
        RizpaStockExchangeDescriptor unmarshal;
        try (InputStream inputStream = new FileInputStream(xmlFile)) {
            unmarshal = new RizpaStockExchangeDescriptor(inputStream);
            String str = unmarshal.checkRSED();
            if (str != null) {
                Platform.runLater(() -> menuController.loadFail(str,"Data Error"));
                updateMessage("");
                updateProgress(0, 100);
                Platform.runLater(()-> menuController.clearDescriptor());
                return false;
            }
        } catch (JAXBException | IOException ex) {
            Platform.runLater(() -> menuController.loadFail(ex.toString(),"Load Error"));

            updateMessage("");
            updateProgress(0, 100);
            Platform.runLater(()-> menuController.clearDescriptor());
            return false;
        }
        descriptor = new RizpaStockExchangeDescriptor();
        makeSleep(11, 20);
        updateMessage("Checking file content...");
        makeSleep(21, 30);


        makeSleep(31, 40);
        updateMessage("Load stocks..");
        makeSleep(41, 50);
        descriptor.setRseStocks(unmarshal.getRseStocks());
        makeSleep(51, 60);

        updateMessage("Load users..");
        descriptor.setRseUsers(unmarshal.getRseUsers());
        makeSleep(61, 80);

        Platform.runLater(()-> menuController.setDescriptor(descriptor));

        makeSleep(81, 100);
        updateMessage("Done...");
        Thread.sleep(SLEEP_TIME);
        updateProgress(0, 100);
        updateMessage("");

        return true;
    }

    //******************************************************************************//
    public void makeSleep(int start, int end) throws InterruptedException {
        for (int i = start; i <= end; i++) {
            Thread.sleep(SLEEP_TIME);
            updateProgress(i, 100);
        }
    }
    //******************************************************************************//
}
