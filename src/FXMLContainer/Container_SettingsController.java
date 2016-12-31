/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXMLContainer;

import helpers.Information;
import helpers.Storage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

/**
 * FXML Controller class
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class Container_SettingsController implements Initializable {

    @FXML
    private CheckBox cb_AllowCPU;
    @FXML
    private Slider slider_MaxPerformance;
    @FXML
    private CheckBox cb_AllowGPU;
    @FXML
    private Label lbl_MaxUsage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        slider_MaxPerformance.setMax(Information.getMaxCpuCernels());
        slider_MaxPerformance.setValue(Storage.getSettings().getSliderState());
        lbl_MaxUsage.setText("Maximum CPU Usage: " + Math.round((double) slider_MaxPerformance.getValue() / Information.getMaxCpuCernels() * 100) + "%");
        
        slider_MaxPerformance.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            lbl_MaxUsage.setText("Maximum CPU Usage: " + Math.round((double) newValue.intValue() / Information.getMaxCpuCernels() * 100) + "%");
            Storage.getSettings().setSliderState(newValue.intValue());
        });
        
        cb_AllowCPU.setSelected(Storage.getSettings().isAllowCPU());
        cb_AllowGPU.setSelected(Storage.getSettings().isAllowGPU());

    }

    @FXML
    private void cb_AllowCPU_onAction(ActionEvent event) {
        Storage.getSettings().setAllowCPU(cb_AllowCPU.isSelected());
    }

    @FXML
    private void cb_AllowGPU_onAction(ActionEvent event) {
        Storage.getSettings().setAllowGPU(cb_AllowGPU.isSelected());
    }

}
