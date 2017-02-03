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
import Server.Computer;
import Server.CopyBlenderFileThread;
import Server.CopyRenderTask;
import helpers.FileHelpers;
import helpers.Information;
import helpers.Storage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import javafx.stage.Window;

/**
 *
 * @author Kilian visit me on <aklio.de>
 */
public class RenderThread extends Thread {

    private Graphicboard board;
    private final Window window;
    private ThreadState threadState;

    public enum ThreadState {
        renderingGPU,
        renderingCPU,
        waitingGPU,
        waitingCPU
    }

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

        while (!Information.isStopRendering()) {

            if (board == null) {
                if (!Storage.getSettings().isAllowCPU()) {
                    try {
                        threadState = ThreadState.waitingCPU;
                        Thread.sleep(1000);
                        continue;
                    } catch (InterruptedException ex) {
                    }
                }
            } else if (!board.isAllowed()) {
                try {
                    threadState = ThreadState.waitingGPU;
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException ex) {
                }
            }

            if (board == null) {
                threadState = ThreadState.renderingCPU;
            } else {
                threadState = ThreadState.renderingGPU;
            }

            RenderTask task;

            if (Information.isClient()) {

                task = new CopyRenderTask(Information.getLocalComputer().getIpAddress(), Information.getLocalComputer().getPort()).getRenderTask();
                Computer.addTaskToRender(task);
                if (!new File(BlenderFile.getFilenameById(task.getFile().getId())).exists()) {
                    new CopyBlenderFileThread(Information.getLocalComputer().getIpAddress(), Information.getLocalComputer().getPort(),
                            task.getFile().getId()).runWithoutThread();
                }

            } else {
                synchronized (Information.getSynchronizer()) {
                    if (!Storage.getRenderTasks().isEmpty()) {
                        task = Storage.getRenderTasks().removeFirst(); //todo: check if isSlave and gather value
                    } else {
                        break;
                    }
                }
            }

            if (task.getFrame() > task.getFile().getEndFrame() || task.getFrame() < task.getFile().getStartFrame()) {
                System.out.println("Skipped Frame: " + task.getFrame());
                Information.increaseFramesRendered(1);
                continue;
            }

            if (!Storage.getFilesToRender().contains(task.getFile())) {
                System.out.println("File not in Files to Render: " + task.getFile().getPath().getName());
                Information.fixTaskList(task.getFile());
                continue;
            }

            String pythonContent = "";

            if (board == null) {
                pythonContent = "import bpy\n"
                        + "bpy.context.scene.cycles.device = \"CPU\"";
            } else {
                pythonContent = "import bpy\n"
                        + "bpy.context.user_preferences.system.compute_device_type = \"" + board.getType() + "\"\n"
                        + "bpy.context.scene.cycles.device = \"GPU\"\n"
                        + "bpy.context.user_preferences.system.compute_device = \"" + board.getSystemName() + "\"";
            }

            File pythonFile = new File(task.getFile().getPath().getParent() + File.separator + task.getFile().getPath().getName().split("\\.")[0] + task.getFrame() + ".py");
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
                        System.out.println("Thread " + this.getId() + " was shutdown");
                        Storage.getRenderTasks().addFirst(task);
                        return;
                    }

                }

                System.out.println("rendered task: " + task);
                Information.increaseFramesRendered(1);

            } catch (Exception ex) {
                synchronized (Information.getSynchronizer()) {
                    Storage.getRenderTasks().addFirst(task);
                }
            } finally {
                pythonFile.delete();
            }

            //todo: send back Rendered Image
            //if succesfull:
            Computer.removeTaskToRender(task);

        }

        System.out.println("Thread " + this.getId() + " finished");

    }

    public ThreadState getThreadState() {
        return threadState;
    }

}
