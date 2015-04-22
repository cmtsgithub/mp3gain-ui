package be.sonck.mp3gain.ui.track;

public class Column {

	private final String columnName;
	private final int preferredWidth;
	private final int horizontalAlignment;
	private final String description;

	public Column(String columnName, int preferredWidth, int horizontalAlignment, String description) {
		this.columnName = columnName;
		this.preferredWidth = preferredWidth;
		this.horizontalAlignment = horizontalAlignment;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getPreferredWidth() {
		return preferredWidth;
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}
}
