package projekt.entiteti;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public interface RangLista{

    default BigDecimal izracunajProsjekIgre(List<Recenzija> listaRecenzija){
        BigDecimal suma = BigDecimal.valueOf(0);

        for(Recenzija recenzija : listaRecenzija){
            suma = suma.add(BigDecimal.valueOf(recenzija.ocjena()));
        }
        return suma.divide(BigDecimal.valueOf(listaRecenzija.size()))
                   .round(new MathContext(2));
    }

    List<Igra> poredajIgrePoOcjenama(List<Igra> igre);
}
