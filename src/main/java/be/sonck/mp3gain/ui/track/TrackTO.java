package be.sonck.mp3gain.ui.track;

import java.math.BigDecimal;

import be.sonck.mp3gain.api.model.Clipping;

public class TrackTO {

	private final String name;
	private final String album;
	private final String location;
	private final BigDecimal volume;
	private final BigDecimal gain;
	private final Clipping clipping;
	private final BigDecimal albumVolume;
	private final BigDecimal albumGain;
	private final Clipping albumClipping;
	private final int discNumber;
	private final int trackNumber;
	private final BigDecimal maxNoClipDbGain;
	private final BigDecimal albumMaxNoClipDbGain;
	private final BigDecimal maxNoClipVolume;
	private final BigDecimal albumMaxNoClipVolume;

	public TrackTO(String name, String album, int trackNumber, int discNumber, String location, BigDecimal volume,
			BigDecimal gain, Clipping clipping, BigDecimal maxNoClipDbGain, BigDecimal maxNoClipVolume,
			BigDecimal albumVolume, BigDecimal albumGain, Clipping albumClipping, BigDecimal albumMaxNoClipDbGain,
			BigDecimal albumMaxNoClipVolume) {
		this.name = name;
		this.album = album;
		this.trackNumber = trackNumber;
		this.discNumber = discNumber;
		this.location = location;
		this.volume = volume;
		this.gain = gain;
		this.clipping = clipping;
		this.maxNoClipDbGain = maxNoClipDbGain;
		this.maxNoClipVolume = maxNoClipVolume;
		this.albumVolume = albumVolume;
		this.albumGain = albumGain;
		this.albumClipping = albumClipping;
		this.albumMaxNoClipDbGain = albumMaxNoClipDbGain;
		this.albumMaxNoClipVolume = albumMaxNoClipVolume;
	}

	public BigDecimal getMaxNoClipVolume() {
		return maxNoClipVolume;
	}

	public BigDecimal getAlbumMaxNoClipVolume() {
		return albumMaxNoClipVolume;
	}

	public BigDecimal getMaxNoClipDbGain() {
		return maxNoClipDbGain;
	}

	public BigDecimal getAlbumMaxNoClipDbGain() {
		return albumMaxNoClipDbGain;
	}

	public int getDiscNumber() {
		return discNumber;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public String getName() {
		return name;
	}

	public String getAlbum() {
		return album;
	}

	public String getLocation() {
		return location;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public BigDecimal getGain() {
		return gain;
	}

	public Clipping getClipping() {
		return clipping;
	}

	public BigDecimal getAlbumVolume() {
		return albumVolume;
	}

	public BigDecimal getAlbumGain() {
		return albumGain;
	}

	public Clipping getAlbumClipping() {
		return albumClipping;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!getClass().equals(other.getClass())) {
			return false;
		}

		return this.getLocation().equals(((TrackTO) other).getLocation());
	}

	@Override
	public int hashCode() {
		return getLocation().hashCode();
	}
}
