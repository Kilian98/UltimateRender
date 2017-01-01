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

    public Graphicboard(String displayname, BoardType type, String systemName) {

        this.displayName = displayname;
        this.type = type;
        this.systemName = systemName;

        allowed = true;

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public BoardType getType() {
        return type;
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

}
