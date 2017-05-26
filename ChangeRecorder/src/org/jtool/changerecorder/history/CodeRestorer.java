/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.history;

import org.jtool.changerecorder.operation.CompoundOperation;
import org.jtool.changerecorder.operation.IOperation;
import org.jtool.changerecorder.operation.NormalOperation;

/**
 * Applies an operation into code.
 * @author Katsuhisa Maruyama
 */
public class CodeRestorer {
    
    /**
     * Obtains the code after the application of a given operation into given code.
     * @param preCode the code before the application
     * @param op the operation to be applied
     * @return the resulting code after the application
     */
    public static String applyOperation(String preCode, IOperation op) {
        if (op instanceof NormalOperation) {
            return applyOperation(preCode, (NormalOperation)op);
            
        } else if (op instanceof CompoundOperation) {
            return applyOperation(preCode, (CompoundOperation)op);
        }
        return preCode;
    }
    
    /**
     * Applies a specified normal operation into given code.
     * @param preCode the code before the application
     * @param op the operation to be applied
     * @return the resulting code after the application
     */
    public static String applyOperation(String preCode, NormalOperation op) {
        StringBuilder postCode = new StringBuilder(preCode);
        
        if (hasDeletionMismatch(postCode, op)) {
            return null;
        }
        
        int start = op.getStart();
        int end = start + op.getDeletedText().length();
        String itext = op.getInsertedText();
        postCode.replace(start, end, itext);
        return postCode.toString();
    }
    
    /**
     * Applies a given compound operation into given code.
     * @param preCode the code before the application
     * @param cop the operation to be applied
     * @return the resulting code after the application
     */
    public static String applyOperation(String preCode, CompoundOperation cop) {
        String postCode = null;
        
        for (IOperation op : cop.getLeaves()) {
            if (op instanceof CompoundOperation) {
                applyOperation(preCode, (CompoundOperation)op);
                
            } else if (op instanceof NormalOperation) {
                postCode = applyOperation(preCode, (NormalOperation)op);
                if (postCode == null) {
                    return null;
                }
                preCode = postCode;
            }
        }
        return postCode;
    }
    
    /**
     * Obtains the code after the application of a given operation into given code reversely.
     * @param preCode the code before the application
     * @param op the operation to be applied
     * @return the resulting code after the application
     */
    public static String applyOperationReversely(String preCode, IOperation op) {
        if (op instanceof NormalOperation) {
            return applyOperationReversely(preCode, (NormalOperation)op);
            
        } else if (op instanceof CompoundOperation) {
            return applyOperationReversely(preCode, (CompoundOperation)op);
            
        }
        return preCode;
    }
    
    /**
     * Applies a specified normal operation into given code reversely.
     * @param preCode the code before the application
     * @param op the operation to be applied
     * @return the resulting code after the application
     */
    public static String applyOperationReversely(String preCode, NormalOperation op) {
        StringBuilder postCode = new StringBuilder(preCode);
        
        if (hasInsertionMismatch(postCode, op)) {
            return null;
        }
        
        int start = op.getStart();
        int end = start + op.getDeletedText().length();
        String dtext = op.getDeletedText();
        postCode.replace(start, end, dtext);
        return postCode.toString();
    }
    
    /**
     * Applies a given compound operation into given code reversely.
     * @param preCode the code before the application
     * @param cop the operation to be applied
     * @return the resulting code after the application
     */
    public static String applyOperationReversely(String preCode, CompoundOperation cop) {
        String postCode = null;
        
        for (IOperation op : cop.getLeaves()) {
            if (op instanceof CompoundOperation) {
                applyOperationReversely(preCode, (CompoundOperation)op);
                
            } else if (op instanceof NormalOperation) {
                postCode = applyOperationReversely(preCode, (NormalOperation)op);
                if (postCode == null) {
                    return null;
                }
                preCode = postCode;
            }
        }
        return postCode;
    }
    
    /**
     * Tests if the deletion derives any mismatch.
     * @param code the code before the application
     * @param op the operation to be applied
     * @return <code>true</code> if a mismatch exists, otherwise <code>false</code>
     */
    private static boolean hasDeletionMismatch(StringBuilder code, NormalOperation op) {
        String dtext = op.getDeletedText();
        int start = op.getStart();
        int end = start + dtext.length();
        
        if (dtext.length() > 0) {
            String rtext = code.substring(start, end);
            if (rtext != null && rtext.compareTo(dtext) != 0) {
                
                for (int i = 0; i < rtext.length(); i++) {
                    if (rtext.charAt(i) == dtext.charAt(i)) {
                        System.out.println(((int)rtext.charAt(i)) + " == " + ((int)dtext.charAt(i)));
                    } else {
                        System.out.println(((int)rtext.charAt(i)) + " != " + ((int)dtext.charAt(i)));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Tests if the deletion derives any mismatch.
     * @param code the code before the application
     * @param op the operation to be applied
     * @return <code>true</code> if a mismatch exists, otherwise <code>false</code>
     */
    private static boolean hasInsertionMismatch(StringBuilder code, NormalOperation op) {
        String itext = op.getDeletedText();
        int start = op.getStart();
        int end = start + itext.length();
        
        if (itext.length() > 0) {
            String rtext = code.substring(start, end);
            if (rtext != null && rtext.compareTo(itext) != 0) {
                
                for (int i = 0; i < rtext.length(); i++) {
                    if (rtext.charAt(i) == itext.charAt(i)) {
                        System.out.println(((int)rtext.charAt(i)) + " == " + ((int)itext.charAt(i)));
                    } else {
                        System.out.println(((int)rtext.charAt(i)) + " != " + ((int)itext.charAt(i)));
                    }
                }
                return true;
            }
        }
        return false;
    }
}
