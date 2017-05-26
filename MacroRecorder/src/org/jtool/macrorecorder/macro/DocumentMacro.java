/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.macro;

/**
 * Stores a macro occurring a document change.
 * @author Katsuhisa Maruyama
 */
public class DocumentMacro extends Macro {
    
    /**
     * The leftmost offset of the document changed by this macro.
     */
    private int start;
    
    /**
     * The contents of the document inserted by this macro.
     */
    private String insertedText;
    
    /**
     * The contents of the document deleted by this macro.
     */
    private String deletedText;
    
    /**
     * Creates an object storing information on a document macro.
     * @param stime the time when the macro started
     * @param etime the time when the macro ended
     * @param type the type of the macro
     * @param path the path of a file or a package this macro was performed
     * @param start the leftmost offset of the document changed by this macro
     * @param itext the contents of the document inserted by the macro
     * @param dtext the contents of the document deleted by the macro
     */
    public DocumentMacro(long stime, long etime, String type, String path, int start, String itext, String dtext) {
        super(stime, etime, type, path);
        this.start = start;
        this.insertedText = itext;
        this.deletedText = dtext;
    }
    
    /**
     * Creates an object storing information on a document macro.
     * @param time the time when the macro started and ended
     * @param type the type of the macro
     * @param path the path of a file or a package this macro was performed
     * @param start the leftmost offset of the document changed by this macro
     * @param itext the contents of the document inserted by the macro
     * @param dtext the contents of the document deleted by the macro
     */
    public DocumentMacro(long time, String type, String path, int start, String itext, String dtext) {
        this(time, time, type, path, start, itext, dtext);
    }
    
    /**
     * Returns the leftmost offset of the document changed by this macro.
     * @return the leftmost offset of the document
     */
    public int getStart() {
        return start;
    }
    
    /**
     * Returns the contents of the document inserted by this macro.
     * @return the inserted contents of the document
     */
    public String getInsertedText() {
        return insertedText;
    }
    
    /**
     *  Returns the contents of the document deleted by this macro.
     * @return the deleted contents of the document
     */
    public String getDeletedText() {
        return deletedText;
    }
    
    /**
     * Tests if this macro inserts any text and deletes no text.
     * @return <code>true</code> if this macro performs insertion, otherwise <code>false>
     */
    public boolean isInsert() {
        return insertedText.length() != 0 && deletedText.length() == 0;
    }
    
    /**
     * Tests if this macro inserts no text and deletes any text.
     * @return <code>true</code> if this macro performs deletion, otherwise <code>false>
     */
    public boolean isDelete() {
        return insertedText.length() == 0 && deletedText.length() != 0;
    }
    
    /**
     * Tests if this macro inserts any text and deletes any text.
     * @return <code>true</code> if this macro performs replacement, otherwise <code>false>
     */
    public boolean isReplace() {
        return insertedText.length() != 0 && deletedText.length() != 0;
    }
    
    /**
     * Returns the string for printing, which does not contain a new line character at its end.
     * @return the string for printing
     */
    public String toString() {
        return "DOC(" + getType() + ") = " + getStartTime() + "-" + getEndTime() + ": " +
               getPath() + " " + getStart() + " " + "[" + getInsertedText() + "][" + getDeletedText() + "]";
    }
}
