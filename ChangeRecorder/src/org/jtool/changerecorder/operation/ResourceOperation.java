/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.changerecorder.operation;

import org.jtool.changerecorder.util.StringComparator;
import org.jtool.changerecorder.util.Time;

/**
 * Stores information on an operation related to the resource (project and package) changes.
 * @author Katsuhisa Maruyama
 */
public class ResourceOperation extends AbstractOperation {
    
    /**
     * Defines the type of a resource change.
     */
    public enum Type {
        
        /**
         * The type of the resource change performed by this operation.
         */
        ADDED, REMOVED, MOVED_FROM, MOVED_TO, RENAMED_FROM, RENAMED_TO, OTHERS;
        
        /**
         * Obtains the type of the resource change performed by this operation.
         * @param str the type string indicating the resource change
         * @return the type of the resource change
         */
        public static ResourceOperation.Type parseType(String str) {
            if (StringComparator.isSameIgnoreCase(str, "added")) {
                return ADDED;
            } else if (StringComparator.isSameIgnoreCase(str, "removed")) {
                return REMOVED;
            } else if (StringComparator.isSameIgnoreCase(str, "moved_from")) {
                return MOVED_FROM;
            } else if (StringComparator.isSameIgnoreCase(str, "moved_to")) {
                return MOVED_TO;
            } else if (StringComparator.isSameIgnoreCase(str, "renamed_from")) {
                return RENAMED_FROM;
            } else if (StringComparator.isSameIgnoreCase(str, "renamed_to")) {
                return RENAMED_TO;
            }
            
            return OTHERS;
        }
    }
    
    /**
     * Defines the target of a resource change.
     */
    public enum Target {
        
        /**
         * The target of the resource change performed by this operation.
         */
        JPROJECT, JPACKAGE, JFILE, OTHERS;
       
        /**
         * Obtains the target of the resource change performed by this operation.
         * @param str the target string indicating the resource change
         * @return the target of the resource change
         */
        public static ResourceOperation.Target parseType(String str) {
            if (StringComparator.isSameIgnoreCase(str, "jproject")) {
                return JPROJECT;
            } else if (StringComparator.isSameIgnoreCase(str, "jpackage")) {
                return JPACKAGE;
            } else if (StringComparator.isSameIgnoreCase(str, "jfile")) {
                return JFILE;
            }
            
            return OTHERS;
        }
    }
    
    /**
     * The type of the resource change performed by this operation.
     */
    protected Type actionType;
    
    /**
     * The target of the resource change performed by this operation.
     */
    protected Target target;
    
    /**
     * The path of a resource identical to the resource changed by this macro.
     */
    private String ipath;
    
    /**
     * Creates an instance that stores an operation related to resource change.
     * @param time the time when the operation was performed
     * @param path the file related to the changed resource
     * @param author the author's name
     * @param atype the type of the resource change
     * @param target the target of the resource change
     * @param ipath the path of a resource identical to the changed resource
     */
    public ResourceOperation(long time, String path, String author, Type atype, Target target, String ipath) {
        super(time, path, author);
        this.actionType = atype;
        this.target = target;
        this.ipath = ipath;
    }
    
    /**
     * Creates an instance that stores an operation related to resource change.
     * @param time the time when the operation was performed
     * @param path the file related to the changed resource, which is usually <code>null</code> 
     * @param ctype the type of the resource change
     * @param target the target of the resource change
     * @param ipath the path of a resource identical to the changed resource
     */
    public ResourceOperation(long time, String path, Type ctype, Target target, String ipath) {
        this(time, path, AbstractOperation.getUserName(), ctype, target, ipath);
    }
    
    /**
     * Returns the type of the resource change performed by this operation
     * @return the type of the resource change
     */
    public Type getActionType() {
        return actionType;
    }
    
    /**
     * Returns the target of the resource change performed by this operation
     * @return the target of the resource change
     */
    public Target getTarget() {
        return target;
    }
    
    /**
     * Returns the path of a resource moved/renamed from or to.
     * @return the path of the identical resource
     */
    public String getIdenticalPath() {
        return ipath;
    }
    
    /**
     * Returns the sort of this operation.
     * @return the string indicating the operation sort
     */
    @Override
    public IOperation.Type getOperationType() {
        return IOperation.Type.RESOURCE;
    }
    
    /**
     * Tests if this operation is the same as a given one.
     * @param op the given operation
     * @return <code>true</code> if the two operations are the same, otherwise <code>false</code>
     */
    @Override
    public boolean equals(IOperation op) {
        if (!(op instanceof ResourceOperation)) {
            return false;
        }
        
        ResourceOperation rop = (ResourceOperation)op;
        return super.equals(rop) &&
               actionType == rop.getActionType() && target == rop.getTarget() &&
               StringComparator.isSame(ipath, rop.getIdenticalPath());
    }
    
    /**
     * Obtains information on this operation.
     * @return the string storing the information on this operation
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(Time.toUsefulFormat(time));
        buf.append(" " + actionType.name());
        buf.append(" author=[" + author + "]");
        buf.append(" target=" + target.name());
        buf.append(" path=" + path);
        buf.append(" ipath=" + ipath);
        
        return buf.toString();
    }
}
