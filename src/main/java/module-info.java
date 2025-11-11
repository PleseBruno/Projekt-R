module hr.fer.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires ejml.simple;
    requires ejml.cdense;

    opens hr.fer.projekt.application to javafx.fxml;
    exports hr.fer.projekt.application;

    opens hr.fer.projekt.controllers to javafx.fxml;
    exports hr.fer.projekt.controllers;
}
