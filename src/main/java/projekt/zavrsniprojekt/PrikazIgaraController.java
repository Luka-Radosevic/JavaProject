package projekt.zavrsniprojekt;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import niti.OsvjezavanjeIgara;
import niti.OsvjezavanjeKategorija;
import projekt.baza.BazaPodataka;
import projekt.entiteti.Igra;
import projekt.entiteti.Kategorija;
import projekt.entiteti.Platforma;
import projekt.entiteti.Publisher;
import projekt.iznimke.BazaPodatakaException;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class PrikazIgaraController{

    @FXML
    private TextField imeIgre;

    @FXML
    private TextField godinaIzlaska;

    @FXML
    private ComboBox<String> ocjenaIgre;

    @FXML
    private ComboBox<Publisher> publisher;

    @FXML
    private Button azuriraj;

    @FXML
    private Button brisi;

    @FXML
    private Button dodajRecenziju;

    @FXML
    private Text detaljiIme;

    @FXML
    private Text detaljiGodina;

    @FXML
    private Text detaljiOcjena;

    @FXML
    private Text detaljiPublisher;

    @FXML
    private TableView<Igra> igraTableView;

    @FXML
    private TableColumn<Igra, String> imeIgreTableColumn;

    @FXML
    private TableColumn<Igra, String> godinaIzlaskaTableColumn;

    @FXML
    private TableColumn<Igra, String> ocjenaIgreTableColumn;

    @FXML
    private TableColumn<Igra, String> publisherTableColumn;

    private FilteredList<Igra> filteredList;

    @FXML
    public void initialize() {
        List<Igra> igre = null;

        try{
            igre = new ArrayList<>(BazaPodataka.dohvatiSveIgre());
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        try{
            publisher.getItems().addAll(BazaPodataka.dohvatiSvePublishere());
            publisher.getItems().add(0, new Publisher((long) -1, "", "", ""));
            publisher.getSelectionModel().select(0);
        }catch(BazaPodatakaException ex){
            ex.printStackTrace();
        }

        for(int i = 0; i < 5; i++){
            ocjenaIgre.getItems().add(i, String.valueOf(i + 1));
        }

        ocjenaIgre.getItems().add(0, "");

        ocjenaIgre.getSelectionModel().select(0);

        imeIgreTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getNaziv());
        });

        godinaIzlaskaTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getGodinaIzlaska().toString());
        });

        ocjenaIgreTableColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().getOcjena() == null){
                return new SimpleStringProperty("Igra nema ocjenu");
            }
            return new SimpleStringProperty(cellData.getValue().getOcjena().toString());
        });

        publisherTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getPublisher().getNaziv());
        });

        ObservableList<Igra> igraObservableList = FXCollections.observableList(igre);
        filteredList = new FilteredList<>(igraObservableList);

        igraTableView.setItems(filteredList);
    }

    @FXML
    protected void onSearchButtonClick(){
        String unesenoIme = imeIgre.getText();
        String unesenaGodina = godinaIzlaska.getText();
        String odabranaOcjena = ocjenaIgre.getValue();
        Publisher uneseniPublisher = publisher.getSelectionModel().getSelectedItem();
        Predicate<Igra> predikat = null;

        if(uneseniPublisher.getId() == -1 && odabranaOcjena.isBlank()){
            predikat = p -> p.getNaziv().contains(unesenoIme) &&
                            p.getGodinaIzlaska().toString().contains(unesenaGodina);
        }
        else if(uneseniPublisher.getId() == -1 && !odabranaOcjena.isBlank()){
            predikat = p -> {
                if(p.getOcjena() == null){
                    return p.getNaziv().contains(unesenoIme) &&
                            p.getGodinaIzlaska().toString().contains(unesenaGodina);
                }
                return p.getNaziv().contains(unesenoIme) &&
                        p.getGodinaIzlaska().toString().contains(unesenaGodina) &&
                        p.getOcjena().toString().equals(odabranaOcjena);
            };
        }
        else if(uneseniPublisher.getId() != -1 && odabranaOcjena.isBlank()){
            predikat = p -> p.getNaziv().contains(unesenoIme) &&
                            p.getGodinaIzlaska().toString().contains(unesenaGodina) &&
                            p.getPublisher().getNaziv().equals(uneseniPublisher.getNaziv());
        }
        else{
            predikat = p -> {
                if(p.getOcjena() == null){
                    return p.getNaziv().contains(unesenoIme) &&
                            p.getGodinaIzlaska().toString().contains(unesenaGodina) &&
                            p.getPublisher().getNaziv().equals(uneseniPublisher.getNaziv());
                }
                return p.getNaziv().contains(unesenoIme) &&
                        p.getGodinaIzlaska().toString().contains(unesenaGodina) &&
                        p.getPublisher().getNaziv().equals(uneseniPublisher.getNaziv()) &&
                        p.getOcjena().toString().equals(odabranaOcjena);
            };
        }

        if(predikat == null){
            return;
        }

        filteredList.setPredicate(predikat);
    }

    @FXML
    protected void onMouseClick(){
        ObservableList<TablePosition> positionObservableList = igraTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Igra igra = igraTableView.getItems().get(redak);

        detaljiIme.setText(igra.getNaziv());
        detaljiGodina.setText(igra.getGodinaIzlaska().toString());

        if(igra.getOcjena() == null){
            detaljiOcjena.setText("Nema ocjena");
        }
        else{
            detaljiOcjena.setText(igra.getOcjena().toString());
        }
        detaljiPublisher.setText(igra.getPublisher().getNaziv());

        if(MainScreen.globalniKorisnik.getNazivRazine().getRazinaNaziv().equals("ADMIN")){
            azuriraj.setVisible(true);
            brisi.setVisible(true);
        }
    }

    @FXML
    protected void onRecenzijaButtonClick(){
        ObservableList<TablePosition> positionObservableList = igraTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Igra igra = igraTableView.getItems().get(redak);
    }

    @FXML
    protected void onAzurirajClick(){
        ObservableList<TablePosition> positionObservableList = igraTableView.getSelectionModel().getSelectedCells();
        if(positionObservableList.size() == 0){
            return;
        }

        TablePosition position = positionObservableList.get(0);
        Integer redak = position.getRow();
        Igra igra = igraTableView.getItems().get(redak);

        BorderPane root;
        try{
            root = (BorderPane)  FXMLLoader.load(
                    getClass().getResource("unosIgara.fxml"));
            MainScreen.setData(igra);
            MainScreen.setMainPage(root, "Ažuriranje Igre");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onObrisiClick(){
        TableView.TableViewSelectionModel<Igra> selectionModel = igraTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try{
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati igru?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Potvrdite brisanje igre");

            Optional<ButtonType> result = confirmation.showAndWait();

            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiIgru(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Igra uspješno izbrisana!");
                alert.setHeaderText("Izbrisana!");
                alert.setContentText("Igra " + selectionModel.getSelectedItem().getNaziv() + " je izbrisana!");
                alert.showAndWait();

                Platform.runLater(new OsvjezavanjeIgara(igraTableView));
            }
        }catch(BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Neuspješno brisanje!");
            alert.setHeaderText("Nije obrisano!");
            alert.setContentText(ex.toString());

            alert.showAndWait();
            throw new RuntimeException(ex);
        }
    }
}
