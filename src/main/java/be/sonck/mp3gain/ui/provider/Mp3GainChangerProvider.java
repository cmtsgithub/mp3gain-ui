package be.sonck.mp3gain.ui.provider;

import be.sonck.mp3gain.api.service.Mp3GainChanger;
import be.sonck.mp3gain.api.service.Mp3GainChangerFactory;

import com.google.inject.Provider;

public class Mp3GainChangerProvider implements Provider<Mp3GainChanger> {

	@Override
	public Mp3GainChanger get() {
		return Mp3GainChangerFactory.create();
	}
}
