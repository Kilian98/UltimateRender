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

import static helpers.Actions.isOnlyNumbers;
import static helpers.Actions.parseInt;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
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

    TCPState tCPState = TCPState.Server;

    private void tb_Port_onChange(String oldValue, String newValue) {

        if (!isOnlyNumbers(newValue) || parseInt(newValue) >= 65535) {
            tb_Port.setText(oldValue);
        }

    }

    public enum TCPState {
        Server,
        ServerRunning,
        Client,
        ClientRunning
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tb_ipAdress.setText(getIPAdress());
        tb_Port.setText("1235");

        tb_Port.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tb_Port_onChange(oldValue, newValue);
        });
    }

    @FXML
    private void rb_onAction(ActionEvent event) {

        if (rb_Server.isSelected()) {
            tb_ipAdress.setDisable(true);
            tb_ipAdress.setText(getIPAdress());
            tCPState = TCPState.Server;
            stopServer();
        } else if (rb_Client.isSelected()) {
            tb_ipAdress.setDisable(false);
            tCPState = TCPState.Client;
            stopClient();
        }

    }

    @FXML
    private void btn_toggle_onAction(ActionEvent event) {

        switch (tCPState) {
            case Server:
                startServer();
                break;
            case ServerRunning:
                stopServer();
                break;
            case Client:
                startClient();
                break;
            case ClientRunning:
                stopClient();
                break;
        }

    }

    private void setTCPState(TCPState state) {

        tCPState = state;

        rb_Client.setDisable(state == TCPState.ClientRunning || state == TCPState.ServerRunning);
        rb_Server.setDisable(state == TCPState.ClientRunning || state == TCPState.ServerRunning);

    }

    private void stopClient() {
        btn_toggle.setText("Connect to server");

        setTCPState(TCPState.Client);
    }

    private void startClient() {
        btn_toggle.setText("Stop connection");

        setTCPState(TCPState.ClientRunning);
    }

    private void stopServer() {
        btn_toggle.setText("Start server");

        setTCPState(TCPState.Server);
    }

    private void startServer() {
        btn_toggle.setText("Stop server");

        setTCPState(TCPState.ServerRunning);
    }

    /**
     * Get the Host IP-Adress of your Computer
     *
     * @return the IP-Adress that starts with 192
     */
    private String getIPAdress() {
        try {
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements();) {
                NetworkInterface e = n.nextElement();

                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements();) {
                    InetAddress addr = a.nextElement();

                    if (addr.getHostAddress().startsWith("192")) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            return "<Local IP not found>";
        }

        return "<Local IP not found>";
    }

}
