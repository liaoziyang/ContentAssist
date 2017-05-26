/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.recorder;

import org.jtool.macrorecorder.macro.DocumentMacro;

/**
 * Compresses macros.
 * @author Katsuhisa Maruyama
 */
public class MacroCompressor {
    
    /**
     * Creates an object compressing macros.
     */
    public MacroCompressor() {
    }
    
    /**
     * Tests if a document macros can be combined with its previous macro.
     * @param macro the document macro
     * @return <code>true</code> if the macros can be combined, otherwise <code>false</code>
     */
    public boolean canCombine(DocumentMacro macro) {
        if (macro == null) {
            return false;
        }
        
        if (macro.isInsert()) {
            return combineWith(macro.getInsertedText());
        }
        
        if (macro.isDelete()) {
            return combineWith(macro.getDeletedText());
        }
        
        if (macro.isReplace()) {
            return combineWith(macro.getInsertedText()) && combineWith(macro.getDeletedText());
        }
        
        return false;
    }
    
    /**
     * Combines successive two document macros.
     * @param last the former document macro 
     * @param next the latter document macro
     * @return the combined macro, or <code>null</code> if the macro cannot be combined
     */
    public DocumentMacro combine(DocumentMacro last, DocumentMacro next) {
        if (next == null) {
            return null;
        }
        
        if (next.isInsert()) {
            return combineInsertMacro(last, next);
        }
        
        if (next.isDelete()) {
            return combineDeleteMacro(last, next);
        }
        
        if (next.isReplace()) {
            return compressReplaceMacro(last, next);
        }
        
        return null;
    }
    
    /**
     * Combines successive two macro.
     * @param last the former document macro
     * @param next the latter document macro that represents the insertion
     * @return the combined macro, or <code>null</code> if the macros cannot be combined
     */
    private DocumentMacro combineInsertMacro(DocumentMacro last, DocumentMacro next) {
        if (last == null) {
            return next;
        }
        
        if (!last.isInsert() || !combineWith(last.getInsertedText())) {
            return null;
        }
        
        if (last.getStart() + last.getInsertedText().length() == next.getStart()) {
            String text = last.getInsertedText() + next.getInsertedText();
            return new DocumentMacro(last.getStartTime(), next.getEndTime(),
                                     last.getType(), last.getPath(), last.getStart(), text, ""); 
        }
        
        return null;
    }
    
    /**
     * Combines successive two macros.
     * @param last the former document macro
     * @param next the latter document macro that represents deletion
     * @return the combined macro, or <code>null</code> if the macros cannot be combined
     */
    private DocumentMacro combineDeleteMacro(DocumentMacro last, DocumentMacro next) {
        if (last == null) {
            return next;
        }
        
        if (!last.isDelete() || !combineWith(last.getDeletedText())) {
            return null;
        }
        
        if (last.getStart() > next.getStart()) {
            if (last.getStart() == next.getStart() + next.getDeletedText().length()) {
                String text = next.getDeletedText() + last.getDeletedText();
                
                return new DocumentMacro(last.getStartTime(), next.getEndTime(),
                                         last.getType(), last.getPath(), next.getStart(), "", text); 
            }
            
        } else {
            if (last.getStart() + last.getInsertedText().length() == next.getStart()) {
                String text = last.getDeletedText() + next.getDeletedText();
                
                return new DocumentMacro(last.getStartTime(), next.getEndTime(),
                                         last.getType(), last.getPath(), last.getStart(), "", text); 
            }
        }
        
        return null;
    }
    
    /**
     * Compresses successive two macros.
     * @param last the former document macro
     * @param next the latter document macro that represents replacement
     * @return the combined macro, or <code>null</code> if the macros cannot be combined
     */
    private DocumentMacro compressReplaceMacro(DocumentMacro last, DocumentMacro next) {
        if (last == null) {
            return next;
        }
        
        if (!(last.isInsert() || last.isReplace()) ||
            !combineWith(last.getInsertedText()) || !combineWith(last.getDeletedText())) {
            return null;
        }
        
        if (last.getStart() == next.getStart() &&
            last.getInsertedText().compareTo(next.getDeletedText()) == 0) {
            String itext = next.getInsertedText();
            String dtext = last.getDeletedText();
            return new DocumentMacro(last.getStartTime(), next.getEndTime(),
                                     last.getType(), last.getPath(), last.getStart(), itext, dtext); 
        }
        
        return null;
    }
    
    /**
     * Tests if a given text can be combined with another one.
     * @param text the text to be combined
     * @return <code>true</code> if the can be combined, otherwise <code>false</code>
     */
    public boolean combineWith(String text) {
        char[] DELIMITER_CHARS = new char[] { '\n', '\r', ';', '{', '}' };
        
        for (int i = 0; i < DELIMITER_CHARS.length; i++) {
            if (text.indexOf(DELIMITER_CHARS[i]) >= 0) {
                return false;
            }
        }
        return true;
    }
}
