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
import helpers.Actions;
import helpers.Constants;
import helpers.Information;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author kilian
 */
public class ServerListenerThread extends ConnectionThread {

    public ServerListenerThread(int port) {
        super("", port);
    }

    @Override
    public void run() {

        ServerSocket sSocket;

        try {
            sSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Actions.showError("An error occured while starting the server", ex);
            return;
        }

        System.out.println("Server started");
        Information.setServerState("Server running...");

        while (!Information.isStopServer()) {

            try {

                Information.setsSocket(sSocket);

                changeSocket(sSocket.accept());

                switch (readLine()) {
                    case Constants.requestBlenderFiles:
                        copyBlenderFiles();
                        break;
                    case Constants.checkForConnection:
                        checkForConnection();
                        break;
                    case Constants.requestNewJob:
                        copyNewJob();
                        break;
                }

            } catch (SocketException e) {
                System.out.println("Server stopped!");
                System.out.println(e.getMessage());
                Information.setServerState("Server stopped");
            } catch (IOException ex) {

            } catch (NetworkException ex) {

            }

        }

    }

    private void changeSocket(Socket s) throws NetworkException {
        try {
            socket = s;
            sIn = socket.getInputStream();
            sOut = socket.getOutputStream();
        } catch (IOException ex) {
            throw new NetworkException();
        }
    }

    private void copyBlenderFiles() {

        try {
            new CopyBlenderFileThread(socket).start();
        } catch (NetworkException ex) {
        }

    }

    private void checkForConnection() {

        try {
            new CheckConnection(socket).start();
        } catch (NetworkException ex) {
        }

    }

    private void copyNewJob() {
        try {
            new CopyRenderTask(socket).start();
        } catch (NetworkException e) {
        }
    }

}
