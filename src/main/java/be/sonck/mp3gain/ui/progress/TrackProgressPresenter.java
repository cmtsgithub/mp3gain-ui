package be.sonck.mp3gain.ui.progress;

import be.sonck.mp3gain.ui.api.ProgressView;

import com.google.inject.Inject;

public class TrackProgressPresenter extends AbstractProgressPresenter {

	@Inject
	public TrackProgressPresenter(@TrackProgress ProgressView progressView) {
		super(progressView);
	}
}
