package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rse.RizpaStockExchangeDescriptor;

import java.io.File;
import java.util.Optional;

public class Main extends Application {

    static private RizpaStockExchangeDescriptor descriptor;
    static private Stage primaryStage;
    static private MenuController menuController;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("newmenu.fxml"));
        Parent p = loader.load();
        menuController = (MenuController) loader.getController();
        primaryStage.setTitle("RSE");
        primaryStage.setScene(new Scene(p, 850, 500));
        primaryStage.show();
    }

    public static void loadFromFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        Task<Boolean> task = new LoadFileTask(selectedFile,descriptor,menuController);
        bindTaskToUIComponents(task,()->{});
        new Thread(task).start();
    }
    public static void clearFile(){
        descriptor = null;
    }
    public static void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task message
        //taskMessageLabel.textProperty().bind(aTask.messageProperty());

        // task progress bar
        menuController.getTaskPB().progressProperty().bind(aTask.progressProperty());

        // task percent label
        /*progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));
*/
        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }
    public static void onTaskFinished(Optional<Runnable> onFinish) {
        /*this.taskMessageLabel.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();*/
        menuController.getTaskPB().progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }
    public static void main(String[] args) {
        launch(args);
    }

}
