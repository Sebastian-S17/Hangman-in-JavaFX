module app.application {
    requires javafx.controls;
    requires javafx.fxml;
            
            requires com.dlsc.formsfx;
                    requires org.kordamp.bootstrapfx.core;
            
    opens app.application to javafx.fxml;
    exports app.application;
}