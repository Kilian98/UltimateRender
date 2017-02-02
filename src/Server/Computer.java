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

import static helpers.Actions.closeStream;
import helpers.Information;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;
import objects.BlenderFile;

/**
 *
 * @author kilian
 */
public class Computer implements Serializable {

    private final ComputerType type;
    private final int id;
    private final LinkedList<BlenderFile> filesToRenderLocal = new LinkedList<>();
    private Socket socket;
    private int port;
    private String ipAddress = "";
    private LinkedList<Socket> allSockets = new LinkedList<>(); //only for clients

    private static final Object synchronizer = new Object();

    public void close() {
        for (Socket s : allSockets){
            closeStream(s);
        }
        closeStream(socket);
    }

    /**
     * Gives the connection-check socket to the computer. IP-Address and Port
     * will set to 'Computer' automatically.
     *
     * @param s The socket to overgive
     */
    public void setSocket(Socket s) {
        this.socket = s;
        this.port = s.getPort();
        if (type == ComputerType.client) {
            this.ipAddress = s.getInetAddress().getHostAddress();
        }
    }

    public enum ComputerType {
        client,
        server
    }

    public Computer(ComputerType type) {
        id = Information.getNextComputerId();
        this.type = type;
    }

    public LinkedList<BlenderFile> getFilesToRenderLocal() {
        return filesToRenderLocal;
    }

    public void addFilesToRenderLocal(BlenderFile file) {
        filesToRenderLocal.add(file);
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LinkedList<Socket> getAllSockets() {
        return allSockets;
    }

    public static void addSocketToList(Socket s) {
        synchronized (synchronizer) {
            if (Information.getLocalComputer() != null) {
                Information.getLocalComputer().getAllSockets().add(s);
            }
        }
    }

    public static void removeSocket(Socket s) {
        synchronized (synchronizer) {
            if (Information.getLocalComputer() != null) {
                Information.getLocalComputer().getAllSockets().remove(s);
            }
        }
    }

}
