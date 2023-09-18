package projekt.baza;

import projekt.entiteti.*;
import projekt.iznimke.BazaPodatakaException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BazaPodataka {

    public static Connection spajanjeNaBazu() throws SQLException, IOException{
        Properties configuration = new Properties();
        configuration.load(new FileReader("dat/properties.txt"));

        String databaseURL = configuration.getProperty("databaseURL");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        Connection connection = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);

        return connection;
    }

    public static List<Kategorija> dohvatiSveKategorije() throws BazaPodatakaException{
        List<Kategorija> listaKategorija = new ArrayList<>();
        try(Connection veza = spajanjeNaBazu()){
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM KATEGORIJA");

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String imeKategorije = resultSet.getString("imeKategorije");
                String opisKategorije = resultSet.getString("opisKategorije");
                Kategorija novaKategorija = new Kategorija(id, imeKategorije, opisKategorije);
                listaKategorija.add(novaKategorija);
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaKategorija;
    }

    public static List<Igra> dohvatiSveIgre() throws BazaPodatakaException{
        List<Igra> listaIgara = new ArrayList<>();
        try(Connection veza = spajanjeNaBazu()){
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM IGRA");

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String imeIgre = resultSet.getString("imeIgre");
                Integer godinaIzlaska = resultSet.getInt("godinaIzlaska");
                Long idPublishera = resultSet.getLong("publisherId");

                Publisher publisher = getPublisher(veza, idPublishera);

                Igra novaIgra = new Igra.IgraBuilder(id)
                                        .newIme(imeIgre)
                                        .newGodina(godinaIzlaska)
                                        .newPublisher(publisher)
                                        .build();
                listaIgara.add(novaIgra);
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaIgara;
    }

    private static Publisher getPublisher(Connection veza, Long idPublishera) throws SQLException {
        Statement upit = veza.createStatement();
        ResultSet resultSet = upit.executeQuery("SELECT * FROM PUBLISHER WHERE id=" + idPublishera);
        Publisher publisher = null;
        while (resultSet.next()){
            if(idPublishera.equals(resultSet.getLong("id"))){
                publisher = new Publisher(
                        resultSet.getLong("id"),
                        resultSet.getString("imePublishera"),
                        resultSet.getString("zemlja"),
                        resultSet.getString("dodatniInfo")
                );
            }
        }
        return publisher;
    }

    public static List<Platforma> dohvatiSvePlatforme() throws BazaPodatakaException{
        List<Platforma> listaPlatformi = new ArrayList<>();
        try(Connection veza = spajanjeNaBazu()){
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM PLATFORMA");

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String imePlatforme = resultSet.getString("imePlatforme");
                String urlPlatforme = resultSet.getString("urlPlatforme");
                Platforma novaPlatforma = new Platforma(id, imePlatforme, urlPlatforme);
                listaPlatformi.add(novaPlatforma);
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaPlatformi;
    }

    public static List<ProdajnoMjesto> dohvatiSvaProdajnaMjesta() throws BazaPodatakaException{
        List<ProdajnoMjesto> listaProdajnihMjesta = new ArrayList<>();
        try(Connection veza = spajanjeNaBazu()){
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM PRODAJNOMJESTO");

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String imeProdajnogMjesta = resultSet.getString("imeProdajnogMjesta");
                String adresaProdajnogMjesta = resultSet.getString("adresaProdajnogMjesta");
                ProdajnoMjesto novoProdajnoMjesto = new ProdajnoMjesto(id, imeProdajnogMjesta, adresaProdajnogMjesta);
                listaProdajnihMjesta.add(novoProdajnoMjesto);
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaProdajnihMjesta;
    }

    public static List<Publisher> dohvatiSvePublishere() throws BazaPodatakaException{
        List<Publisher> listaPublishera = new ArrayList<>();
        try(Connection veza = spajanjeNaBazu()){
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery("SELECT * FROM PUBLISHER");

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String imePublishera = resultSet.getString("imePublishera");
                String zemlja = resultSet.getString("zemlja");
                String dodatniInfo = resultSet.getString("dodatniInfo");
                Publisher noviPublisher = new Publisher(id, imePublishera, zemlja, dodatniInfo);
                listaPublishera.add(noviPublisher);
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaPublishera;
    }

    public static void dodajKategoriju(String ime, String opis) throws BazaPodatakaException{
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO KATEGORIJA(IMEKATEGORIJE, OPISKATEGORIJE) VALUES(?, ?)");
            preparedStatement.setString(1, ime);
            preparedStatement.setString(2, opis);
            preparedStatement.executeUpdate();
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static Long dodajIgru(String ime, Integer godina, Long idPublishera) throws BazaPodatakaException{
        Long idIgre = Long.valueOf(-1);
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO IGRA(IMEIGRE, GODINAIZLASKA, PUBLISHERID) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, ime);
            preparedStatement.setInt(2, godina);
            preparedStatement.setLong(3, idPublishera);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()){
                idIgre = resultSet.getLong(1);
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return idIgre;
    }

    public static void dodajKategorijuIgre(Long idIgre, Long idKategorije) throws BazaPodatakaException{
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO IGRA_KATEGORIJA(IGRAID, KATEGORIJAID) VALUES(?, ?)");
            preparedStatement.setLong(1, idIgre);
            preparedStatement.setLong(2, idKategorije);
            preparedStatement.executeUpdate();
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void dodajPlatformuIgre(Long idIgre, Long idPlatforme) throws BazaPodatakaException{
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO IGRA_PLATFORMA(IGRAID, PLATFORMAID) VALUES(?, ?)");
            preparedStatement.setLong(1, idIgre);
            preparedStatement.setLong(2, idPlatforme);
            preparedStatement.executeUpdate();
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void dodajProdajnoMjestoIgre(Long idIgre, Long idProdajnogMjesta) throws BazaPodatakaException{
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO IGRA_PRODAJNOMJESTO(IGRAID, PRODAJNOMJESTOID) VALUES(?, ?)");
            preparedStatement.setLong(1, idIgre);
            preparedStatement.setLong(2, idProdajnogMjesta);
            preparedStatement.executeUpdate();
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void dodajPlatformu(String ime, String urlPlatforme) throws BazaPodatakaException{
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO PLATFORMA(IMEPLATFORME, URLPLATFORME) VALUES(?, ?)");
            preparedStatement.setString(1, ime);
            preparedStatement.setString(2, urlPlatforme);
            preparedStatement.executeUpdate();
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void dodajProdajnoMjesto(String ime, String adresa) throws BazaPodatakaException{
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO PRODAJNOMJESTO(IMEPRODAJNOGMJESTA, ADRESAPRODAJNOGMJESTA) VALUES(?, ?)");
            preparedStatement.setString(1, ime);
            preparedStatement.setString(2, adresa);
            preparedStatement.executeUpdate();
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void dodajPublishera(String ime, String zemlja, String dodatniInfo) throws BazaPodatakaException{
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza
                    .prepareStatement("INSERT INTO PUBLISHER(IMEPUBLISHERA, ZEMLJA, DODATNIINFO) VALUES(?, ?, ?)");
            preparedStatement.setString(1, ime);
            preparedStatement.setString(2, zemlja);
            preparedStatement.setString(3, dodatniInfo);
            preparedStatement.executeUpdate();
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do greške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajKategoriju(Long id, String ime, String opis) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("UPDATE KATEGORIJA SET IMEKATEGORIJE = ?, OPISKATEGORIJE = ? WHERE ID = ?");
            preparedStatement.setString(1, ime);
            preparedStatement.setString(2, opis);
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri ažuriranju kategorije!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajIgru(Long id, String ime, Integer godina, Long publisherId) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("UPDATE IGRA SET IMEIGRE = ?, GODINAIZLASKA = ?, PUBLISHERID = ? WHERE ID = ?");
            preparedStatement.setString(1, ime);
            preparedStatement.setInt(2, godina);
            preparedStatement.setLong(3, publisherId);
            preparedStatement.setLong(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri ažuriranju igre!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajPlatformu(Long id, String ime, String url) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("UPDATE PLATFORMA SET IMEPLATFORME = ?, URLPLATFORME = ? WHERE ID = ?");
            preparedStatement.setString(1, ime);
            preparedStatement.setString(2, url);
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri ažuriranju platforme!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajProdajnoMjesto(Long id, String ime, String adresa) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("UPDATE PRODAJNOMJESTO SET IMEPRODAJNOGMJESTA = ?, ADRESAPRODAJNOGMJESTA = ? WHERE ID = ?");
            preparedStatement.setString(1, ime);
            preparedStatement.setString(2, adresa);
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri ažuriranju prodajnog mjesta!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajPublishera(Long id, String ime, String zemlja, String dodatniInfo) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("UPDATE PUBLISHER SET IMEPUBLISHERA = ?, ZEMLJA = ?, DODATNIINFO = ? WHERE ID = ?");
            preparedStatement.setString(1, ime);
            preparedStatement.setString(2, zemlja);
            preparedStatement.setString(3, dodatniInfo);
            preparedStatement.setLong(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri ažuriranju publishera!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static List<Kategorija> dohvatiKategorijeIgre(Long idIgre) throws BazaPodatakaException{
        List<Kategorija> listaKategorija = new ArrayList<>();
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM IGRA_KATEGORIJA WHERE IGRAID = ?");
            preparedStatement.setLong(1, idIgre);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Long kategorijaId = resultSet.getLong("KATEGORIJAID");
                PreparedStatement noviPreparedStatement = veza.prepareStatement("SELECT * FROM KATEGORIJA WHERE ID = ?");
                noviPreparedStatement.setLong(1, kategorijaId);
                ResultSet noviResultSet = noviPreparedStatement.executeQuery();
                while(noviResultSet.next()){
                    Long id = noviResultSet.getLong("id");
                    String imeKategorije = noviResultSet.getString("imeKategorije");
                    String opisKategorije = noviResultSet.getString("opisKategorije");
                    Kategorija novaKategorija = new Kategorija(id, imeKategorije, opisKategorije);
                    listaKategorija.add(novaKategorija);
                }
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaKategorija;
    }

    public static List<Platforma> dohvatiPlatformeIgre(Long idIgre) throws BazaPodatakaException{
        List<Platforma> listaPlatforma = new ArrayList<>();
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM IGRA_PLATFORMA WHERE IGRAID = ?");
            preparedStatement.setLong(1, idIgre);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Long platformaId = resultSet.getLong("PLATFORMAID");
                PreparedStatement noviPreparedStatement = veza.prepareStatement("SELECT * FROM PLATFORMA WHERE ID = ?");
                noviPreparedStatement.setLong(1, platformaId);
                ResultSet noviResultSet = noviPreparedStatement.executeQuery();
                while(noviResultSet.next()){
                    Long id = noviResultSet.getLong("id");
                    String imePlatforme = noviResultSet.getString("imePlatforme");
                    String urlPlatforme = noviResultSet.getString("urlPlatforme");
                    Platforma novaPlatforma = new Platforma(id, imePlatforme, urlPlatforme);
                    listaPlatforma.add(novaPlatforma);
                }
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaPlatforma;
    }

    public static List<ProdajnoMjesto> dohvatiProdajnaMjestaIgre(Long idIgre) throws BazaPodatakaException{
        List<ProdajnoMjesto> listaProdajnihMjesta = new ArrayList<>();
        try(Connection veza = spajanjeNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM IGRA_PRODAJNOMJESTO WHERE IGRAID = ?");
            preparedStatement.setLong(1, idIgre);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Long prodajnoMjestoId = resultSet.getLong("PRODAJNOMJESTOID");
                PreparedStatement noviPreparedStatement = veza.prepareStatement("SELECT * FROM PRODAJNOMJESTO WHERE ID = ?");
                noviPreparedStatement.setLong(1, prodajnoMjestoId);
                ResultSet noviResultSet = noviPreparedStatement.executeQuery();
                while(noviResultSet.next()){
                    Long id = noviResultSet.getLong("id");
                    String imeProdajnogMjesta = noviResultSet.getString("imeProdajnogMjesta");
                    String adresaProdajnogMjesta = noviResultSet.getString("adresaProdajnogMjesta");
                    ProdajnoMjesto novoProdajnoMjesto = new ProdajnoMjesto(id, imeProdajnogMjesta, adresaProdajnogMjesta);
                    listaProdajnihMjesta.add(novoProdajnoMjesto);
                }
            }
        }catch(SQLException | IOException ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaProdajnihMjesta;
    }

    public static void obrisiKategoriju(Kategorija kategorija) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("DELETE FROM KATEGORIJA WHERE id =" + kategorija.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri brisanju kategorije!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void obrisiIgru(Igra igra) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("DELETE FROM IGRA WHERE id =" + igra.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri brisanju igre!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void obrisiPlatformu(Platforma platforma) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("DELETE FROM PLATFORMA WHERE id =" + platforma.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri brisanju platforme!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void obrisiProdajnoMjesto(ProdajnoMjesto prodajnoMjesto) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("DELETE FROM PRODAJNOMJESTO WHERE id =" + prodajnoMjesto.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri brisanju prodajnog mjesta!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void obrisiPublishera(Publisher publisher) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {

            PreparedStatement preparedStatement = veza
                    .prepareStatement("DELETE FROM PUBLISHER WHERE id =" + publisher.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške pri brisanju publishera!";
            throw new BazaPodatakaException(poruka, ex);
        }
    }
}
