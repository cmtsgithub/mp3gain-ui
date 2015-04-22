package be.sonck.mp3gain.ui.track;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class AlignmentRenderer extends DefaultTableCellRenderer {
	
	private static final AlignmentRenderer LEFT_RENDERER = new AlignmentRenderer(SwingConstants.LEFT);
	private static final AlignmentRenderer CENTER_RENDERER = new AlignmentRenderer(SwingConstants.CENTER);
	private static final AlignmentRenderer RIGHT_RENDERER = new AlignmentRenderer(SwingConstants.RIGHT);
	
	public static AlignmentRenderer getRenderer(int alignment) {
		switch (alignment) {
			case SwingConstants.LEFT: return LEFT_RENDERER;
			case SwingConstants.CENTER: return CENTER_RENDERER;
			default: return RIGHT_RENDERER;
		}
	}
	
	private final int alignment;

	private AlignmentRenderer(int alignment) {
		this.alignment = alignment;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setHorizontalAlignment(alignment);
		return label;
	}
}
