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
