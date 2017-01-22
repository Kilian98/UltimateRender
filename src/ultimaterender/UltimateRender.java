/* 
 * Copyright (C) 2017 kilian
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

        stage.setMinHeight(450);
        stage.setMinWidth(550);
        stage.setResizable(true);
        stage.show();

        Storage.getPathToBlenderExe(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
