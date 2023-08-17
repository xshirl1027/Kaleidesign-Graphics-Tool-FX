module com.example.kaleideigngraphicstoolfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.kaleideigngraphicstoolfx to javafx.fxml;
    exports com.kaleideigngraphicstoolfx;
}