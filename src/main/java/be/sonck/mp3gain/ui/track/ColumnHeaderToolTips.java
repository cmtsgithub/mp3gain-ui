package be.sonck.mp3gain.ui.track;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ColumnHeaderToolTips extends MouseMotionAdapter {

	// Current column whose tooltip is being displayed.
	// This variable is used to minimize the calls to setToolTipText().
	private TableColumn currentColumn;

	// Maps TableColumn objects to tooltips
	private final Map<TableColumn, String> tips = new HashMap<TableColumn, String>();

	// If tooltip is null, removes any tooltip text.
	public void setToolTip(TableColumn col, String tooltip) {
		if (tooltip == null) {
			tips.remove(col);
		} else {
			tips.put(col, tooltip);
		}
	}

	public void mouseMoved(MouseEvent evt) {
		TableColumn column = null;
		JTableHeader header = (JTableHeader) evt.getSource();
		TableColumnModel columnModel = header.getTable().getColumnModel();
		int vColIndex = columnModel.getColumnIndexAtX(evt.getX());

		// Return if not clicked on any column header
		if (vColIndex >= 0) {
			column = columnModel.getColumn(vColIndex);
		}

		if (column != currentColumn) {
			header.setToolTipText((String) tips.get(column));
			currentColumn = column;
		}
	}
}