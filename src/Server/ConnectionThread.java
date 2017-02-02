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
import static helpers.Actions.closeStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author kilian
 */
public abstract class ConnectionThread extends Thread {

    public Socket socket;
    public InputStream sIn;
    public OutputStream sOut;

    String ipAddress = "";
    int port = 0;

    public ConnectionThread(String ip, int port) {
        this.ipAddress = ip;
        this.port = port;
    }
    
    public ConnectionThread(Socket s) throws NetworkException {

        try {
            this.socket = s;
            sIn = socket.getInputStream();
            sOut = socket.getOutputStream();
        } catch (IOException ex) {
            throw new NetworkException();
        }

    }

    public void close(){
        Computer.removeSocket(socket);
        Actions.closeStream(socket);
    }
    
    public void establishClientConnction(String messageToServer) throws NetworkException {

        try {
            socket = new Socket(ipAddress, port);
            sIn = socket.getInputStream();
            sOut = socket.getOutputStream();

            sendLine(messageToServer);

        } catch (IOException ex) {
            throw new NetworkException();
        }

        Computer.addSocketToList(socket);
        System.out.println("Established Connection");

    }

    @Deprecated
    public void establishServerConnection() throws NetworkException {

        if (!ipAddress.equals("")){
            throw new NetworkException(); //if the ip is set, it is a Client and shall open up a Server
        }
        
        try {
            ServerSocket sSocket = new ServerSocket(port);
            socket = sSocket.accept();

            sIn = socket.getInputStream();
            sOut = socket.getOutputStream();

        } catch (IOException ex) {
            throw new NetworkException();
        }

    }
    
    public void sendLine(String line) {

        PrintWriter out = new PrintWriter(sOut, true);
        out.println(line);

    }

    public String readLine() throws NetworkException {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sIn));
            return reader.readLine();
        } catch (IOException ex) {
            throw new NetworkException();
        }

    }

    public void sendObject(Object obj) throws NetworkException {

        try {
            ObjectOutputStream oOut = new ObjectOutputStream(sOut);
            oOut.writeObject(obj);
        } catch (IOException ex) {
            throw new NetworkException();
        }

    }

    public Object readObject() throws NetworkException {
        try {
            ObjectInputStream oInput = new ObjectInputStream(sIn);
            return oInput.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new NetworkException();
        }
    }

    public void sendFile(File f) throws NetworkException {

        try {
            byte[] buffer = new byte[4096];
            InputStream inputStream = new FileInputStream(f);
            int len = 0;

            while ((len = inputStream.read(buffer)) > 0) {
                sOut.write(buffer, 0, len);
            }

        } catch (IOException ex) {
            throw new NetworkException();
        } finally {
            closeStream(sOut);
        }
    }

    

    public void readFile(File outputFile) throws NetworkException {

        try {

            OutputStream outputStream = new FileOutputStream(outputFile);
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[4096];
            int len = 0;

            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }


        } catch (IOException ex) {
            throw new NetworkException();
        } finally {
            closeStream(socket);
        }
    }

    @Override
    public abstract void run();

}
