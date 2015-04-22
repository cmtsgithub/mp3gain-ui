package be.sonck.mp3gain.ui.progress;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import be.sonck.mp3gain.ui.api.ProgressPresenter;
import be.sonck.mp3gain.ui.api.ProgressView;

public abstract class AbstractProgressPresenter implements ProgressPresenter {

	private final ProgressView progressView;

	public AbstractProgressPresenter(ProgressView progressView) {
		this.progressView = progressView;
	}
	
	@Override
	public void setProgress(final int value, final String text) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					progressView.setProgress(value, text);
				}
			});
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
