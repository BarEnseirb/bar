/**
 * The module containing the bar application.
 */
module fr.pjdevs.bar {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires com.google.gson;
    
    opens fr.pjdevs.bar.controls to javafx.fxml;
    opens fr.pjdevs.bar.models to com.google.gson;

    exports fr.pjdevs.bar;
}
