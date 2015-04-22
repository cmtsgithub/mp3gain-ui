package be.sonck.mp3gain.ui.progress;

import javax.swing.JProgressBar;

import be.sonck.mp3gain.ui.Constants;
import be.sonck.mp3gain.ui.api.ProgressView;

public class DefaultProgressView extends JProgressBar implements ProgressView {

	public DefaultProgressView() {
		setFont(getFont().deriveFont(Constants.FONT_SIZE));
	}
	
	@Override
	public void setProgress(int value, String text) {
		if (text == null) {
			setStringPainted(false);
		} else {
			setString(text);
			setStringPainted(true);
		}
		
		setValue(value);
	}
}
