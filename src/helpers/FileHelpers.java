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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author kilian
 */
public class FileHelpers {

    /**
     *
     * @param path the file in which should be writen in (will be overwriten if
     * already exists)
     * @param content content what to write in the File
     * @return true if the action was succesful
     */
    public static boolean writeFile(File path, String content) {

        BufferedWriter writer = null;

        try {

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));

            String[] lines = content.split("\n");
            
            for (String s : lines){
                writer.write(s);
                writer.newLine();
            }
                    
            writer.flush();

        } catch (IOException e) {
            return false;
        } finally {
            closeWriter(writer);
        }

        return true;
    }

    static void closeWriter(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

}
