/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Manages the XML file stream.
 * @author Katsuhisa Maruyama
 */
public class XmlFileStream {
    
    /**
     * Reads and returns the DOM instance created from the contents of an XML file.
     * @param path the full path indicating the XML file to be read
     * @return the DOM instance
     */
    public static Document read(final String path) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            builder.setErrorHandler(new ErrorHandler() {
                
                public void error(SAXParseException e) throws SAXException {
                    printException(path, e);
                }
                
                public void fatalError(SAXParseException e) throws SAXException {
                    printException(path, e);
                }
                
                public void warning(SAXParseException e) throws SAXException {
                    printException(path, e);
                }
            });
            
            File file = new File(path);
            return builder.parse(file);
            
        } catch (Exception e) {
            System.err.println("DOM: Parse error occurred: " + e.getMessage() + ".");
        }
        return null;
    }
    
    /**
     * Prints errors during paring the contents of the XML file. 
     * @param path the full path indicating the XML file to be read
     * @param e the occurred exception
     */
    private static void printException(String path, SAXParseException e) {
        System.err.println("[FATAL]file:" + path + "line:" + e.getLineNumber() + ", " +
          "column:" + e.getColumnNumber() + ", " + e.getMessage());
    }
    
    /**
     * 
     * Writes the content of the DOM instance into an XML file.
     * @param doc the content of the DOM instance to be written
     * @param path the full path indicating the file which the contents are written into
     * @param file the input file
     */
    public static void write(Document doc, String path, IFile file) {
        try {
            String encoding;
            if (file == null) {
                encoding = Charset.defaultCharset().name();
            } else {
                encoding = file.getCharset();
            }
            
            write(doc, path, encoding);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * Writes the content of the DOM instance into an XML file.
     * @param doc the content of the DOM instance to be written
     * @param path the full path indicating the file which the contents are written into
     * @param the encoding of texts in the DOM instance
     */
    public static void write(Document doc, String path, String encoding) {
        path.replace('/', File.separatorChar);
        
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            // transformer.setOutputProperty(OutputKeys.ENCODING, "Shift_JIS");
            // transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource src = new DOMSource(doc);
            
            StringWriter writer = new StringWriter();
            transformer.transform(src, new StreamResult(writer));
            
            FileStream.write(path, writer.toString());
        } catch (TransformerException e) {
            System.out.println("DOM: Write error occurred: " + e.getMessage() + ".");
        }
    }
}
