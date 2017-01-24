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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author kilian
 */
public class Container_TCPController implements Initializable {

    @FXML
    private TextField tb_Port;
    @FXML
    private TextField tb_ipAdress;
    @FXML
    private Button btn_toggle;
    @FXML
    private RadioButton rb_Server;
    @FXML
    private ToggleGroup one;
    @FXML
    private RadioButton rb_Client;
    @FXML
    private Label lbl_ServerStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void tb_Port_onAction(ActionEvent event) {
        
    }

    @FXML
    private void tb_ipAdress_onAction(ActionEvent event) {
        
    }

    @FXML
    private void rb_onAction(ActionEvent event) {
        
    }
    
}
