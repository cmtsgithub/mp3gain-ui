package be.sonck.mp3gain.ui.volume;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.sonck.mp3gain.ui.Constants;
import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.api.VolumeView;
import be.sonck.mp3gain.ui.event.Event;
import be.sonck.mp3gain.ui.event.VolumeChangedEvent;
import be.sonck.mp3gain.ui.swing.EventCreator;
import be.sonck.mp3gain.ui.swing.EventWorker;
import be.sonck.mp3gain.ui.swing.SwingWorkerManager;

import com.google.inject.Inject;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class DefaultVolumeView extends JPanel implements VolumeView {

	private static final int DEFAULT_SLIDER_VALUE = Constants.DEFAULT_VOLUME.multiply(new BigDecimal(10)).intValue();
	private static final BigDecimal TEN = new BigDecimal("10");
	
	private final EventBus eventBus;
	
	private JLabel label;
	private JSlider slider;
	

	@Inject
	public DefaultVolumeView(EventBus eventBus) {
		super(createLayout());
		
		this.eventBus = eventBus;

		add(createSlider(), CC.xy(2, 1));
		add(createLabel(), CC.xy(3, 1));
	}
	
	

	@Override
	public void setEnabled(boolean enabled) {
		slider.setEnabled(enabled);
	}

	private JLabel createLabel() {
		label = new JLabel(toLabelText(Constants.DEFAULT_VOLUME));
		
		label.setFont(getFont().deriveFont(Constants.FONT_SIZE));
		label.setForeground(Color.DARK_GRAY);
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setToolTipText("this is the target volume");
		
		Dimension preferredSize = label.getPreferredSize();
		label.setPreferredSize(new Dimension(preferredSize.width + 10, preferredSize.height));
		
		return label;
	}

	private String toLabelText(BigDecimal bigDecimal) {
		return bigDecimal.toPlainString() + " dB";
	}

	private JSlider createSlider() {
		slider = new JSlider(JSlider.HORIZONTAL, 750, 1000, DEFAULT_SLIDER_VALUE);
		
		slider.addChangeListener(createChangeListener());
		slider.addMouseListener(createMouseListener(slider));
		
		slider.setPreferredSize(new Dimension(500, slider.getPreferredSize().height));
		slider.setToolTipText("set the target volume");
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		
		return slider;
	}

	private ChangeListener createChangeListener() {
		return new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				label.setText(toLabelText(getDecimalValue((JSlider) event.getSource())));
			}
		};
	}

	private MouseAdapter createMouseListener(final JSlider slider) {
		return new MouseAdapter() {
			private BigDecimal currentValue = getDecimalValue(slider);
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					slider.setValue(DEFAULT_SLIDER_VALUE);
					updateValue(Constants.DEFAULT_VOLUME);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				currentValue = getDecimalValue(slider);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				BigDecimal newValue = getDecimalValue(slider);
				if (!newValue.equals(currentValue)) {
					updateValue(newValue);
				}
			}

			private void updateValue(final BigDecimal newValue) {
				currentValue = newValue;
				SwingWorkerManager.getInstance().execute(new EventWorker(eventBus, new EventCreator() {
					@Override
					public Event create() {
						return new VolumeChangedEvent(newValue);
					}
				}));
			}
		};
	}

	private BigDecimal getDecimalValue(JSlider slider) {
		return new BigDecimal(slider.getValue()).divide(TEN, 1, RoundingMode.HALF_UP);
	}
	
	private static LayoutManager createLayout() {
		String columnSpecs = "fill:pref:grow, pref, pref";
		String rowSpecs = "pref";
		
		return new FormLayout(columnSpecs, rowSpecs);
	}
}
