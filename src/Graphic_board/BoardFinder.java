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
        
        liste.add(new Graphicboard("Nvidia GTX 660m", Graphicboard.BoardType.CUDA, "CUDA_0"));
        liste.add(new Graphicboard("Nvidia GTX 660m", Graphicboard.BoardType.CUDA, "CUDA_0"));
        liste.add(new Graphicboard("Nvidia GTX 660m", Graphicboard.BoardType.CUDA, "CUDA_0"));
        liste.add(new Graphicboard("Nvidia GTX 660m", Graphicboard.BoardType.CUDA, "CUDA_0"));
        liste.add(new Graphicboard("Nvidia GTX 660m", Graphicboard.BoardType.CUDA, "CUDA_0"));
        liste.add(new Graphicboard("Nvidia GTX 660m", Graphicboard.BoardType.CUDA, "CUDA_0"));

        return liste;

    }

}
