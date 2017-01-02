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
package FXMLContainer;

import Exceptions.ReadBlenderException;
import helpers.Actions;
import static helpers.Actions.parseInt;
import helpers.Constants;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import objects.BlenderFile;

/**
 * FXML Controller class
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class Container_blenderSettingsController implements Initializable {

    @FXML
    private CheckBox cb_AdjustRenderSettings;
    @FXML
    private TextField tb_startFrame;
    @FXML
    private TextField tb_EndFrame;
    @FXML
    private TextField tb_destination;
    @FXML
    private ChoiceBox<?> cb_imageFormat;

    private BlenderFile blenderFile;
    boolean linked = false; //Last check if Container and BlenderFile are linked
    private final String numbers = "0123456789";
    @FXML
    private Label lbl_Renderer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        List<String> liste = new ArrayList<>();

        liste.add("PNG");
        liste.add("JPEG");
        liste.add("BMP");
        liste.add("TIFF");

        ObservableList ls = FXCollections.observableArrayList(liste);

        cb_imageFormat.setItems(ls);

        cb_imageFormat.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            cb_imageFormat_onChange(oldValue.intValue(), newValue.intValue());
        });

        tb_startFrame.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tb_startFrame_onChange(oldValue, newValue);
        });

        tb_EndFrame.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tb_EndFrame_onChange(oldValue, newValue);
        });

        tb_destination.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tb_destination_onChange(oldValue, newValue);
        });

    }

    public void linkWithBlenderFile(BlenderFile file) {

        if (file != null) {
            this.blenderFile = file;
            linked = true;

            tb_startFrame.setText(file.getStartFrame() + "");
            tb_EndFrame.setText(file.getEndFrame() + "");
            tb_destination.setText(file.getPathToRender() + "");
            cb_imageFormat.getSelectionModel().select(Constants.ImageFormatToIndex.get(file.getFileFormat()));

            lbl_Renderer.setText("Renderer: " + file.getRenderer().toString());

            cb_AdjustRenderSettings.setSelected(file.isAdjustedSettings());
            cb_AdjustRenderSettings_onAction(null);
        }

    }

    @FXML
    private void cb_AdjustRenderSettings_onAction(ActionEvent event) {

        tb_startFrame.setDisable(!cb_AdjustRenderSettings.isSelected());
        tb_EndFrame.setDisable(!cb_AdjustRenderSettings.isSelected());
        tb_destination.setDisable(!cb_AdjustRenderSettings.isSelected());
        cb_imageFormat.setDisable(!cb_AdjustRenderSettings.isSelected());

        if (linked) {
            blenderFile.setAdjustedSettings(cb_AdjustRenderSettings.isSelected());
        } else {
            Actions.showError("File and Controller are not connected. Please notify the developer", new ReadBlenderException());
        }

    }

    public BlenderFile getBlenderFile() {
        return blenderFile;
    }

    public void setBlenderFile(BlenderFile blenderFile) {
        this.blenderFile = blenderFile;
    }

    private boolean isOnlyNumbers(String str) {

        for (char s : str.toCharArray()) {

            if (!numbers.contains(s + "")) {
                return false;
            }

        }

        return true;

    }

    private void cb_imageFormat_onChange(int oldValue, int newValue) {

        blenderFile.setFileFormat(Constants.IndexToImageFormat.get(newValue));

    }

    private void tb_startFrame_onChange(String oldValue, String newValue) {
        if (!isOnlyNumbers(newValue)) {
            tb_startFrame.setText(oldValue);
        } else {
            blenderFile.setStartFrame(parseInt(newValue));
        }
    }

    private void tb_EndFrame_onChange(String oldValue, String newValue) {
        if (!isOnlyNumbers(newValue)) {
            tb_EndFrame.setText(oldValue);
        } else {
            blenderFile.setEndFrame(parseInt(newValue));
        }
    }

    private void tb_destination_onChange(String oldValue, String newValue) {
        blenderFile.setPathToRender(new File(newValue));
    }

}
