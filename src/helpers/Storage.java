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
package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import javafx.stage.Window;
import objects.BlenderFile;
import objects.RenderQueue;
import objects.RenderTask;
import objects.Settings;

/**
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class Storage {

    private static Settings settings = new Settings();
    private static RenderQueue queue = new RenderQueue();

    private static final File pathToSettings = new File(Paths.getSettingsPath());
    private static final File pathToQueue = new File(Paths.getQueuePath());

    public static void loadSettings() {

        settings = (Settings) loadObject(pathToSettings, settings);
        queue = (RenderQueue) loadObject(pathToQueue, queue);
        
        Information.setFramesRendered(queue.getFramesRendered());

    }

    public static void saveSettings() {

        saveObject(pathToSettings, settings);
        saveObject(pathToQueue, queue);

    }

    private static Object loadObject(File pathToFile, Object objToAssign) {
        if (pathToFile.exists()) {
            try {

                ObjectInputStream reader = new ObjectInputStream(new FileInputStream(pathToFile));
                objToAssign = reader.readObject();

            } catch (ClassNotFoundException | IOException ex) {
                Actions.showError("Error at reading settings", ex);
            }

        } else {

            System.out.println("creating new file");

            pathToFile.getParentFile().mkdirs();
            try {

                pathToFile.createNewFile();
                ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(pathToFile));
                writer.writeObject(objToAssign);

            } catch (IOException ex) {
                Actions.showError("Error at reading settings", ex);
            }
        }

        return objToAssign;
    }

    private static void saveObject(File pathToFile, Object objectToWrite) {
        try {
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(pathToFile));
            writer.writeObject(objectToWrite);
        } catch (IOException ex) {
            Actions.showError("Error at saving settings", ex);
        }
    }

    public static File getPathToBlenderExe(Window window) {
        return settings.getPathToBlenderExe(window);
    }

    public static void setPathToBlenderExe(Window window) {
        settings.setPathToBlenderExe(window);
    }

    public static List<BlenderFile> getFilesToRender() {
        return queue.getFilesToRender();
    }

    public static void setFilesToRender(List<BlenderFile> filesToRender) {
        queue.setFilesToRender(filesToRender);
    }

    public static LinkedList<RenderTask> getRenderTasks() {
        return queue.getTasks();
    }

    public static void setRenderTasks(LinkedList<RenderTask> tasks) {
        queue.setTasks(tasks);
    }

    public static void addFileToRender(BlenderFile file) {
        queue.getFilesToRender().add(file);
    }

    public static Settings getSettings() {
        return settings;
    }

    public static RenderQueue getQueue() {
        return queue;
    }

}
