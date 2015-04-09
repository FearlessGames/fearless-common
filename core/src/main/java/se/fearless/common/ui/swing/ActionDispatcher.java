package se.fearless.common.ui.swing;

import se.fearless.common.ui.Action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionDispatcher implements ActionListener {
	private Action action;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (action != null) {
			action.act();
		}
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
