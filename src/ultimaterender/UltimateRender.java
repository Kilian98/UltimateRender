/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimaterender;

import static helpers.Actions.saveClose;
import helpers.Constants;
import helpers.Storage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Kilian
 */
public class UltimateRender extends Application {

    /**
     * *
     * Starts the program and opens the main Form
     *
     * @param stage
     * @throws Exception if something wents wrong with the creation of the form
     */
    @Override
    public void start(Stage stage) throws Exception {

        Constants.initConstants();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainForm.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("UltimateRender");
        stage.setOnCloseRequest((WindowEvent event) -> {
            saveClose(event);
        });

        stage.setWidth(800);
        stage.setHeight(550);
        stage.setMinHeight(450);
        stage.setMinWidth(550);
        stage.show();

        Storage.getPathToBlenderExe(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
