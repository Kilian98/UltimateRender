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
import FXMLContainer.Container_InformationController;
import FXMLContainer.Container_TCPController;
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
import objects.InformationThread;
import objects.RenderThread;

/**
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class MainFormController implements Initializable {

    boolean containsBlenderContainer = false;

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
    @FXML
    private MenuItem mi_toggleTCP;

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

        names = FXCollections.observableArrayList(Storage.getFilesToRender());

        lv_Queue.setItems(names);

        refreshListView();

        try {
            initContainer();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

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

    public void addBlenderFiles(List<File> list) {

        for (File f : list) {

            try {
                if (!Actions.checkForExistingBlenderFile(f)) {
                    Storage.addFileToRender(new BlenderFile(f, getWindow()));
                } else {
                    Actions.showAlert("File already in render queue", "The file is already in your render queue, it will not be added again", f.toString());
                }
            } catch (ReadBlenderException | IOException | InterruptedException | UnknownRendererException | ParseException ex) {
                Actions.showError("An error occured while trying to read a Blender File (.blend)", ex);
            }

        }

        refreshListView();

    }

    private void refreshListView() {

        int selection = lv_Queue.getSelectionModel().getSelectedIndex();

        names.setAll(Storage.getFilesToRender());

        lv_Queue.getSelectionModel().select(selection);

    }

    Stage getWindow() {
        return ((Stage) btn_netPause.getScene().getWindow());
    }

    private void lv_Queue_onChange(int newValue) throws IOException {

//        System.out.println(containsBlenderContainer);
        if (containsBlenderContainer) {
            vbox_mid.getChildren().remove(3);
            vbox_mid.getChildren().remove(2);
        }

        if (newValue == -1) {
            containsBlenderContainer = false;
            return;
        }

        containsBlenderContainer = true;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLContainer/Container_blenderSettings.fxml"));
        Parent root_blenderSettings = loader.load();
        Container_blenderSettingsController controller_blenderSettings = (Container_blenderSettingsController) loader.getController();
        controller_blenderSettings.linkWithBlenderFile((BlenderFile) lv_Queue.getSelectionModel().getSelectedItem());

//        System.out.println(vbox_mid.getChildren().size());
        vbox_mid.getChildren().add(2, new Separator(Orientation.HORIZONTAL));
        vbox_mid.getChildren().add(3, root_blenderSettings);

    }

    private void initContainer() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLContainer/Container_Settings.fxml"));
        Parent root_generalSettings = loader.load();

        loader = new FXMLLoader(getClass().getResource("/FXMLContainer/Container_Information.fxml"));
        Parent root_Information = loader.load();
        Container_InformationController infoController = (Container_InformationController) loader.getController();
        Information.setInfoController(infoController);

        loader = new FXMLLoader(getClass().getResource("/FXMLContainer/Container_TCP.fxml"));
        Parent root_TCP = loader.load();
        Container_TCPController tcpController = (Container_TCPController) loader.getController();
        Information.setTCPController(tcpController);

        vbox_mid.getChildren().add(new Separator(Orientation.HORIZONTAL));
        vbox_mid.getChildren().add(root_generalSettings);
        vbox_mid.getChildren().add(new Separator(Orientation.HORIZONTAL));
        if (Storage.getSettings().isShowTCP()) {
            vbox_mid.getChildren().add(root_TCP);
            vbox_mid.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }
        vbox_mid.getChildren().add(root_Information);
        vbox_mid.getChildren().add(new Separator(Orientation.HORIZONTAL));

        new InformationThread().start();
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

        Information.setStopRendering(false);
        System.out.println("starting rendering...");

        RenderThread[] processes = new RenderThread[Storage.getSettings().getMaxInstancesPerDevice()
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

        Information.setStatus(Information.Status.Rendering);

    }

    @FXML
    private void btn_pcPause_onAction(ActionEvent event) {
        Information.setStopRendering(true);
        Information.setStatus(Information.Status.Pausing);
    }

    @FXML
    private void btn_pcAbort_onAction(ActionEvent event) {

        Information.abortRendering();
        Information.setStatus(Information.Status.Stopped);

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

    @FXML
    private void mi_toggleTCP_onAction(ActionEvent event) throws IOException {

        if (Storage.getSettings().isShowTCP()) {

            Storage.getSettings().setShowTCP(false);
            vbox_mid.getChildren().remove(vbox_mid.getChildren().size() - 3);
            vbox_mid.getChildren().remove(vbox_mid.getChildren().size() - 3);

        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLContainer/Container_TCP.fxml"));
            Parent root_TCP = loader.load();
            vbox_mid.getChildren().add(vbox_mid.getChildren().size() - 2, root_TCP);
            vbox_mid.getChildren().add(vbox_mid.getChildren().size() - 2, new Separator(Orientation.HORIZONTAL));
            Storage.getSettings().setShowTCP(true);
        }

    }

}
