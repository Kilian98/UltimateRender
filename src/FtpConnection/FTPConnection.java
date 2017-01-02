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
package FtpConnection;

import java.io.File;

/**
 *
 * @author MarkTheSmasher
 */
public class FTPConnection {

    /*Wichtige Funktionen:
        * Konstruktor: FTP-Adresse, Username, Passwort --> Verbindung herstellen
        * file.listFiles
        * file.copyFileToServer
        * file.copyFileFromServe
              
    */
    //
    //
    /*
        * Aufzählung aller Grafikkarten (List<Grafikkarte>)
        * Grafikkarte.getName()
        * Grafikkarte.getSystemName()
        * 
    */
    private String ip;
    private String un;
    private String pw;

    public FTPConnection(String newip, String newun, String newpw) {
        ip=newip;
        un=newun;
        pw=newpw;
    }
    
    
}
