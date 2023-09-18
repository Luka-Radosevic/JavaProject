package projekt.zavrsniprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kriptiranje.Enkripcija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projekt.entiteti.Korisnik;
import projekt.iznimke.BazaPodatakaException;

import java.io.IOException;

public class MainScreen extends Application {

    public static final Logger logger = LoggerFactory.getLogger(MainScreen.class);
    private static Stage mainStage;

    public static Korisnik globalniKorisnik;

    public static Enkripcija globalnaEnkripcija;

    public static void loginKorisnik(Korisnik korisnik){
        globalniKorisnik = korisnik;
    }

    public static void logoutKorisnik(){
        globalniKorisnik = null;
    }

    @Override
    public void start(Stage stage) throws IOException {
        try{
            globalnaEnkripcija = new Enkripcija();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainScreen.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setMainPage(BorderPane root, String stageName){
        Scene scene = new Scene(root, 872, 600);
        root.setStyle("-fx-background: rgb(225, 228, 203)");
        mainStage.setTitle(stageName);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void setData(Object object){
        mainStage.setUserData(object);
    }

    public static Object getData(){
        return mainStage.getUserData();
    }
}