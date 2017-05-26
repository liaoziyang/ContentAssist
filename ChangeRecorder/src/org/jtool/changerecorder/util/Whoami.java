/*
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Obtains the name of a user editing source code.
 * @author Katsuhisa Maruyama
 */
public class Whoami {
    
    /**
     * The name of a command for obtaining developer's name.
     */
    private static final String whoamiCmd = "whoami";
    
    /**
     * The user name.
     */
    private static String userName = null;
    
    /**
     * Obtains the user name by using the <code>whoami</code> command.
     * @return the user name
     */
    public static String getUserName() {
        if (userName != null) {
            return userName;
        }
        
        userName = "Unknown";
        
        BufferedReader reader = null;
        Process process = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(whoamiCmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            userName = reader.readLine();
            if (userName.indexOf("\\") >= 0) { // for Windows format (host_name + "\" + user_name) 
                userName = userName.substring(userName.indexOf("\\") + 1);
            }
        } catch (IOException e) {
            /* empty */
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    /* empty */
                }
            }
        }
        
        return userName;
    }
}
