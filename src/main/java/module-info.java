module hr.fer.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens hr.fer.projekt.application to javafx.fxml;
    exports hr.fer.projekt.application;

    opens hr.fer.projekt.controllers to javafx.fxml;
    exports hr.fer.projekt.controllers;
    exports hr.fer.projekt.temp;
    opens hr.fer.projekt.temp to javafx.fxml;
}
