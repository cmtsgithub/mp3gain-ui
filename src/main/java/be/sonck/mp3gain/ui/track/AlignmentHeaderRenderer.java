package be.sonck.mp3gain.ui.track;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class AlignmentHeaderRenderer implements TableCellRenderer {

	private final TableCellRenderer tableCellRenderer;
	private final int horizontalAlignment;

	public AlignmentHeaderRenderer(TableCellRenderer tableCellRenderer, int horizontalAlignment) {
		this.tableCellRenderer = tableCellRenderer;
		this.horizontalAlignment = horizontalAlignment;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) tableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);
		label.setHorizontalAlignment(horizontalAlignment);
		return label;
	}

}
