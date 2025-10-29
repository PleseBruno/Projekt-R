module hr.fer.projekt {
    requires javafx.controls;
    requires javafx.fxml;

    opens hr.fer.projekt.application to javafx.fxml;
    exports hr.fer.projekt.application;

    opens hr.fer.projekt.controllers to javafx.fxml;
    exports hr.fer.projekt.controllers;
}
