package se.fearless.common.ui.swing.slidertext;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class JTextSlider extends JPanel {
	private final Collection<ChangeListener> listeners = new CopyOnWriteArrayList<>();

	private final JSlider slider;
	private final JTextField textfield;

	private final double minValue;
	private final double maxValue;
	private final double step;

	private final Runnable propagatePendingValue = new Runnable() {
		@Override
		public void run() {
			internalSetValue(pendingValue);
			updateTextField();
			isPending = false;

			notifyListeners();
		}
	};

	private double offset;

	private boolean fromTextChange;
	private boolean updatingText;

	private double pendingValue;
	private boolean isPending;

	public JTextSlider(double minValue, double maxValue, double step, double startValue) {
		super(new BorderLayout());
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;

		double len = maxValue - minValue;
		int count = (int) Math.ceil(len / step);
		slider = new JSlider(JSlider.HORIZONTAL, 0, count, 0);
		slider.setMajorTickSpacing(count);
		slider.setMinorTickSpacing(count / 10);
		slider.setPaintTicks(true);
		add(slider, BorderLayout.CENTER);

		textfield = new JTextField("", 5);
		add(textfield, BorderLayout.EAST);

		internalSetValue(startValue);
		updateTextField();

		slider.addChangeListener(e -> {
			if (fromTextChange) {
				return;
			}
			updateTextField();
			notifyListeners();
		});
		textfield.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				textWasChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textWasChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textWasChanged();
			}
		});
	}

	private String getValueString() {
		return String.format("%.3f", getValue());
	}

	/**
	 * Only called from the Swing thread
	 */
	private void textWasChanged() {
		if (updatingText) {
			return;
		}
		
		String s = textfield.getText();
		try {
			double v = NumberFormat.getInstance().parse(s).doubleValue();
			fromTextChange = true;
			internalSetValue(v);
			fromTextChange = false;

			notifyListeners();
		} catch (ParseException e) {
			// Leave the value as it is until it's valid
		}
	}

	/**
	 * Only called from the Swing thread
	 */
	private void internalSetValue(double v) {
		int sliderValue = (int) ((v - minValue) / step);
		slider.setValue(sliderValue);
		offset = v - getSliderValue();
	}

	private void notifyListeners() {
		ChangeEvent change = new ChangeEvent(this);
		for (ChangeListener listener : listeners) {
			listener.stateChanged(change);
		}
	}

	/**
	 * Only called from the Swing thread
	 */
	private void updateTextField() {
		updatingText = true;
		textfield.setText(getValueString());
		updatingText = false;
	}

	/**
	 * Can be called from any thread, will internally make sure the work is done on the Swing thread.
	 */
	public void setValue(double v) {
		pendingValue = v;
		isPending = true;
		SwingUtilities.invokeLater(propagatePendingValue);
	}

	private double getSliderValue() {
		return minValue + slider.getValue() * step;
	}

	public double getValue() {
		if (isPending) {
			return pendingValue;
		}
		return offset + getSliderValue();
	}

	public void addChangeListener(ChangeListener changeListener) {
		listeners.add(changeListener);
	}
}
