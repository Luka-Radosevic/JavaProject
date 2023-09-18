package projekt.entiteti;

import projekt.sortiranje.IgraSorter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Igra extends Entitet implements RangLista{
    private Integer godinaIzlaska;
    private Publisher publisher;

    private Long publisherId;

    private List<Recenzija> listaRecenzija;
    private BigDecimal ocjena;

    public Igra(IgraBuilder builder) {
        super(builder.id, builder.imeIgre);
        this.godinaIzlaska = builder.godinaIzlaska;
        this.publisher = builder.publisher;
        this.listaRecenzija = builder.listaRecenzija;
        this.ocjena = builder.ocjena;
    }

    public Integer getGodinaIzlaska() {
        return godinaIzlaska;
    }

    public void setGodinaIzlaska(Integer godinaIzlaska) {
        this.godinaIzlaska = godinaIzlaska;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Recenzija> getListaRecenzija() {
        return listaRecenzija;
    }

    public void setListaRecenzija(List<Recenzija> listaRecenzija) {
        this.listaRecenzija = listaRecenzija;
    }

    public BigDecimal getOcjena() {
        return ocjena;
    }

    public void setOcjena(BigDecimal ocjena) {
        this.ocjena = ocjena;
    }

    public static class IgraBuilder{
        private Long id;
        private String imeIgre;
        private Integer godinaIzlaska;

        private Long publisherId;

        private Publisher publisher;
        private List<Recenzija> listaRecenzija;
        private BigDecimal ocjena;

        public IgraBuilder(Long id){
            this.id = id;
        }

        public IgraBuilder newListaRecenzija(List<Recenzija> listaRecenzija){
            this.listaRecenzija = listaRecenzija;
            return this;
        }

        public IgraBuilder newIme(String imeIgre){
            this.imeIgre = imeIgre;
            return this;
        }

        public IgraBuilder newGodina(Integer godinaIzlaska){
            this.godinaIzlaska = godinaIzlaska;
            return this;
        }

        public IgraBuilder newPublisher(Publisher publisher ){
            this.publisher = publisher;
            return this;
        }

        public IgraBuilder newOcjena(BigDecimal ocjena){
            this.ocjena = ocjena;
            return this;
        }

        public Igra build(){
            Igra igra = new Igra(this);
            return igra;
        }
    }

    @Override
    public List<Igra> poredajIgrePoOcjenama(List<Igra> igre) {
        igre = igre.stream().sorted(Comparator.comparing(Igra::getOcjena)).collect(Collectors.toList());
        //pogledat još
        return null;
    }
}
