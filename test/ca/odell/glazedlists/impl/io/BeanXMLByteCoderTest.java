/**
 * Glazed Lists
 * http://glazedlists.dev.java.net/
 *
 * COPYRIGHT 2003 O'DELL ENGINEERING LTD.
 */
package ca.odell.glazedlists.impl.io;

import java.util.*;
import javax.swing.JLabel;
import java.io.*;
// Glazed Lists in bytes
import ca.odell.glazedlists.io.*;
// for being a JUnit test case
import junit.framework.*;
import ca.odell.glazedlists.*;

/**
 * Tests the BeanXMLByteCoder..
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class BeanXMLByteCoderTest extends TestCase {
    
    /**
     * Prepare for the test.
     */
    public void setUp() {
    }

    /**
     * Clean up after the test.
     */
    public void tearDown() {
    }

    /**
     * Tests that the XML encoding works.
     */
    public void testCoding() throws IOException {
        Bufferlo data = new Bufferlo();
        
        JLabel bean = new JLabel();
        bean.setText("Limp Bizkit");
        bean.setToolTipText("Fred Durst");
        bean.setEnabled(false);
        
        ByteCoder beanXMLByteCoder = new BeanXMLByteCoder();
        beanXMLByteCoder.encode(bean, data.getOutputStream());
        JLabel beanCopy = (JLabel)beanXMLByteCoder.decode(data.getInputStream());
        
        assertEquals(bean.getText(), beanCopy.getText());
        assertEquals(bean.getToolTipText(), beanCopy.getToolTipText());
        assertEquals(bean.isEnabled(), beanCopy.isEnabled());
    }
}
