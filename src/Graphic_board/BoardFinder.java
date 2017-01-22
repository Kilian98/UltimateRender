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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MarkTheSmasher
 */
public class BoardFinder {

    public static List<Graphicboard> getGraphicBoards() {

        List<Graphicboard> liste = new ArrayList<>();

        //@Mark: todo: find all available graphic boards including Type, DisplayName and SystemName
        
        liste.add(new Graphicboard("Unknown (Usable if CUDA/Nvidia)", Graphicboard.BoardType.CUDA, "CUDA_0"));

        return liste;

    }

}
