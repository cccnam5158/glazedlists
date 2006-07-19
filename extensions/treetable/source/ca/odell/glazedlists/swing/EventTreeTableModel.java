/* Glazed Lists                                                 (c) 2003-2006 */
/* http://publicobject.com/glazedlists/                      publicobject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.swing;

import ca.odell.glazedlists.*;
import ca.odell.glazedlists.impl.swing.SwingThreadProxyEventList;
import ca.odell.glazedlists.gui.*;
import ca.odell.glazedlists.event.*;
import javax.swing.table.*;

public class EventTreeTableModel<E> extends AbstractTableModel implements ListEventListener<E>, TreeTableModel {

    /** the proxy moves events to the Swing Event Dispatch thread */
    private final TransformedList<E,E> swingThreadSource;

    private final EventTableModel<E> tableModel;

    private final TreeFormat<E> treeFormat;

    /** Reusable table event for broadcasting changes */
    private final MutableTableModelEvent tableModelEvent = new MutableTableModelEvent(this);

    /**
     * Creates a new table that renders the specified list in the specified
     * format.
     */
    public EventTreeTableModel(EventList<E> source, TableFormat<E> tableFormat, TreeFormat<E> treeFormat) {
        // lock the source list for reading since we want to prevent writes
        // from occurring until we fully initialize this EventTableModel
        source.getReadWriteLock().readLock().lock();
        try {
            if (source instanceof SwingThreadProxyEventList)
                this.swingThreadSource = (SwingThreadProxyEventList<E>) source;
            else
                this.swingThreadSource = GlazedListsSwing.swingThreadProxyList(source);

            this.tableModel = new EventTableModel<E>(this.swingThreadSource, tableFormat);
            this.treeFormat = treeFormat;

            // prepare listeners
            swingThreadSource.addListEventListener(this);
        } finally {
            source.getReadWriteLock().readLock().unlock();
        }
    }

    /**
     * Returns the height of the row object at the given <code>rowIndex</code>
     * within the tree represented by this {@link TreeTableModel}.
     */
    public int getDepth(int rowIndex) {
        swingThreadSource.getReadWriteLock().readLock().lock();
        try {
            return TreeTableSupport.getDepth(treeFormat, swingThreadSource.get(rowIndex));
        } finally {
            swingThreadSource.getReadWriteLock().readLock().unlock();
        }
    }

    /**
     * For implementing the ListEventListener interface. This sends changes
     * to the table which repaint the table cells. Because this class is backed
     * by {@link GlazedListsSwing#swingThreadProxyList}, all natural calls to
     * this method are guaranteed to occur on the Swing EDT.
     */
    public void listChanged(ListEvent<E> listChanges) {

    }

    /** @inheritDoc */
    public TableFormat<E> getTableFormat() {
        return tableModel.getTableFormat();
    }

    /** @inheritDoc */
    public void setTableFormat(TableFormat<E> tableFormat) {
        tableModel.setTableFormat(tableFormat);
    }

    /** @inheritDoc */
    public E getElementAt(int index) {
        return tableModel.getElementAt(index);
    }

    /** @inheritDoc */
    public String getColumnName(int column) {
        return tableModel.getColumnName(column);
    }

    /** @inheritDoc */
    public int getRowCount() {
        return tableModel.getRowCount();
    }

    /** @inheritDoc */
    public int getColumnCount() {
        return tableModel.getColumnCount();
    }

    /** @inheritDoc */
	public Class getColumnClass(int columnIndex) {
		return tableModel.getColumnClass(columnIndex);
	}

    /** @inheritDoc */
    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }

    /** @inheritDoc */
    public boolean isCellEditable(int row, int column) {
        return tableModel.isCellEditable(row, column);
    }

    /** @inheritDoc */
    public void setValueAt(Object editedValue, int row, int column) {
        tableModel.setValueAt(editedValue, row, column);
    }

    /** @inheritDoc */
    public void dispose() {
        tableModel.dispose();
    }
}