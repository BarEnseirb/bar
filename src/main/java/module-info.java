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
    exports fr.pjdevs.bar.controls;
    exports fr.pjdevs.bar.models;
    exports fr.pjdevs.bar.util;
}
