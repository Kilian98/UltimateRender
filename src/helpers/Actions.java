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

import Exceptions.ReadBlenderException;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import objects.BlenderFile;

/**
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class Actions {

    private final static String numbers = "0123456789";
    private static int blenderFileId = 0;

    /**
     * Asks if the program should be closed, but only when there are running
     * tasks in the background
     *
     * @param event window event to consume the try of closing
     */
    public static void saveClose(WindowEvent event) {

        File f = new File(Paths.getWorkingDir());
        if (f.exists()) {
            FileHelpers.deleteDir(f);
        }

        if (Information.threadsRunning()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Do you really want to close this program?");
            alert.setContentText("There are still background tasks running, they will be shut down as well!");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Storage.saveSettings();
                Information.abortRendering();
                System.exit(0);
            } else if (event != null) {
                event.consume();
            }
        } else {

            Storage.saveSettings();

            System.exit(0);
        }

    }
    
    public static void closeStream(Closeable s) {

        if (s == null) {
            return;
        }

        try {
            s.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void showError(String message, Exception ex) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(ex.getClass().toString());
                alert.setHeaderText(ex.getMessage());
                alert.getDialogPane().setContent(new Label(message));
//        alert.setContentText(ex.getMessage());

                alert.showAndWait();
            }
        });

    }

    public static void showAlert(String title, String header, String content) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.getDialogPane().setContent(new Label(content));
                //alert.setContentText(ex.getMessage());

                alert.showAndWait();

            }
        });

    }

    public static HashMap<Integer, String> getInformationsFromBlenderFile(File blenderFile, String pythonArgument, Window window) throws IOException, InterruptedException, ReadBlenderException {

        HashMap<Integer, String> map = new HashMap<>();

        String tmp = "";
        String output = "";
        File pathToPythonFile = new File(blenderFile.toString().replace(".blend", "") + ".py");

        String fullPythonArgument = "";

        for (String s : pythonArgument.split("\n")) {
            fullPythonArgument += "print(\"I424242: \" + str(" + s + "))" + "\n";
        }

        FileHelpers.writeFile(pathToPythonFile, Constants.pythonImportLine + fullPythonArgument);

//        ProcessBuilder pb = new ProcessBuilder("/home/kilian/Schreibtisch/blender-2.78a-linux-glibc211-x86_64/blender", "-b", blenderFile.toString(), "--python", pathToPythonFile.toString());
//        ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Blender Foundation\\Blender\\blender", "-b", blenderFile.toString(), "--python", pathToPythonFile.toString());
//
        ProcessBuilder pb = new ProcessBuilder(Storage.getPathToBlenderExe(window).toString().replace(".desktop", ""), "-b", blenderFile.toString(),
                "--python", pathToPythonFile.toString());
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();

        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));

        while ((tmp = bri.readLine()) != null) {
            output += tmp + "\n";
//            System.out.println(tmp);
        }

        String[] lines = output.split("\n");
        int i = 0;

        for (String s : lines) {

            if (s.startsWith("I424242: ")) {
                System.out.println(s);
                map.put(i, s.replace("I424242: ", ""));
                i++;
            } else {
            }

        }

        pathToPythonFile.delete();

        return map;

    }

    public static int parseInt(String str) {

        if (!str.equals("")) {
            return Integer.parseInt(str);
        } else {
            return 0;
        }

    }

    /**
     * checks, if a File is in the render queue already
     *
     * @param f the path to the new File
     * @return true, if the file already exists
     */
    public static boolean checkForExistingBlenderFile(File f) {

        for (BlenderFile bf : Storage.getFilesToRender()) {
            if (bf.getPath().toString().equals(f.toString())) {
                return true;
            }
        }

        return false;

    }

    public static boolean isOnlyNumbers(String str) {

        for (char s : str.toCharArray()) {

            if (!numbers.contains(s + "")) {
                return false;
            }

        }

        return true;

    }

    public static int getNextBlenderFileID() {
        blenderFileId++;
        return blenderFileId;
    }

}
