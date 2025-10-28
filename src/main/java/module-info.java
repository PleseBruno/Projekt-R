module t.projektr {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens t.projektr to javafx.fxml;
    opens t.projektr.application to javafx.graphics;
    exports t.projektr;
    exports t.projektr.entities;
    exports t.projektr.temp;
}