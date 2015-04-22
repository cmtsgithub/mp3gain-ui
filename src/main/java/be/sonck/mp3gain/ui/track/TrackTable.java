package be.sonck.mp3gain.ui.track;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import be.sonck.mp3gain.ui.Constants;

final class TrackTable extends JTable {
	TrackTable(TableModel tableModel) {
		super(tableModel);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setFont(getFont().deriveFont(Constants.FONT_SIZE));

		customizeColumns();
		addToolTips();
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return AlignmentRenderer.getRenderer(TrackTableAdapter.COLUMNS[column].getHorizontalAlignment());
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int colIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, colIndex);
		c.setBackground(determineBackgroundColor(rowIndex, colIndex));
		return c;
	}

	private Color determineBackgroundColor(int rowIndex, int colIndex) {
		if (isCellSelected(rowIndex, colIndex)) {
			return Constants.SELECTION_COLOR;
			
		} else if (isClipped(rowIndex, colIndex)) {
			return Constants.ERROR_ROW_COLOR;

		} else if (rowIndex % 2 == 0) {
			return Constants.ALTERNATE_ROW_COLOR;

		} else {
			// If not shaded, match the table's background
			return getBackground();
		}
	}

	private boolean isClipped(int rowIndex, int colIndex) {
		return "Y".equals(getValueAt(rowIndex, colIndex));
	}
	
//	private boolean isClipped(int rowIndex) {
//		return "Y".equals(getValueAt(rowIndex, 8));
//	}

	private void addToolTips() {
		ColumnHeaderToolTips tips = new ColumnHeaderToolTips();

		for (int i = 0; i < getColumnCount(); i++) {
			tips.setToolTip(getColumnModel().getColumn(i), TrackTableAdapter.COLUMNS[i].getDescription());
		}

		getTableHeader().addMouseMotionListener(tips);
	}

	private void customizeColumns() {
		TableCellRenderer headerRenderer = getTableHeader().getDefaultRenderer();

		for (int i = 0; i < TrackTableAdapter.COLUMNS.length; i++) {
			TableColumn tableColumn = getColumnModel().getColumn(i);
			tableColumn.setHeaderRenderer(new AlignmentHeaderRenderer(headerRenderer, TrackTableAdapter.COLUMNS[i]
					.getHorizontalAlignment()));

			int preferredWidth = TrackTableAdapter.COLUMNS[i].getPreferredWidth();
			if (preferredWidth > 0) {
				tableColumn.setPreferredWidth(preferredWidth);
			}
		}
	}
}