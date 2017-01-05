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
package Graphic_board;

import java.io.Serializable;

/**
 *
 * @author Kilian Brenner
 */
public class Graphicboard implements Serializable{

    public enum BoardType {
        CUDA,
        OpenCL
    }

    String displayName;
    BoardType type;
    String systemName;
    boolean allowed;
    int renderingFrames;

    public Graphicboard(String displayname, BoardType type, String systemName) {

        this.displayName = displayname;
        this.type = type;
        this.systemName = systemName;

        allowed = true;
        renderingFrames = 0;

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type.toString();
    }

    public void setType(BoardType type) {
        this.type = type;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public int getRenderingFrames() {
        return renderingFrames;
    }

    public void setRenderingFrames(int renderingFrames) {
        this.renderingFrames = renderingFrames;
    }
    
    

}
