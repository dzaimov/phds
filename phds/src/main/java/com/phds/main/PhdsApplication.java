package com.phds.main;

import com.phds.common.SpringApplicationContext;
import com.phds.phd.service.PhdService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.phds")
public class PhdsApplication extends Application {

    @Autowired
    PhdService phdService;

    private ConfigurableApplicationContext springContext;
    private Parent root;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(PhdsApplication.class);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ui/main.fxml"));
        fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
        root = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ДокторантиТЕ");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        Platform.runLater( () -> root.requestFocus() );
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
    }


    public static void main(String[] args) {
        launch(PhdsApplication.class, args);
    }

}
