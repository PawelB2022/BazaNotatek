module BazaNotatek
{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence; // Requires Java Persistence API (JPA)
    requires java.sql;

    requires org.hibernate.orm.core; // Requires Hibernate

    opens controllers to javafx.fxml; // Open your package for FXML
    exports controllers; // Export your package

    // If using Hibernate, open the packages for Hibernate entities and configuration
    opens entities to org.hibernate.orm.core;
    exports entities;
}