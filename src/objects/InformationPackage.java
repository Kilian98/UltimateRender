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
public class InformationPackage implements Serializable{

    public int renderedFrames = -1;
    public int remainingFrames = -1;

    public int renderingThreads = -1;
    public int computersConnected = -1;
    public int id;

    public InformationPackage(int renderedFrames, int renderingThreads, int id) {
        this.renderedFrames = renderedFrames;
        this.renderingThreads = renderingThreads;
        this.id = id;
    }

    public InformationPackage(int renderedFrames, int remainingFrames, int renderingThreads, int computersConnected) {
        this.renderedFrames = renderedFrames;
        this.remainingFrames = remainingFrames;
        this.renderingThreads = renderingThreads;
        this.computersConnected = computersConnected;
    }

}
