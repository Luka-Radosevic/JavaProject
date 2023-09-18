package projekt.zavrsniprojekt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import projekt.entiteti.Kategorija;

import java.io.IOException;

public class PocetnaController {

    @FXML
    private MenuBar menu;

    @FXML
    private MenuItem unosKategorije;

    @FXML
    private MenuItem unosIgre;

    @FXML
    private MenuItem unosPlatforme;

    @FXML
    private MenuItem unosProdajnogMjesta;

    @FXML
    private MenuItem unosPublishera;

    @FXML
    private Menu user;

    @FXML
    public void prikazKategorija(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("prikazKategorija.fxml"));
            MainScreen.setMainPage(root, "Kategorije");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void unosKategorija(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosKategorija.fxml"));
            MainScreen.setData(null);
            MainScreen.setMainPage(root, "Unos kategorija");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void prikazIgara(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("prikazIgara.fxml"));
            MainScreen.setMainPage(root, "Igre");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void unosIgara(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosIgara.fxml"));
            MainScreen.setData(null);
            MainScreen.setMainPage(root, "Unos igara");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void prikazPlatforma(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("prikazPlatformi.fxml"));
            MainScreen.setMainPage(root, "Platforme");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void unosPlatforma(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosPlatforma.fxml"));
            MainScreen.setData(null);
            MainScreen.setMainPage(root, "Unos platformi");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void prikazProdajnihMjesta(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("prikazProdajnihMjesta.fxml"));
            MainScreen.setMainPage(root, "Prodajna mjesta");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void unosProdajnihMjesta(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosProdajnogMjesta.fxml"));
            MainScreen.setData(null);
            MainScreen.setMainPage(root, "Unos prodajnih mjesta");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void prikazPublishera(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("prikazPublishera.fxml"));
            MainScreen.setMainPage(root, "Publisheri");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void unosPublishera(){
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosPublishera.fxml"));
            MainScreen.setData(null);
            MainScreen.setMainPage(root, "Unos publishera");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void receiveData(){
        user.setText(MainScreen.globalniKorisnik.getKorisnickoIme());
        if(MainScreen.globalniKorisnik.getNazivRazine().getRazinaNaziv().equals("USER")){
            unosKategorije.setVisible(false);
            unosIgre.setVisible(false);
            unosPlatforme.setVisible(false);
            unosProdajnogMjesta.setVisible(false);
            unosPublishera.setVisible(false);
        }

        user.setVisible(true);
    }

    @FXML
    private void odjava(){
        MainScreen.logoutKorisnik();
        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("main.fxml"));
            MainScreen.setData(null);
            MainScreen.setMainPage(root, "main");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
