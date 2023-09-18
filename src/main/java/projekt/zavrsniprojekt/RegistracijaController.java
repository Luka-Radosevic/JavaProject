package projekt.zavrsniprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import projekt.datoteke.Datoteke;
import projekt.entiteti.Korisnik;
import projekt.entiteti.RazinaPristupa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistracijaController {

    public static List<Korisnik> korisnici = new ArrayList<>();

    @FXML
    private TextField korisnickoIme;

    @FXML
    private PasswordField lozinka;

    @FXML
    private PasswordField ponovljenaLozinka;

    @FXML
    private Label errori;

    @FXML
    private Button registracijaButton;

    @FXML
    public void registracija(){

        String unesenoIme = korisnickoIme.getText();
        String unesenaLozinka = lozinka.getText();
        String unesenaPonovljena = ponovljenaLozinka.getText();

        if(unesenoIme.isEmpty() || unesenoIme.isBlank() || unesenaLozinka.isEmpty() || unesenaLozinka.isBlank()
                ||unesenaPonovljena.isEmpty() || unesenaPonovljena.isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sva polja moraju biti unešena!");
            alert.setContentText("Sva polja moraju biti unešena!");
            alert.showAndWait();
            return;
        }

        if(!unesenaLozinka.equals(unesenaPonovljena)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lozinke se ne podudaraju!");
            alert.setContentText("Lozinke se ne podudaraju!");
            alert.showAndWait();
            return;
        }

        List<Korisnik> listaKorisnika = Datoteke.dohvatiKorisnike();

        for(Korisnik korisnik : listaKorisnika){
            if(korisnik.getKorisnickoIme().equals(unesenoIme)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Već postoji korisnik s tim imenom");
                alert.setContentText("Već postoji korisnik s tim imenom");
                alert.showAndWait();
                return;
            }
        }

        Datoteke.unesiKorisnike(new Korisnik(unesenoIme, unesenaLozinka, new RazinaPristupa("USER")));

        BorderPane root;
        try {
            root = (BorderPane) FXMLLoader.load(
                    getClass().getResource("login.fxml"));
            MainScreen.setMainPage(root, "Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
