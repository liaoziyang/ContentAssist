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
import org.jtool.changerecorder.util.StringComparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.List;
import java.util.ArrayList;

/**
 * Converts the XML representation into the operation history.
 * @author Katsuhisa Maruyama
 */
public class Xml2Operation {
    
    /**
     * Converts the XML representation of the operation history into the Java object representation.
     * @param doc the content of the DOM instance
     * @return the operation history after the conversion
     */
    public static OperationHistory convert(Document doc) {
        NodeList list = doc.getElementsByTagName(XmlConstantStrings.OperationHistoryElem);
        if (list.getLength() <= 0) {
            System.err.print("invalid operation history format");
            return null;
        }
        
        List<IOperation> history = new ArrayList<IOperation>();
        NodeList operationList = doc.getElementsByTagName(XmlConstantStrings.OperationsElem);
        if (operationList == null) {
            return null;
        }
        
        Node operations = operationList.item(0);
        if (operations == null) {
            return null;
        }
        
        NodeList childOperations = operations.getChildNodes();
        for(int i = 0; i < childOperations.getLength(); i++) {
            Node node = childOperations.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                IOperation op = getOperation(node);
                if (op != null) {
                    history.add(op);
                }
            }
        }
        
        return new OperationHistory(history);
    }
    
    /**
     * Obtains the operation from the DOM element.
     * @param node the DOM element
     * @return the operation
     */
    static IOperation getOperation(Node node) {
        Element elem = (Element)node;
        if (StringComparator.isSame(elem.getNodeName(), XmlConstantStrings.NormalOperationElem)) {
            return getNormalOperation(elem);
            
        } else if (StringComparator.isSame(elem.getNodeName(), XmlConstantStrings.CompoundOperationElem)) {
            return getCompoundOperation(elem);
            
        } else if (StringComparator.isSame(elem.getNodeName(), XmlConstantStrings.FileOperationElem)) {
            return getFileOperations(elem);
            
        } else if (StringComparator.isSame(elem.getNodeName(), XmlConstantStrings.MenuOperationElem)) {
            return getMenuOperation(elem);
            
        } else if (StringComparator.isSame(elem.getNodeName(), XmlConstantStrings.CopyOperationElem)) {
            return getCopyOperation(elem);
            
        } else if (StringComparator.isSame(elem.getNodeName(), XmlConstantStrings.ResourceOperationElem)) {
            return getResourceOperation(elem);
        }
        return null;
    }
    
    /**
     * Creates a normal operation from the DOM element.
     * @param elem the DOM element
     * @return the created operation
     */
    private static NormalOperation getNormalOperation(Element elem) {
        String time = elem.getAttribute(XmlConstantStrings.TimeAttr);
        String seq = elem.getAttribute(XmlConstantStrings.SeqAttr);
        String offset = elem.getAttribute(XmlConstantStrings.OffsetAttr);
        String file = elem.getAttribute(XmlConstantStrings.FileAttr);
        String action = elem.getAttribute(XmlConstantStrings.ActionAttr);
        String author = elem.getAttribute(XmlConstantStrings.AuthorAttr);
        
        String insText = getFirstChildText(elem.getElementsByTagName(XmlConstantStrings.InsertedElem));
        String delText = getFirstChildText(elem.getElementsByTagName(XmlConstantStrings.DeletedElem));
        
        NormalOperation op = new NormalOperation(Long.parseLong(time), Integer.parseInt(seq),
            file, author, Integer.parseInt(offset), insText, delText, NormalOperation.Type.parseType(action));
        return op;
    }
    
    /**
     * Creates a compound operation from the DOM element.
     * @param elem the DOM element
     * @return the created operation
     */
    private static CompoundOperation getCompoundOperation(Element elem) {
        List<IOperation> ops = new ArrayList<IOperation>();
        String time = elem.getAttribute(XmlConstantStrings.TimeAttr);
        String label = elem.getAttribute(XmlConstantStrings.LabelAttr);
        
        NodeList childList = elem.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                ops.add(getOperation((Element)childList.item(i)));
            }
        }
        
        CompoundOperation op = new CompoundOperation(Long.parseLong(time), ops, label);
        return op;
    }
    
    /**
     * Creates a copy operation from the DOM element.
     * @param elem the DOM element
     * @return the created operation
     */
    private static CopyOperation getCopyOperation(Element elem) {
        String time = elem.getAttribute(XmlConstantStrings.TimeAttr);
        String offset = elem.getAttribute(XmlConstantStrings.OffsetAttr);
        String file = elem.getAttribute(XmlConstantStrings.FileAttr);
        String author = elem.getAttribute(XmlConstantStrings.AuthorAttr);
        
        String copiedText = getFirstChildText(elem.getElementsByTagName(XmlConstantStrings.CopiedElem));
        
        CopyOperation op = new CopyOperation(Long.parseLong(time),
            file, author, Integer.parseInt(offset), copiedText);
        return op;
    }
    
    /**
     * Creates a file operation from the DOM element.
     * @param elem the DOM element
     * @return the created operation
     */
    private static FileOperation getFileOperations(Element elem) {
        String time = elem.getAttribute(XmlConstantStrings.TimeAttr);
        String file = elem.getAttribute(XmlConstantStrings.FileAttr);
        String action = elem.getAttribute(XmlConstantStrings.ActionAttr);
        String author = elem.getAttribute(XmlConstantStrings.AuthorAttr);
        
        String code = getFirstChildText(elem.getElementsByTagName(XmlConstantStrings.CodeElem));
        if (code == null) {
            code = "";
        }
        
        FileOperation op = new FileOperation(Long.parseLong(time),
            file, author, FileOperation.Type.parseType(action), code);
        return op;
    }
    
    /**
     * Creates a menu operation from the DOM element.
     * @param elem the DOM element
     * @return the created operation
     */
    private static MenuOperation getMenuOperation(Element elem) {
        String time = elem.getAttribute(XmlConstantStrings.TimeAttr);
        String file = elem.getAttribute(XmlConstantStrings.FileAttr);
        String label = elem.getAttribute(XmlConstantStrings.LabelAttr);
        String author = elem.getAttribute(XmlConstantStrings.AuthorAttr);
        
        MenuOperation op = new MenuOperation(Long.parseLong(time), file, author, label);
        return op;
    }
    
    /**
     * Creates the resource change operation from the DOM element.
     * @param elem the DOM element
     * @return the created operation
     */
    private static IOperation getResourceOperation(Element elem) {
        String time = elem.getAttribute(XmlConstantStrings.TimeAttr);
        String file = elem.getAttribute(XmlConstantStrings.FileAttr);
        String action = elem.getAttribute(XmlConstantStrings.ActionAttr);
        String target = elem.getAttribute(XmlConstantStrings.TargetAttr);
        String apath = elem.getAttribute(XmlConstantStrings.APathAttr);
        String author = elem.getAttribute(XmlConstantStrings.AuthorAttr);
        
        ResourceOperation.Type actionValue = ResourceOperation.Type.parseType(action);
        ResourceOperation.Target targetValue = ResourceOperation.Target.parseType(target);
        ResourceOperation op = new ResourceOperation(Long.parseLong(time),
            file, author, actionValue, targetValue, apath);
        return op;
    }
    
    /**
     * Obtains the text of stored in the first child of a given node list.
     * @param nodes the node list of nodes store the text
     * @return the text string, <code>null</code> if no text was found
     */
    private static String getFirstChildText(NodeList nodes) {
        if (nodes == null || nodes.getLength() == 0) {
            return null;
        }
        return getFirstChildText(nodes.item(0));
    }
    
    /**
     * Obtains the text stored in the first child of a given node.
     * @param node the node that stores the text
     * @return the text string, <code>null</code> if no text was found
     */
    private static String getFirstChildText(Node node) {
        if (node == null) {
            return null;
        }
        
        NodeList nodes = node.getChildNodes();
        if (nodes == null || nodes.getLength() == 0) {
            return null;
        }
        
        Node child = nodes.item(0);
        if (child.getNodeType() == Node.TEXT_NODE) {
            return ((Text)child).getNodeValue();
        }
        return null;
    }
}
