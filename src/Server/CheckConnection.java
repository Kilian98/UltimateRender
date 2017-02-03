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
package Server;

import Exceptions.NetworkException;
import helpers.Constants;
import helpers.Information;
import java.net.Socket;
import Server.Computer.ComputerType;

/**
 *
 * @author kilian
 */
public class CheckConnection extends ConnectionThread {

    boolean connected = false;

    public CheckConnection(Socket s) throws NetworkException {
        super(s);
    }
    
    public CheckConnection(String ipAddress, int port){
        super(ipAddress, port);
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void run() {

        try {

            if (!ipAddress.equals("")) {     //Part foir Client Connection

                establishClientConnction(Constants.checkForConnection);
                Information.setLocalComputer((Computer) readObject());
                
                Information.getLocalComputer().setSocket(socket);


                connected = true;
                Information.setServerState("Connected");

                if (readLine() == null) {
                    System.err.println("The connection shut down (Error)");
                    Information.setServerState("Server Connection Lost");
                }

            } else {

                Computer pc = new Computer(ComputerType.client);
                sendObject(pc);
                Information.addComputerToList(pc);

                if (readLine() == null) {
                    System.err.println("The connection shut down (Error)");
//                    Actions.showAlert("Connection Lost", "A Client has disconnected", "Disconnection");
                }
                
                Information.removeComputerFromList(pc);

            }

        } catch (NetworkException e) {
            System.err.println("Error or Shutdown!");
            Information.setServerState("Shutdown");
        }finally{
            close();
        }

    }

}
