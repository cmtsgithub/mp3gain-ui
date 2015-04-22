package be.sonck.mp3gain.ui.progress;

import be.sonck.mp3gain.ui.api.ProgressView;

import com.google.inject.Inject;

public class MainProgressPresenter extends AbstractProgressPresenter {

	@Inject
	public MainProgressPresenter(@MainProgress ProgressView progressView) {
		super(progressView);
	}
}
