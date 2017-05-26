/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.util;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

/**
 * Manages the file stream.
 * @author Katsuhisa Maruyama
 */
public class FileStream {
    
    /**
     * Reads and returns the contents of a file.
     * @param path the full path indicating the file to be read
     * @return the contents of the file
     */
    public static String read(String path) {
        File file = new File(path);
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return "";
    }
    
    /**
     * Writes the content into a file.
     * @param path the full path indicating the file which the contents are written into
     * @param contents the contents to be written
     */
    public static void write(String path, String contents) {
        makeDir(path);
        
        File file = new File(path);
        try {
            FileUtils.writeStringToFile(file, contents);
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Creates a directory containing a file.
     * @param path the path name of the file
     */
    public static void makeDir(String path) {
        try {
            FileUtils.forceMkdir(new File(path).getParentFile());
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
