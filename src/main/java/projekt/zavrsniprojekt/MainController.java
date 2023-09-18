package projekt.zavrsniprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private Button loginButton;

    @FXML
    private Button registracijaButton;


    @FXML
    public void login(){
        BorderPane root;
        try{
            root = (BorderPane) FXMLLoader.load(
                    getClass().getResource("login.fxml"));
            MainScreen.setMainPage(root, "Login");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void registracija(){
        BorderPane root;
        try{
            root = (BorderPane) FXMLLoader.load(
                    getClass().getResource("registracija.fxml"));
            MainScreen.setMainPage(root, "Registracija");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}