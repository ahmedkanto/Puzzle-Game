module mygame.labyrinthpuzzle {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.apache.logging.log4j;
    requires homework.project.utils;

    opens mygame to javafx.fxml, com.fasterxml.jackson.databind;
    exports mygame;
    exports modelBoard;
    opens modelBoard to com.fasterxml.jackson.databind, javafx.fxml;
}
