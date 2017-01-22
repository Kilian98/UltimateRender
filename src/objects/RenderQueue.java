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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author kilian
 */
public class RenderQueue implements Serializable {

    private List<BlenderFile> filesToRender;
    private LinkedList<RenderTask> tasks;
    private int framesRendered;

    public RenderQueue() {
        filesToRender = new ArrayList<>();
        tasks = new LinkedList<>();
    }

    public void makeTasks(List<BlenderFile> files, boolean overwrite) {

        if (overwrite) {
            tasks.clear();
        }

        for (BlenderFile f : files) {

            for (int i = f.getStartFrame(); i <= f.getEndFrame(); i++) {
                tasks.add(new RenderTask(f, i));
            }
        }

    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public List<BlenderFile> getFilesToRender() {
        return filesToRender;
    }

    public void setFilesToRender(List<BlenderFile> filesToRender) {
        this.filesToRender = filesToRender;
    }

    public LinkedList<RenderTask> getTasks() {
        return tasks;
    }

    public void setTasks(LinkedList<RenderTask> tasks) {
        this.tasks = tasks;
    }

    public int getFramesRendered() {
        return framesRendered;
    }

    public void setFramesRendered(int framesRendered) {
        this.framesRendered = framesRendered;
    }

//</editor-fold>
}
