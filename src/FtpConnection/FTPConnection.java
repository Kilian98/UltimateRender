/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FtpConnection;

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
