package ro.mta.facc.selab.tema2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

//clasa principala a aplicatiei
public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        primaryStage.setTitle("Patra's Weather");  //setarea Titlului
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/icon.png"))); //setarea iconitei
        try {
            loader.setLocation(this.getClass().getResource("/view/PatraWeatherInterface.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
