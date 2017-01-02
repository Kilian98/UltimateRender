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

import helpers.Information;
import helpers.Storage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;

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
    private Label lbl_MaxUsage;
    @FXML
    private FlowPane vbox_Cards;

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

        initGPUs();

    }

    @FXML
    private void cb_AllowCPU_onAction(ActionEvent event) {
        Storage.getSettings().setAllowCPU(cb_AllowCPU.isSelected());
    }

    private void initGPUs() {

        int cntr = 0;

        for (Graphic_board.Graphicboard g : Storage.getSettings().getGpus()) {

            final CheckBox cb = new CheckBox(g.getDisplayName());
            final int i = cntr;
            vbox_Cards.getChildren().add(cb);
            cb.setSelected(Storage.getSettings().getGpus().get(i).isAllowed());
            FlowPane.setMargin(cb, new Insets(7));

            cb.setOnAction((ActionEvent event) -> {
                Storage.getSettings().getGpus().get(i).setAllowed(cb.isSelected());
            });

            cntr++;
        }
    }

}
