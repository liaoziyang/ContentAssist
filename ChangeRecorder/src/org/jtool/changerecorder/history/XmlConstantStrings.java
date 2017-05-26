/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.history;

/**
 * The elements, attributes, and values of the attributes appearing in XML documents
 * that store information on the operation history.
 * @author Katsuhisa Maruyama
 */
public interface XmlConstantStrings {
    
    static final String OperationHistoryElem = "OperationHistory";
    static final String OperationHistoryVersion = "1.0a";
    static final String OperationsElem = "operations";
    
    static final String NormalOperationElem = "normalOperation";
    static final String CompoundOperationElem = "compoundOperation";
    static final String CopyOperationElem = "copyOperation";
    static final String FileOperationElem = "fileOperation";
    static final String MenuOperationElem = "menuOperation";
    static final String ResourceOperationElem = "resourceOperation";
    
    static final String InsertedElem = "inserted";
    static final String DeletedElem = "deleted";
    static final String CopiedElem = "copied";
    static final String CodeElem = "code";
    
    static final String VersionAttr = "version";
    static final String TimeAttr = "time";
    static final String SeqAttr = "seq";
    static final String OffsetAttr = "offset";
    static final String FileAttr = "file";
    static final String ActionAttr = "action";
    static final String LabelAttr = "label";
    static final String AuthorAttr = "author";
    
    static final String TargetAttr = "target";
    static final String APathAttr = "apath";
    
    static final String Yes = "yes";
    static final String No = "no";
    static final String None = "None";
}
