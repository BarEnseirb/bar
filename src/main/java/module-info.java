module fr.pjdevs.bar {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires com.google.gson;

    opens fr.pjdevs.bar to javafx.fxml, com.google.gson;

    exports fr.pjdevs.bar;
}
