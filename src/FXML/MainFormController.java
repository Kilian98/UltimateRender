/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML;

import Exceptions.ParseException;
import Exceptions.ReadBlenderException;
import Exceptions.UnknownRendererException;
import SmallFXML.Container_blenderSettingsController;
import helpers.Actions;
import static helpers.Actions.saveClose;
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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import objects.BlenderFile;

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

        names = FXCollections.observableArrayList(Storage.getFilesToRender());

        lv_Queue.setItems(names);

    }

    @FXML
    private void btn_pcPause_onAction(ActionEvent event) {

    }

    @FXML
    private void btn_pcAbort_onAction(ActionEvent event) {

    }

    @FXML
    private void btn_netPause_onAction(ActionEvent event) {

    }

    @FXML
    private void btn_netAbort_onAction(ActionEvent event) {

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

    }

    Stage getWindow() {
        return ((Stage) btn_netPause.getScene().getWindow());
    }

    private void lv_Queue_onChange(int newValue) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SmallFXML/Container_blenderSettings.fxml"));
        Parent root = loader.load();

        Container_blenderSettingsController controller = (Container_blenderSettingsController) loader.getController();

        controller.linkWithBlenderFile((BlenderFile) lv_Queue.getSelectionModel().getSelectedItem());

        vbox_mid.getChildren().clear();
        vbox_mid.getChildren().add(root);

    }

    @FXML
    private void btn_pcStart_onAction(ActionEvent event) {
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