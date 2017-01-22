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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author kilian
 */
public class Container_InformationController implements Initializable {

    @FXML
    private Label lbl_status;
    @FXML
    private Label lbl_renderedFrames;
    @FXML
    private Label lbl_localRenderingThreads;
    @FXML
    private Label lbl_localWaitingThreads;
    @FXML
    private Label lbl_pcsConnected;
    @FXML
    private Label lbl_extRenderingThreads;
    @FXML
    private Label lbl_ConnectionThreads;
    @FXML
    private Label lbl_remainingFrames;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public Label getLbl_status() {
        return lbl_status;
    }

    public Label getLbl_renderedFrames() {
        return lbl_renderedFrames;
    }

    public Label getLbl_localRenderingThreads() {
        return lbl_localRenderingThreads;
    }

    public Label getLbl_localWaitingThreads() {
        return lbl_localWaitingThreads;
    }

    public Label getLbl_pcsConnected() {
        return lbl_pcsConnected;
    }

    public Label getLbl_extRenderingThreads() {
        return lbl_extRenderingThreads;
    }

    public Label getLbl_ConnectionThreads() {
        return lbl_ConnectionThreads;
    }

    public Label getLbl_remainingFrames() {
        return lbl_remainingFrames;
    }

}
