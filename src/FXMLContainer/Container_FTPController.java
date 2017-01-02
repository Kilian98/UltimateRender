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
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class Container_FTPController implements Initializable {

    @FXML
    private CheckBox cb_useServer;
    @FXML
    private TextField tb_ServerPath;
    @FXML
    private TextField tb_ServerUsername;
    @FXML
    private PasswordField tb_ServerPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void cb_useServer_onAction(ActionEvent event) {
        
        tb_ServerPath.setDisable(!cb_useServer.isSelected());
        tb_ServerUsername.setDisable(!cb_useServer.isSelected());
        tb_ServerPassword.setDisable(!cb_useServer.isSelected());
        
    }
    
}
