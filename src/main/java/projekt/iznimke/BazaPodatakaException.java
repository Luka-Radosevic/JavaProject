package projekt.iznimke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projekt.zavrsniprojekt.MainScreen;

public class BazaPodatakaException extends Exception{

    public BazaPodatakaException(){}

    public BazaPodatakaException(String message){
        super(message);
        MainScreen.logger.error(message);
    }

    public BazaPodatakaException(String message, Throwable cause){
        super(message, cause);
        MainScreen.logger.error(message);
    }

    public BazaPodatakaException(Throwable cause){super(cause);}
}
