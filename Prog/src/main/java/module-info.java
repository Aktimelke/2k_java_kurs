module com.example.prog {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.desktop;


    opens com.example.prog to javafx.fxml;
    exports com.example.prog;
    exports com.example.prog.limitedFields;
    opens com.example.prog.limitedFields to javafx.fxml;
    exports com.example.prog.Models;
    opens com.example.prog.Models to javafx.fxml;
}