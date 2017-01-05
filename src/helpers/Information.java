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

/**
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class Information {

    static private int threadsRunning;
    static private boolean stopRendering;
    static private Thread[] threads;
    private static final Object synchronizer = new Object();

    /**
     * *
     * You may check, if there are threads running in the background e.g. before
     * closing the program
     *
     * @return boolean if there are still background tasks running
     */
    public static boolean threadsRunning() {
        return threadsRunning != 0;
    }

    public static int getMaxCpuCernels() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static int getThreadsRunning() {
        return threadsRunning;
    }

    public static void setThreadsRunning(int threadsRunning) {
        Information.threadsRunning = threadsRunning;
    }

    public static boolean isStopRendering() {
        return stopRendering;
    }

    public static void setStopRendering(boolean stopRendering) {
        Information.stopRendering = stopRendering;
    }

    public static Thread[] getProcesses() {
        return threads;
    }

    public static void setThreads(Thread[] thread) {
        Information.threads = thread;
    }

    public static void abortRendering() {

        stopRendering = true;
        
        for (Thread p : threads) {
            p.interrupt();
        }

        synchronized (synchronizer) {
            threadsRunning = 0;
        }

    }

    public static Object getSynchronizer() {
        return synchronizer;
    }

}
