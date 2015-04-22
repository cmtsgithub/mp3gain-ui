package be.sonck.mp3gain.ui.event;

import java.math.BigDecimal;

public class VolumeChangedEvent implements Event {

	private final BigDecimal volume;

	public VolumeChangedEvent(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getVolume() {
		return volume;
	}
}
