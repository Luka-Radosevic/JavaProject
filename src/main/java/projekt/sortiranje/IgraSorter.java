package projekt.sortiranje;

import projekt.entiteti.Igra;

import java.util.Comparator;

public class IgraSorter implements Comparator<Igra> {
    @Override
    public int compare(Igra i1, Igra i2) {
        if(i1.getOcjena().compareTo(i2.getOcjena()) > 0){
            return 1;
        }
        else if(i1.getOcjena().compareTo(i2.getOcjena()) < 0){
            return -1;
        }
        else{
            return 0;
        }
    }
}
