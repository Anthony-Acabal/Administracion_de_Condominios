module condominio.proyecto_condominio {

    requires javafx.controls;
    requires javafx.fxml;

    requires jasperreports;

    requires java.sql;
    requires java.desktop;

    requires java.xml;
    requires java.logging;
    requires java.compiler;
    
    requires jakarta.mail;
    requires jakarta.activation;

    opens condominio.proyecto_condominio
            to javafx.fxml;

    opens condominio.proyecto_condominio.logic
            to javafx.fxml;

    opens condominio.proyecto_condominio.model
            to javafx.base;

    opens condominio.proyecto_condominio.controller
            to javafx.fxml;

    exports condominio.proyecto_condominio;
}