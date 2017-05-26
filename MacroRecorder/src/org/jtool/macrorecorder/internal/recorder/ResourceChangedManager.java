/*
 *  Copyright 2014
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.macrorecorder.internal.recorder;

import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.jtool.macrorecorder.macro.ResourceMacro;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages operations related to element changes.
 * @author Katsuhisa Maruyama
 */
public class ResourceChangedManager implements IElementChangedListener {
    
    /**
     * A recorder that records menu actions.
     */
    private MenuMacroRecorder recorder;
    
    /**
     * Creates an object that records resource change events.
     * @param recorder a recorder that records menu actions
     */
    public ResourceChangedManager(MenuMacroRecorder recorder) {
        this.recorder = recorder;
    }
    
    /**
     * Registers an element change manager with the Java model.
     * @param em the element change manager
     */
    public static void register(ResourceChangedManager em) {
        JavaCore.addElementChangedListener(em);
    }
    
    /**
     * Unregisters an element change manager with the Java model.
     * @param em the element change manager
     */
    public static void unregister(ResourceChangedManager em) {
        JavaCore.removeElementChangedListener(em);
    }
    
    /**
     * Receives an event when one or more Java elements have changed. 
     * @param event the change event
     */
    @Override
    public void elementChanged(ElementChangedEvent event) {
        long time = Time.getCurrentTime();
        ChangeCollector collector = new ChangeCollector(event.getDelta());
        
        for (IJavaElementDelta delta : collector.deltas) {
            if (delta.getKind() == IJavaElementDelta.REMOVED) {
                ResourceMacro macro = createResourceRemovedMacro(time, delta);
                if (macro != null) {
                    recorder.recordResourceMacro(macro);
                }
            }
        }
        
        for (IJavaElementDelta delta : collector.deltas) {
            if (delta.getKind() == IJavaElementDelta.ADDED) {
                ResourceMacro macro = createResourceAddedMacro(time, delta);
                if (macro != null) {
                    recorder.recordResourceMacro(macro);
                }
            }
        }
        
        for (IJavaElementDelta delta : collector.deltas) {
            if (delta.getKind() == IJavaElementDelta.CHANGED) {
                ResourceMacro macro = createResourceChangedMacro(time, delta);
                if (macro != null) {
                    recorder.recordResourceMacro(macro);
                }
            }
        }
    }
    
    /**
     * Creates a macro corresponding to the removed delta of the change.
     * @param time the time when the change occurred
     * @param delta the removed delta of the change
     * @return the created resource macro
     */
    private ResourceMacro createResourceRemovedMacro(long time, IJavaElementDelta delta) {
        IJavaElement elem = delta.getElement();
        String path = elem.getPath().toString();
        if (path == null) {
            return null;
        }
        
        String type = "Removed";
        if ((delta.getFlags() & IJavaElementDelta.F_MOVED_TO) != 0) {
            if (isRenamed(delta.getElement(), delta.getMovedToElement())) {
                type = "RenamedTo";
            } else {
                type = "MovedTo";
            }
        }
        
        return new ResourceMacro(time, type, path, elem);
    }
    
    /**
     * Creates a macro corresponding to the added delta of the change.
     * @param time the time when the change occurred
     * @param delta the added delta of the change
     * @return the created resource macro
     */
    private ResourceMacro createResourceAddedMacro(long time, IJavaElementDelta delta) {
        IJavaElement elem = delta.getElement();
        String path = elem.getPath().toString();
        if (path == null) {
            return null;
        }
        
        String type = "Added";
        if ((delta.getFlags() & IJavaElementDelta.F_MOVED_FROM) != 0) {
            if (isRenamed(delta.getElement(), delta.getMovedFromElement())) {
                type = "RenamedFrom";
            } else {
                type = "MovedFrom";
            }
        }
        
        return new ResourceMacro(time, type, path, elem);
    }
    
    /**
     * Creates a macro corresponding to the changed delta of the change.
     * @param time the time when the change occurred
     * @param delta the changed delta of the change
     * @return the created resource macro
     */
    private ResourceMacro createResourceChangedMacro(long time, IJavaElementDelta delta) {
        IJavaElement elem = delta.getElement();
        String path = elem.getPath().toString();
        if (path == null) {
            return null;
        }
        
        if ((delta.getFlags() & IJavaElementDelta.F_CONTENT) != 0) {
            // System.out.println("CONTENT CHANGED" + path);
            return null;
        }
        
        return new ResourceMacro(time, "Changed", path, elem);
    }
    
    /**
     * Tests if the element was renamed by the change.
     * @param before the element before the change
     * @param after the element after the change
     * @return <code>true</code> if renaming was applied, otherwise <code>false</code>
     */
    private boolean isRenamed(IJavaElement before, IJavaElement after) {
        if (before == null || after == null) {
            return true;
        }
        
        String beforen = ResourceMacro.getName(before);
        String aftern =  ResourceMacro.getName(after);
        if (beforen.compareTo(aftern) != 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Collects change deltas.
     */
    class ChangeCollector {
        
        /**
         * The collection of change deltas
         */
        List<IJavaElementDelta> deltas = new ArrayList<IJavaElementDelta>();
        
        /**
         * Creates an object that collects the deltas of element changes.
         * @param delta the root delta of the change
         */
        ChangeCollector(IJavaElementDelta delta) {
            collectDeltas(delta);
        }
        
        /**
         * Collects all the deltas of the changes.
         * @param delta the root delta of the change
         * @param deltas the collection of the deltas to be collected
         */
        private void collectDeltas(IJavaElementDelta delta) {
            if (delta.getKind() == IJavaElementDelta.ADDED ||
                delta.getKind() == IJavaElementDelta.REMOVED) {
                if (!contain(delta)) {
                    deltas.add(delta);
                }
            } else if (delta.getKind() == IJavaElementDelta.CHANGED &&
                    ((delta.getFlags() & IJavaElementDelta.F_CONTENT) != 0)) {
                if (!contain(delta)) {
                    deltas.add(delta);
                }
            }
            
            for (IJavaElementDelta d : delta.getAffectedChildren()) {
                collectDeltas(d);
            }
        }
        
        /**
         * Tests if a given change delta was already contained in the change collection.
         * @param delta the change delta
         * @return <code>true</code> if the change delta was contained in the change collection, otherwise <code> false</code>
         */
        private boolean contain(IJavaElementDelta delta) {
            String path = delta.getElement().getPath().toString();
            for (IJavaElementDelta d : deltas) {
                String p = d.getElement().getPath().toString();
                if (p.compareTo(path) == 0 && d.getKind() == delta.getKind()) {
                    return true;
                }
            }
            return false;
        }
    }
}
