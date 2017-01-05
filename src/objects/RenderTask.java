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

/**
 *
 * @author kilian
 */
public class RenderTask implements Serializable{

    private BlenderFile file;
    private int frame;

    public RenderTask(BlenderFile file, int frame) {
        this.file = file;
        this.frame = frame;
    }

    //<editor-fold defaultstate="collapsed" desc="getters and Setters">
    public BlenderFile getFile() {
        return file;
    }

    public void setFile(BlenderFile file) {
        this.file = file;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }
//</editor-fold>

}
