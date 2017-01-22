/* 
 * Copyright (C) 2017 >Kilian
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
package helpers;

import FXMLContainer.Container_InformationController;
import java.util.LinkedList;
import objects.BlenderFile;
import objects.RenderTask;
import objects.RenderThread;

/**
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class Information {

    static private Container_InformationController infoController;

    public enum Status {
        Rendering,
        Pausing,
        Stopped
    }

    static private boolean stopRendering;
    static private RenderThread[] threads;
    private static final Object synchronizer = new Object();

    //status information
    static int renderingThreads; //only tmp value
    static int waitingThreads; //only tmp value
    static int serverThreads; //only tmp value
    static Status status = Status.Stopped;
    static int extRenderingThreads; //only tmp value
    static int computersConnected; //only tmp value

    static int framesRendered;
    static int framesRemaining;

    static long timeRendering;

    static private int threadsRunning;

    /**
     * *
     * You may check, if there are threads running in the background e.g. before
     * closing the program
     *
     * @return boolean if there are still background tasks running
     */
    public static boolean threadsRunning() {
        if (threads == null) {
            return false;
        }
        for (Thread t : threads) {
            if (t != null && t.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public static int getMaxCpuCernels() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static boolean isStopRendering() {
        return stopRendering;
    }

    public static void setStopRendering(boolean stopRendering) {
        Information.stopRendering = stopRendering;
    }

    public static RenderThread[] getProcesses() {
        return threads;
    }

    public static void setThreads(RenderThread[] thread) {
        Information.threads = thread;
    }

    public static void abortRendering() {

        stopRendering = true;

        for (Thread p : threads) {
            try {
                p.interrupt();
                p.join();
            } catch (InterruptedException ex) {
            }
        }
        
        

    }

    public static void updateInformation() {

        renderingThreads = 0;
        waitingThreads = 0;

        if (!threadsRunning()) {
            status = Status.Stopped;
        } else {
            for (RenderThread t : threads) {
                if (t.getThreadState() == RenderThread.ThreadState.renderingCPU || t.getThreadState() == RenderThread.ThreadState.renderingGPU) {
                    renderingThreads++;
                } else {
                    waitingThreads++;
                }
            }
        }

        Information.setFramesRemaining(Storage.getQueue().getTasks().size() + renderingThreads + extRenderingThreads);

    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public static Object getSynchronizer() {
        return synchronizer;
    }

    public static RenderThread[] getThreads() {
        return threads;
    }

    public static int getRenderingThreads() {
        return renderingThreads;
    }

    public static int getWaitingThreads() {
        return waitingThreads;
    }

    public static int getServerThreads() {
        return serverThreads;
    }

    public static int getFramesRendered() {
        return framesRendered;
    }

    public static int getFramesRemaining() {
        return framesRemaining;
    }

    public static long getTimeRendering() {
        return timeRendering;
    }

    public static void setInfoController(Container_InformationController infoController) {
        Information.infoController = infoController;
    }

    public static void setRenderingThreads(int renderingThreads) {
        Information.renderingThreads = renderingThreads;
    }

    public static void setWaitingThreads(int waitingThreads) {
        Information.waitingThreads = waitingThreads;
    }

    public static void setServerThreads(int serverThreads) {
        Information.serverThreads = serverThreads;
    }

    public static void setFramesRendered(int framesRendered) {
        Information.framesRendered = framesRendered;
    }

    public static void setFramesRemaining(int framesRemaining) {
        Information.framesRemaining = framesRemaining;
    }

    public static void setTimeRendering(long timeRendering) {
        Information.timeRendering = timeRendering;
    }

    public static Container_InformationController getInfoController() {
        return infoController;
    }

    public static Status getStatus() {
        return status;
    }

    public static void setStatus(Status status) {
        Information.status = status;
    }

    public static int getComputersConnected() {
        return computersConnected;
    }

    public static void setComputersConnected(int computersConnected) {
        Information.computersConnected = computersConnected;
    }

    public static int getExtRenderingThreads() {
        return extRenderingThreads;
    }

    public static void setExtRenderingThreads(int extRenderingThreads) {
        Information.extRenderingThreads = extRenderingThreads;
    }

    public static void increaseFramesRendered(int i) {
        synchronized (synchronizer) {
            framesRendered += i;
            Storage.getQueue().setFramesRendered(framesRendered);
        }
    }
    
    public static void clearFramesRendered(){
        framesRendered = 0;
        Storage.getQueue().setFramesRendered(0);
    }

    /**
     * *
     * If a Task with a file not contained in the to-Render-List, this method
     * should be executed, to delete all the tasks associated with this file
     *
     * @param file the file that is not in the to-Render-List anymore
     */
    public static void fixTaskList(BlenderFile file) {

        synchronized (synchronizer) {

            LinkedList<RenderTask> tasks = Storage.getRenderTasks();

            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getFile() == file) {
                    System.out.println("Removed task: " + tasks.get(i));
                    tasks.remove(i);
                    i--;
                }
            }

        }

    }

//</editor-fold>
}
