module app {
    requires javafx.controls;
    requires javafx.fxml;

    requires transitive javafx.graphics;

    opens app.tech.jimothy.initracker to javafx.fxml;
    exports app.tech.jimothy.initracker;
}
