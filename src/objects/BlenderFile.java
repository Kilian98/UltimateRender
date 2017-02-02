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

import Exceptions.ParseException;
import Exceptions.ReadBlenderException;
import Exceptions.UnknownRendererException;
import helpers.Actions;
import static helpers.Actions.parseInt;
import helpers.Constants;
import static helpers.Information.getMaxCpuCernels;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import javafx.stage.Window;

/**
 *
 * @author Kilian Brenner visit me on <aklio.de>
 */
public class BlenderFile implements Serializable {

    public enum Renderer {
        BlenderRenderer,
        CyclesRenderer
    }

    File path;

    private int startFrame;
    private int endFrame;

    private boolean allowGPU; //for some projects the GPU memory is not sufficient for the rendering or Blender Renderer does not support GPU
    private int maxThreads = getMaxCpuCernels();

    private Renderer renderer;
    private File pathToRender;
    private String fileFormat;
    private boolean adjustedSettings = false;

    final private File pathToRender_orig;
    final private String fileFormat_orig;
    final private int startFrame_orig;
    final private int endFrame_orig;

    final private long id;

    /**
     * Creates a new BlenderFile. The required Information will be read out of
     * the blender document
     *
     * @param blenderFile the Blender File
     * @throws java.io.IOException if the file is not found
     * @throws java.lang.InterruptedException if the blender task executed to
     * gather the information from the file is being killed
     * @throws Exceptions.UnknownRendererException if the renderer is neither
     * the Blender Renderer nor Cycles
     * @throws Exceptions.ParseException if something goes wrong with the
     * parsing. This happens most likely when reading information from the
     * blender file (not read the correct output)
     * @throws Exceptions.ReadBlenderException
     */
    public BlenderFile(File blenderFile, Window window) throws IOException, InterruptedException, UnknownRendererException, ParseException, ReadBlenderException {

        id = Actions.getNextBlenderFileID();
        path = blenderFile;

        HashMap<Integer, String> map = Actions.getInformationsFromBlenderFile(blenderFile, ""
                + Constants.blenderRendererPytonString + "\n"
                + Constants.blenderStartFramePythonString + "\n"
                + Constants.blenderEndFramePythonString + "\n"
                + Constants.blenderImageformatPythonString + "\n"
                + Constants.blenderPathPytonString,
                window);

        String tmp = map.get(0);
        int tmpInt = 0;

        if (tmp.equals("BLENDER_RENDER")) {
            renderer = Renderer.BlenderRenderer;
            allowGPU = false;
        } else if (tmp.equals("CYCLES")) {
            renderer = Renderer.CyclesRenderer;
            allowGPU = true;
        } else {
            throw new UnknownRendererException();
        }

        try {
            tmpInt = parseInt(map.get(1));
            startFrame = tmpInt;
            startFrame_orig = tmpInt;
            tmpInt = parseInt(map.get(2));
            endFrame = tmpInt;
            endFrame_orig = tmpInt;
        } catch (NumberFormatException e) {
            throw new ParseException();
        }

        tmp = map.get(3);

        if (tmp.equals("PNG") || tmp.equals("JPEG") || tmp.equals("BMP") || tmp.equals("TIFF")) {
            fileFormat = tmp;
            fileFormat_orig = tmp;
        } else {
            fileFormat = "PNG";
            fileFormat_orig = "PNG";
        }

        tmp = map.get(4);

        if (tmp.startsWith("//")) {

            File currentDir = path.getParentFile();
            String[] splitted = tmp.substring(2).split(File.separator);
            String lastPath = "";

            for (String s : splitted) {
                if (s.equals("..")) {
                    currentDir = currentDir.getParentFile();
                } else {
                    lastPath += s + File.separator;
                }
            }

            tmp = currentDir.toString() + File.separator + lastPath.substring(0, lastPath.length() - 1);

        }

        if (tmp.endsWith(File.separator)) {
            tmp += path.getName().split("\\.")[0];
        }

        pathToRender = new File(tmp);
        pathToRender_orig = new File(tmp);

    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    @Override
    public String toString() {
        return path.getName();
    }

    public File getPath() {
        return path;
    }

    public int getStartFrame() {
        if (adjustedSettings) {
            return startFrame;
        } else {
            return startFrame_orig;
        }
    }

    public int getEndFrame() {
        if (adjustedSettings) {
            return endFrame;
        } else {
            return endFrame_orig;
        }
    }

    public boolean isAllowGPU() {
        return allowGPU;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public File getPathToRender() {
        if (adjustedSettings) {
            return pathToRender;
        } else {
            return pathToRender_orig;
        }
    }

    public String getFileFormat() {
        if (adjustedSettings) {
            return fileFormat;
        } else {
            return fileFormat_orig;
        }
    }

    public boolean isAdjustedSettings() {
        return adjustedSettings;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }

    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setPathToRender(File pathToRender) {
        this.pathToRender = pathToRender;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public void setAdjustedSettings(boolean adjustedSettings) {
        this.adjustedSettings = adjustedSettings;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public long getId() {
        return id;
    }

//</editor-fold>
}
