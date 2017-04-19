/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cleanoutloudserver;

import javax.xml.ws.Endpoint;


public class CleanOutLoadServer {
    
    public static void main(String[] arg) throws Exception{
        
        ICleanOutLoud CoL = new CleanOutLoudImpl();
        
        Endpoint.publish("http://[::]:3769/col", CoL);
        System.out.println("Clean out loud server startet");
        
        
    }
    
}
