/**
 * Glazed Lists
 * http://glazedlists.dev.java.net/
 *
 * COPYRIGHT 2003 O'DELL ENGINEERING LTD.
 */
package ca.odell.glazedlists.io;

import java.util.*;
// for access to volatile classes
import ca.odell.glazedlists.impl.*;
import ca.odell.glazedlists.impl.io.*;
// implemented interfaces
import ca.odell.glazedlists.io.ByteCoder;


/**
 * A factory for creating all sorts of objects to be used with Glazed Lists.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
public final class GlazedListsIO {

    /**
     * A dummy constructor to prevent instantiation of this class
     */
    private GlazedListsIO() {
        throw new UnsupportedOperationException();
    }


    // ByteCoders // // // // // // // // // // // // // // // // // // // // //

    /** Provide Singleton access for all ByteCoders with no internal state */
    private static ByteCoder serializableByteCoder = new SerializableByteCoder();
    private static ByteCoder beanXMLByteCoder = new BeanXMLByteCoder();

    /**
     * Creates a {@link ByteCoder} that encodes {@link java.io.Serializable Serializable}
     * Objects using an {@link java.io.ObjectOutputStream}.
     */
    public static ByteCoder serializableByteCoder() {
        if(serializableByteCoder == null) serializableByteCoder = new SerializableByteCoder();
        return serializableByteCoder;
    }

    /**
     * Creates a {@link ByteCoder} that uses {@link java.beans.XMLEncoder XMLEncoder} and
     * {@link java.beans.XMLDecoder XMLDecoder} classes from java.beans. Encoded
     * Objects must be JavaBeans.
     */
    public static ByteCoder beanXMLByteCoder() {
        if(beanXMLByteCoder == null) beanXMLByteCoder = new BeanXMLByteCoder();
        return beanXMLByteCoder;
    }
}