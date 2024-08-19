package sceneTryOuts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import application.commonResourcePaths.CommonResourcePaths;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(CommonResourcePaths.APP_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        //Thread.currentThread().setName("main");
        launch(args);
    }
}
