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

import java.util.HashMap;

/**
 *
 * @author kilian
 */
public class Constants {

    final public static String pythonImportLine = "import bpy\n";
    final public static String blenderImageformatPythonString = "bpy.data.scenes[\"Scene\"].render.image_settings.file_format";
    final public static String blenderStartFramePythonString = "bpy.data.scenes[\"Scene\"].frame_start";
    final public static String blenderEndFramePythonString = "bpy.data.scenes[\"Scene\"].frame_end";
    final public static String blenderRendererPytonString = "bpy.data.scenes[\"Scene\"].render.engine";
    final public static String blenderPathPytonString = "bpy.data.scenes[\"Scene\"].render.filepath";
    final public static HashMap<String, Integer> ImageFormatToIndex = new HashMap<>();
    final public static HashMap<Integer, String> IndexToImageFormat = new HashMap<>();

    public static void initConstants() {
        ImageFormatToIndex.put("PNG", 0);
        ImageFormatToIndex.put("JPEG", 1);
        ImageFormatToIndex.put("BMP", 2);
        ImageFormatToIndex.put("TIFF", 3);
        
        IndexToImageFormat.put(0, "PNG");
        IndexToImageFormat.put(1, "JPEG");
        IndexToImageFormat.put(2, "BMP");
        IndexToImageFormat.put(3, "TIFF");
    }
}
