/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.history;

import org.jtool.changerecorder.operation.CompoundOperation;
import org.jtool.changerecorder.operation.CopyOperation;
import org.jtool.changerecorder.operation.FileOperation;
import org.jtool.changerecorder.operation.IOperation;
import org.jtool.changerecorder.operation.MenuOperation;
import org.jtool.changerecorder.operation.NormalOperation;
import org.jtool.changerecorder.operation.ResourceOperation;
import org.jtool.changerecorder.operation.TextOperation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Converts the operation history into the XML representation.
 * @author Katsuhisa Maruyama
 */
public class Operation2Xml {
    
    /**
     * Converts the Java object representation of the operation history into the XML representation.
     * @param history the operation history to be converted
     * @return the content of the DOM instance
     */
    public static Document convert(OperationHistory history) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            generateTree(doc, history);
            return doc;
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Generates the DOM tree corresponding to the operation history.
     * @param doc the content of the DOM instance
     * @param history the operation history to be converted
     */
    private static void generateTree(Document doc, OperationHistory history) {
        Element rootElem = doc.createElement(XmlConstantStrings.OperationHistoryElem);
        rootElem.setAttribute(XmlConstantStrings.VersionAttr, XmlConstantStrings.OperationHistoryVersion);
        doc.appendChild(rootElem);
        
        Element operations = doc.createElement(XmlConstantStrings.OperationsElem);
        rootElem.appendChild(operations);
        
        for (IOperation op : history.getOperations()) {
            createOperationsElement(doc, op, operations);
        }
    }
    
    /**
     * Creates a DOM element corresponding to the specified operation.
     * @param doc the content of the DOM instance
     * @param op the specified operation
     * @param parent the parent DOM element
     */
    private static void createOperationsElement(Document doc, IOperation op, Element parent) {
        if (op == null) {
            return;
            
        } else if (op.getOperationType() == IOperation.Type.NORMAL) {
            Element opElem = appendNormalOperationElement(doc, (NormalOperation)op);
            parent.appendChild(opElem);
            
        } else if (op.getOperationType() == IOperation.Type.COMPOUND) {
            Element opElem = appendCompoundOperationElement(doc, (CompoundOperation)op);
            parent.appendChild(opElem);
            
        } else if (op.getOperationType() == IOperation.Type.COPY) {
            Element opElem = appendCopyOperationElement(doc, (CopyOperation)op);
            parent.appendChild(opElem);
            
        } else if (op.getOperationType() == IOperation.Type.FILE) {
            Element opElem = appendFileOperationElement(doc, (FileOperation)op);
            parent.appendChild(opElem);
            
        } else if (op.getOperationType() == IOperation.Type.MENU) {
            Element opElem = appendMenuOperationElement(doc, (MenuOperation)op);
            parent.appendChild(opElem);
            
        } else if (op.getOperationType() == IOperation.Type.RESOURCE) {
            Element opElem = appendResourceOperationElement(doc, (ResourceOperation)op);
            parent.appendChild(opElem);
            
        } else {
            System.err.println(Operation2Xml.class.getName() + ": unknown operation");
        }
    }
    
    /**
     * Creates a DOM element corresponding to a normal operation.
     * @param doc the content of the DOM instance
     * @param op the operation
     * @return the DOM element corresponding to the normal operation
     */
    private static Element appendNormalOperationElement(Document doc, NormalOperation op) {
        Element opElem = doc.createElement(XmlConstantStrings.NormalOperationElem);
        setTextOperationElement(doc, op, opElem);
        opElem.setAttribute(XmlConstantStrings.ActionAttr, op.getActionType().toString());
        
        return opElem;
    }
    
    /**
     * Creates a DOM element corresponding to a text operation.
     * @param doc the content of the DOM instance
     * @param op the operation
     * @param opElem the DOM element corresponding to the operation
     */
    private static void setTextOperationElement(Document doc, TextOperation op, Element opElem) {
        opElem.setAttribute(XmlConstantStrings.TimeAttr, String.valueOf(op.getTime()));
        opElem.setAttribute(XmlConstantStrings.SeqAttr, String.valueOf(op.getSequenceNumber()));
        opElem.setAttribute(XmlConstantStrings.OffsetAttr, String.valueOf(op.getStart()));
        opElem.setAttribute(XmlConstantStrings.FileAttr, op.getFilePath());
        opElem.setAttribute(XmlConstantStrings.AuthorAttr, op.getAuthor());
        
        Element insElem = doc.createElement(XmlConstantStrings.InsertedElem);
        opElem.appendChild(insElem);
        insElem.appendChild(doc.createTextNode(op.getInsertedText()));
        
        Element delElem = doc.createElement(XmlConstantStrings.DeletedElem);
        opElem.appendChild(delElem);
        delElem.appendChild(doc.createTextNode(op.getDeletedText()));
    }
    
    /**
     * Creates a DOM element corresponding to a compound operation.
     * @param doc the content of the DOM instance
     * @param op the specified operation
     * @return the DOM element corresponding to the operation
     */
    private static Element appendCompoundOperationElement(Document doc, CompoundOperation op) {
        Element opElem = doc.createElement(XmlConstantStrings.CompoundOperationElem);
        opElem.setAttribute(XmlConstantStrings.TimeAttr, String.valueOf(op.getTime()));
        opElem.setAttribute(XmlConstantStrings.LabelAttr, op.getLabel());
        
        for (IOperation o : op.getLeaves()) {
            createOperationsElement(doc, o, opElem);
        }
        
        return opElem;
    }
    
    /**
     * Creates a DOM element corresponding to a copy operation.
     * @param doc the content of the DOM instance
     * @param op the operation
     * @return the DOM element corresponding to the operation
     */
    private static Element appendCopyOperationElement(Document doc, CopyOperation op) {
        Element opElem = doc.createElement(XmlConstantStrings.CopyOperationElem);
        opElem.setAttribute(XmlConstantStrings.TimeAttr, String.valueOf(op.getTime()));
        opElem.setAttribute(XmlConstantStrings.OffsetAttr, String.valueOf(op.getStart()));
        opElem.setAttribute(XmlConstantStrings.FileAttr, String.valueOf(op.getFilePath()));
        opElem.setAttribute(XmlConstantStrings.AuthorAttr, op.getAuthor());
        
        Element copiedElem = doc.createElement(XmlConstantStrings.CopiedElem);
        copiedElem.appendChild(copiedElem);
        copiedElem.appendChild(doc.createTextNode(op.getCopiedText()));
        
        return opElem;
    }
    
    /**
     * Creates a DOM element corresponding to a file operation.
     * @param doc the content of the DOM instance
     * @param op the operation
     * @return the DOM element corresponding to the operation
     */
    private static Element appendFileOperationElement(Document doc, FileOperation op) {
        Element opElem = doc.createElement(XmlConstantStrings.FileOperationElem);
        opElem.setAttribute(XmlConstantStrings.TimeAttr, String.valueOf(op.getTime()));
        opElem.setAttribute(XmlConstantStrings.FileAttr, String.valueOf(op.getFilePath()));
        opElem.setAttribute(XmlConstantStrings.ActionAttr, String.valueOf(op.getActionType().toString()));
        opElem.setAttribute(XmlConstantStrings.AuthorAttr, op.getAuthor());
        
        if (op.getCode() != null) {
            Element codeElem = doc.createElement(XmlConstantStrings.CodeElem);
            opElem.appendChild(codeElem);
            codeElem.appendChild(doc.createTextNode(op.getCode()));
        }
        
        return opElem;
    }
    
    /**
     * Creates a DOM element corresponding to a menu operation.
     * @param doc the content of the DOM instance
     * @param op the operation
     * @return the DOM element corresponding to the operation
     */
    private static Element appendMenuOperationElement(Document doc, MenuOperation op) {
        Element opElem = doc.createElement(XmlConstantStrings.MenuOperationElem);
        opElem.setAttribute(XmlConstantStrings.TimeAttr, String.valueOf(op.getTime()));
        opElem.setAttribute(XmlConstantStrings.FileAttr, String.valueOf(op.getFilePath()));
        opElem.setAttribute(XmlConstantStrings.LabelAttr, String.valueOf(op.getLabel()));
        opElem.setAttribute(XmlConstantStrings.AuthorAttr, op.getAuthor());
        
        return opElem;
    }
    
    /**
     * Creates a DOM element corresponding to a resource operation.
     * @param doc the content of the DOM instance
     * @param op the operation
     * @return the DOM element corresponding to the operation
     */
    private static Element appendResourceOperationElement(Document doc, ResourceOperation op) {
        Element opElem = doc.createElement(XmlConstantStrings.ResourceOperationElem);
        opElem.setAttribute(XmlConstantStrings.TimeAttr, String.valueOf(op.getTime()));
        opElem.setAttribute(XmlConstantStrings.FileAttr, op.getFilePath());
        opElem.setAttribute(XmlConstantStrings.ActionAttr, op.getActionType().toString());
        opElem.setAttribute(XmlConstantStrings.TargetAttr, op.getTarget().toString());
        opElem.setAttribute(XmlConstantStrings.APathAttr, op.getIdenticalPath());
        opElem.setAttribute(XmlConstantStrings.AuthorAttr, op.getAuthor());
        
        return opElem;
    }
}
