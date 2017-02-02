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
import helpers.Paths;
import helpers.Storage;
import java.io.File;
import java.net.Socket;
import objects.BlenderFile;

/**
 *
 * @author kilian
 */
public class CopyBlenderFileThread extends ConnectionThread {

    File fileToTransfer = null; //only set for the Server
    private Object blenderFile;
    long blenderFileId;

    public CopyBlenderFileThread(String ipAddress, int port, long fileID) {
        super(ipAddress, port);
        blenderFileId = fileID;
    }

    public CopyBlenderFileThread(Socket s) throws NetworkException {
        super(s);
    }

    @Override
    public void run() {

        if (!ipAddress.equals("")) {     //Part foir Client Connection

            try {

                establishClientConnction(Constants.requestBlenderFiles); //sends a line as well
                sendLine(blenderFileId + "");

                BlenderFile file = (BlenderFile) readObject();
                Information.getLocalComputer().addFilesToRenderLocal(file);

                File destination = new File(Paths.getWorkingDir() + File.separator + file.getId() + ".blend"); //todo
                readFile(destination);

                System.out.println("Transferred File: " + destination.toString() + " from Server");

            } catch (NetworkException ex) {
            }

        } else {    //part for the server

            try {

                try {
                    blenderFileId = Long.parseLong(readLine());
                    System.out.println("Blender File ID: " + blenderFileId);
                } catch (NetworkException | NumberFormatException ex) {
                    System.err.println("error reading id: WARNING");
                }

                for (BlenderFile f : Storage.getFilesToRender()) {
                    if (f.getId() == blenderFileId) {
                        blenderFile = f;
                        fileToTransfer = f.getPath();
                        System.out.println(blenderFile);
                        break;
                    }
                }

                sendObject(blenderFile);
                sendFile(fileToTransfer);
                System.out.println("Transfered File: " + fileToTransfer.toString() + " to Client");
            } catch (NetworkException ex) {
            }

        }

    }

}
