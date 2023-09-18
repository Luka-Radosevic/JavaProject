module projekt.zavrsniprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires commons.codec;


    opens projekt.zavrsniprojekt to javafx.fxml;
    exports projekt.zavrsniprojekt;
}