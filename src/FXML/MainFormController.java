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
package FXML;

import Exceptions.ParseException;
import Exceptions.ReadBlenderException;
import Exceptions.UnknownRendererException;
import FXMLContainer.Container_blenderSettingsController;
import Graphic_board.Graphicboard;
import helpers.Actions;
import static helpers.Actions.saveClose;
import helpers.Information;
import helpers.Storage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import objects.BlenderFile;
import objects.RenderThread;

/**
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class MainFormController implements Initializable {

    private Label label;
    @FXML
    private MenuBar MenuBar;
    @FXML
    private MenuItem mi_Close;
    @FXML
    private ListView<?> lv_Queue;
    @FXML
    private Button btn_pcPause;
    @FXML
    private Button btn_pcAbort;
    @FXML
    private Button btn_netPause;
    @FXML
    private Button btn_netAbort;
    @FXML
    private MenuItem mi_fullScreen;
    @FXML
    private MenuItem mi_AddFile;

    static ObservableList names;
    @FXML
    private VBox vbox_mid;
    @FXML
    private Button btn_pcStart;
    @FXML
    private Button btn_netStart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        lv_Queue.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try {
                lv_Queue_onChange(newValue.intValue());
            } catch (IOException ex) {
                Actions.showError("Error with reading a BlendFile (Object)", ex);
            }
        });

        Storage.loadSettings();

        try {
            lv_Queue_onChange(-1);
        } catch (IOException ex) {
        }

        names = FXCollections.observableArrayList(Storage.getFilesToRender());

        lv_Queue.setItems(names);

    }

    @FXML
    private void mi_fullScreen_onAction(ActionEvent event) {
        getWindow().setFullScreen(true);
    }

    @FXML
    private void mi_Close_onAction(ActionEvent event) {

        saveClose(null);

    }

    @FXML
    private void mi_AddFile_onAction(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Blender Files", "*.blend*"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        fc.setTitle("Select file(s) you want to render");
//        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Blender Files", ".blend"));

        List<File> files = fc.showOpenMultipleDialog(getWindow());

        if (files != null) {
            addBlenderFiles(files);
        }

    }

    public static void refreshLVSelction() {

    }

    public void addBlenderFiles(List<File> list) {

        for (File f : list) {

            try {
                Storage.addFileToRender(new BlenderFile(f, getWindow()));
            } catch (ReadBlenderException | IOException | InterruptedException | UnknownRendererException | ParseException ex) {
                Actions.showError("An error occured while trying to read a Blender File (.blend)", ex);
            }

        }

        refreshListView();

    }

    private void refreshListView() {
        names.setAll(Storage.getFilesToRender());
        vbox_mid.getChildren().clear();
        try {
            lv_Queue_onChange(-1);
        } catch (IOException ex) {
        }

    }

    Stage getWindow() {
        return ((Stage) btn_netPause.getScene().getWindow());
    }

    private void lv_Queue_onChange(int newValue) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLContainer/Container_blenderSettings.fxml"));
        Parent root_blenderSettings = loader.load();
        Container_blenderSettingsController controller_blenderSettings = (Container_blenderSettingsController) loader.getController();
        controller_blenderSettings.linkWithBlenderFile((BlenderFile) lv_Queue.getSelectionModel().getSelectedItem());

        loader = new FXMLLoader(getClass().getResource("/FXMLContainer/Container_Settings.fxml"));
        Parent root_generalSettings = loader.load();

        vbox_mid.getChildren().clear();
        vbox_mid.getChildren().add(root_generalSettings);

        if (newValue != -1) {
            vbox_mid.getChildren().add(new Separator(Orientation.HORIZONTAL));
            vbox_mid.getChildren().add(root_blenderSettings);
            vbox_mid.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }
    }

    @FXML
    private void btn_pcStart_onAction(ActionEvent event) {

        if (!Storage.getSettings().isAllowCPU() && !Storage.getSettings().gpuAllowed()) {
            Actions.showAlert("No rendering device", "Please select at least one rendering devive", "You cannot render without a rendering device");
            return;
        }

        if (!Storage.getSettings().gpuAllowed() && Storage.getSettings().getSliderState() == 0) {
            Actions.showAlert("No rendering device", "Please select a CPU usage value over 0% or an other rendering device", "You cannot render with a "
                    + "usage of 0% and no other device");
            return;
        }

        System.out.println("starting rendering...");

        Thread[] processes = new Thread[Storage.getSettings().getMaxInstancesPerDevice()
                + Storage.getSettings().getGpus().size() * Storage.getSettings().getMaxInstancesPerDevice()];
        Information.setThreads(processes);

        int i = 0; //index for array

        //Create CPU Threads
        for (; i < Storage.getSettings().getMaxInstancesPerDevice(); i++) {
            processes[i] = new RenderThread(null, getWindow());
            processes[i].start();
        }

        //Create GPU Threads
        for (Graphicboard b : Storage.getSettings().getGpus()) {
            for (; i < Storage.getSettings().getMaxInstancesPerDevice() * (1 + Storage.getSettings().getGpus().size()); i++) {
                processes[i] = new RenderThread(b, getWindow());
                processes[i].start();
            }
        }

    }

    @FXML
    private void btn_pcPause_onAction(ActionEvent event) {
        Information.setStopRendering(true);
    }

    @FXML
    private void btn_pcAbort_onAction(ActionEvent event) {

        Information.abortRendering();

    }

    @FXML
    private void btn_netPause_onAction(ActionEvent event) {

    }

    @FXML
    private void btn_netAbort_onAction(ActionEvent event) {

    }

    @FXML
    private void btn_netStart_onAction(ActionEvent event) {
    }

    @FXML
    private void lv_Queue_onKeyTyped(KeyEvent event) {

        if (event.getCode().equals(KeyCode.DELETE)) {
            deleteSelected();
        }

    }

    private void deleteSelected() {
        int index = lv_Queue.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            Storage.getFilesToRender().remove(index);

            refreshListView();

            if (index < Storage.getFilesToRender().size()) {
                lv_Queue.getSelectionModel().select(index);
            } else if (!Storage.getFilesToRender().isEmpty()) {
                lv_Queue.getSelectionModel().select(index - 1);
            }
        }
    }

}
