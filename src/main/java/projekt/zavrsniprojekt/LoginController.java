package projekt.zavrsniprojekt;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import kriptiranje.Enkripcija;
import projekt.datoteke.Datoteke;
import projekt.entiteti.Korisnik;
import javafx.fxml.FXML;
import projekt.files.Utilization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {

    public static List<Korisnik> korisnici = new ArrayList<>();

    @FXML
    private TextField korisnickoIme;

    @FXML
    private PasswordField lozinka;

    @FXML
    private Label errori;

    @FXML
    private Button loginButton;

    @FXML
    public void login(){

        List<Korisnik> listaKorisnika = Datoteke.dohvatiKorisnike();
        String unesenoIme = korisnickoIme.getText();
        String unesenaLozinka = lozinka.getText();
        try{
            for(Korisnik korisnik : listaKorisnika){
                if(unesenoIme.equals(korisnik.getKorisnickoIme())){
                    if(unesenaLozinka.equals(korisnik.getLozinka())){
                        MainScreen.loginKorisnik(korisnik);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if(MainScreen.globalniKorisnik == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Neuspješno ulogiravanje");
            alert.setContentText("Neispravno ime ili šifra!");
            alert.showAndWait();
        }
        else{
            BorderPane root;
            try {
                root = (BorderPane) FXMLLoader.load(
                        getClass().getResource("pocetni.fxml"));
                MainScreen.setMainPage(root, "Pocetni ekran");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
