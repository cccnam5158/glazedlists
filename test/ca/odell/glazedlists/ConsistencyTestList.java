/**
 * Glazed Lists
 * http://glazedlists.dev.java.net/
 *
 * COPYRIGHT 2003 O'DELL ENGINEERING LTD.
 */
package ca.odell.glazedlists;

// Java collections are used for underlying data storage
import java.util.*;
// the Glazed Lists
import ca.odell.glazedlists.event.*;
// for being a JUnit test case
import junit.framework.*;

/**
 * A very basic list that ensures that lists are kept consistent and that
 * the change events are consistent.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
public class ConsistencyTestList implements ListEventListener {
    
    /** a second copy of the list data */
    private List expected;

    /** a name for reporting problems with the list */
    private String name;

    /** the source list to compare against */
    private EventList source;

    /** whether to cough out changes to the console as they happen */
    private boolean verbose = false;

    /**
     * Creates a new ConsistencyTestList that ensures events from the source
     * list are consistent.
     *
     * @param verbose whether to print changes to the console as they happne
     */
    public ConsistencyTestList(EventList source, String name, boolean verbose) {
        this.source = source;
        this.name = name;
        this.verbose = verbose;
        
        // populate the list of expected values
        expected = new ArrayList();
        for(int i = 0; i < source.size(); i++) {
            expected.add(source.get(i));
        }
    }

    /**
     * Creates a new ConsistencyTestList that ensures events from the source
     * list are consistent.
     */
    public ConsistencyTestList(EventList source, String name) {
        this(source, name, false);
    }

    /**
     * For implementing the ListEventListener interface.
     */
    public void listChanged(ListEvent listChanges) {
        // print the changes if necessary
        if(verbose) System.out.println(name + ": " + listChanges);
        
        // record the changed indices
        List changedIndices = new ArrayList();
        
        // keep track of the highest change index so far
        int highestChangeIndex = 0;
        
        // for all changes, one index at a time
        while(listChanges.next()) {
            
            // get the current change info
            int changeIndex = listChanges.getIndex();
            int changeType = listChanges.getType();
            
            // save this index for validation later
            changedIndices.add(new Integer(changeIndex));
            
            // make sure the change indicies are positive and not descreasing
            Assert.assertTrue(changeIndex >= 0);
            Assert.assertTrue(changeIndex >= highestChangeIndex);
            highestChangeIndex = changeIndex;
                
            // verify the index is small enough, and adjust the size
            if(changeType == ListEvent.INSERT) {
                expected.add(changeIndex, source.get(changeIndex));
            } else if(changeType == ListEvent.DELETE) {
                expected.remove(changeIndex);
            } else if(changeType == ListEvent.UPDATE) {
                expected.set(changeIndex, source.get(changeIndex));
            }
        }
        
        // verify the source is consistent with what we expect
        Assert.assertEquals(expected.size(), source.size());
        for(Iterator c = changedIndices.iterator(); c.hasNext(); ) {
            int changeIndex = ((Integer)c.next()).intValue();
            for(int i = Math.max(changeIndex - 1, 0); i < Math.min(changeIndex+2, expected.size()); i++) {
                Assert.assertEquals(expected.get(i), source.get(i));
            }
        }
    }
}