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
package objects;

import FXMLContainer.Container_InformationController;
import helpers.Information;
import helpers.Storage;
import javafx.application.Platform;

/**
 *
 * @author kilian
 */
public class InformationThread extends Thread {

    @Override
    public void run() {

        Container_InformationController controller = Information.getInfoController();

        while (true) {

            try {
                Thread.sleep(Storage.getSettings().getRefreshMillis());
            } catch (InterruptedException ex) {
                System.out.println("couldn't wait to gather new Information");
            }

            Information.updateInformation();

            Platform.runLater(() -> {

                controller.getLbl_status().setText("Status: " + Information.getStatus().toString());
                controller.getLbl_renderedFrames().setText("Rendered Frames: " + Information.getFramesRendered());
                controller.getLbl_remainingFrames().setText("Remaining Frames: " + Information.getFramesRemaining());

                controller.getLbl_localRenderingThreads().setText("Local rendering threads: " + Information.getRenderingThreads());
                controller.getLbl_localWaitingThreads().setText("Local waiting threads: " + Information.getWaitingThreads());

                controller.getLbl_pcsConnected().setText("Computers connected: " + Information.getComputersConnected());
                controller.getLbl_extRenderingThreads().setText("External rendering threads: " + Information.getExtRenderingThreads());
                controller.getLbl_ConnectionThreads().setText("External Rendering Threads: " + Information.getServerThreads());
            });

        }

    }

}
