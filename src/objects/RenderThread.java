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

import Graphic_board.Graphicboard;
import helpers.FileHelpers;
import helpers.Information;
import helpers.Storage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Window;

/**
 *
 * @author Kilian visit me on <aklio.de>
 */
public class RenderThread extends Thread {

    private Graphicboard board;
    private final Window window;

    public RenderThread(Graphicboard board, Window window) {
        this.board = board;
        this.window = window;
    }

    public Graphicboard getBoard() {
        return board;
    }

    public void setBoard(Graphicboard board) {
        this.board = board;
    }

    @Override
    public void run() {

        while (!Information.isStopRendering() && !Storage.getRenderTasks().isEmpty()) {

            if (board == null) {
                if (!Storage.getSettings().isAllowCPU()) {
                    try {
                        Thread.sleep(1000);
                        continue;
                    } catch (InterruptedException ex) {
                    }
                }
            } else if (!board.isAllowed()) {
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException ex) {
                }
            }

            RenderTask task;

            synchronized (Information.getSynchronizer()) {
                if (!Storage.getRenderTasks().isEmpty()) {
                    task = Storage.getRenderTasks().removeFirst();
                } else {
                    break;
                }
            }

            if (task.getFrame() > task.getFile().getEndFrame() || task.getFrame() < task.getFile().getStartFrame()) {
                System.out.println("Skipped Frame: " + task.getFrame());
                continue;
            }

            String pythonContent = "";

            if (board != null) {
                pythonContent = "import bpy\n"
                        + "bpy.context.user_preferences.system.compute_device_type = \"" + board.getType() + "\"\n"
                        + "bpy.context.scene.cycles.device = \"GPU\"\n"
                        + "bpy.context.user_preferences.system.compute_device = \"" + board.getSystemName() + "\"";
            }

            File pythonFile = new File(task.getFile().getPath().getParent() + "/" + task.getFile().getPath().getName().split("\\.")[0] + task.getFrame() + ".py");
            FileHelpers.writeFile(pythonFile, pythonContent);

            ProcessBuilder pb = new ProcessBuilder(
                    Storage.getSettings().getPathToBlenderExe(window).toString(),
                    "-b",
                    task.getFile().getPath().toString(),
                    "-o",
                    task.getFile().getPathToRender().toString(),
                    "-F",
                    task.getFile().getFileFormat(),
                    "--python",
                    pythonFile.toString(),
                    "-f",
                    task.getFrame() + "");

            pb.redirectErrorStream(true);

            pb.directory(new File("/home/"));

            try {
                Process p = pb.start();

//                p.waitFor();
                BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));

                while (bri.readLine() != null) {
                    if (Thread.interrupted()) {
                        p.destroy();
                        System.out.println("interrupt");
                        break;
                    }

                }

                System.out.println("rendered frame: " + task.getFrame());

            } catch (IOException ex) {
            } catch (Exception ex) {
                synchronized (Information.getSynchronizer()) {
                    Storage.getRenderTasks().addFirst(task);
                }
            } finally {
                pythonFile.delete();
            }

        }

        System.out.println("Task finished");

    }

}
