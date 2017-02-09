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
import objects.InformationPackage;

/**
 *
 * @author kilian
 */
public class CopyInformation extends ConnectionThread {

    private InformationPackage informationPackage;
    boolean transmitted = false;
    boolean error = false;

    public CopyInformation(String ip, int port, InformationPackage infoPack) {
        super(ip, port);
        this.informationPackage = infoPack;
    }

    public CopyInformation(Socket s) throws NetworkException {
        super(s);
    }

    @Deprecated
    public void sendInformation() {
        try {
            establishClientConnction(Constants.sendInformation);
            sendObject(informationPackage);
        } catch (NetworkException ex) {
        }
    }

    @Deprecated
    public void getInformationClient() {

        try {
            if (!readLine().equals(Constants.sendInformation)) {
                System.err.println("Fatal error while sending Information");
            }
            informationPackage = (InformationPackage) readObject();

            Information.getComputer(informationPackage.id).setFramesRendered(informationPackage.renderedFrames);
            Information.setExtRenderingThreads(informationPackage.renderingThreads - Information.getRenderingThreads());
            Information.setComputersConnected(informationPackage.computersConnected);
            Information.setFramesRemaining(informationPackage.remainingFrames);

        } catch (NetworkException ex) {
        }

    }

    public InformationPackage getInfoPackage() throws NetworkException {

        while (!transmitted) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }

        if (error) {
            throw new NetworkException();
        }

        return informationPackage;

    }

    @Override
    public void run() {

        if (!ipAddress.equals("")) {        //Part foir Client Connection
            try {

                establishClientConnction(Constants.sendInformation);

//                System.out.println("Client sending object");
                sendObject(informationPackage);
//                System.out.println("Client sending succesful");

//                System.out.println("Client reading object");
                informationPackage = (InformationPackage) readObject();
//                System.out.println("Client reading succesful");
                transmitted = true;
                sendLine("");
            } catch (NetworkException ex) {
                transmitted = true;
                error = true;
            }
        } else {

            try {

//                System.out.println("Server reading object");
                InformationPackage tmpPackage = null;
                tmpPackage = (InformationPackage) readObject();
//                System.out.println("Server reading succesful");
                //todo update Information
                Computer comp = null;

                while (true) {
                    comp = Information.getComputer(tmpPackage.id);
                    Thread.sleep(100);
                    if (comp != null) {
                        break;
                    }
                }

                comp.setFramesRendered(tmpPackage.renderedFrames);
                comp.setRenderingThreads(tmpPackage.renderingThreads);

//                System.out.println("Server sending object");
                sendObject(new InformationPackage(
                        Information.getAllRenderedFrames(),
                        Information.getFramesRemaining(),
                        Information.getRenderingThreads(),
                        Information.getComputersConnected()));
//                System.out.println("Server sending succesful");
                readLine();

            } catch (NetworkException | InterruptedException ex) {
            }

        }

        close();
    }

}
