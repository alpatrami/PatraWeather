package ro.mta.facc.selab.tema2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *      Clasa principala a aplicatiei
 *      Aplicația Java își propune să ofere informații despre vreme pornind de la un fișier (input.txt) cu date despre diferite localități din diferite părți ale globului.
 *      Aplicația încarcă datele făcând request-uri către API-ul oferit de OpenWeather pentru informațiile necesare. Aplicația folosește JavaFX si minimal-json.
 *      Interfața grafică este intuitivă și ușor de folosit.
 *
 * @author Alin Pătrașcu
 */

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        primaryStage.setTitle("Patra's Weather");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/icon.png")));
        try {
            loader.setLocation(this.getClass().getResource("/view/PatraWeatherInterface.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
