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

/**
 *
 * @author kilian
 */
public class Paths {
    
    private static String windowsSettingsPath = "UltimateRender\\settings.ul";
    private static String linuxSettingsPath = "UltimateRender/settings.ul";
    private static String macSettingsPath = "UltimateRender\\settings.ul";
    
    private static String windowsQueuePath = "UltimateRender\\queue.ul";
    private static String linuxQueuePath = "UltimateRender/queue.ul";
    private static String macQueuePath = "UltimateRender\\queue.ul";
    
    
    public static String getSettingsPath(){
        return linuxSettingsPath; //todo: check for running os
    }
    
    public static String getQueuePath(){
        return linuxQueuePath; //todo: check for running os
    }
}
