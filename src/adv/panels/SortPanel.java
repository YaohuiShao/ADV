package adv.panels;

import adv.main.Window;
import adv.sortingInputHandler.SortInputDialog;
import adv.views.SortView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import edu.usfca.xj.appkit.gview.GView;
import edu.usfca.xj.appkit.gview.utils.*;

public abstract class SortPanel extends Panel {

	protected boolean showBars;
	protected boolean showIndices;
	protected SortView sortView;
	protected Window window;
	protected JButton hideBars;
	protected JButton hideIndices;
	protected JButton startAnimation;
	protected JButton newInput;
	protected SortInputDialog dialog;
	protected JButton zoomIn;
	protected JButton zoomOut;
	protected int runFlag;

	private final static int SLOW_INDEX = 0;
	private final static int MEDIUM_INDEX = 1;
	private final static int FAST_INDEX = 2;
	private final static int VERY_FAST_INDEX = 3;


	public SortPanel(Window window) {
		super(window);
		// this.window = window; unneccssary line!!


		showBars = true;
		showIndices = true;

		newInput = new JButton("New Input");
		newInput.setFont(f);
		newInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				restartButton.setEnabled(false);
				displayInputDialog();
			}

		});
		setUpButtons(newInput);

		dialog = new SortInputDialog(this);

		hideBars = new JButton("Hide Bars");
		hideBars.setFont(f);
		hideBars.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				showBars = !showBars;
				sortView.toggleShowBars();

				if (showBars) {
					hideBars.setText("Hide Bars");
				} else {
					hideBars.setText("View Bars");
				}

				changeDone();
			}
		});
		setUpButtons(hideBars);

		hideIndices = new JButton("Hide Indices");
		hideIndices.setFont(f);
		hideIndices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				showIndices = !showIndices;
				sortView.toggleShowIndices();
				if (showIndices) {
					hideIndices.setText("Hide Indices");
				} else {
					hideIndices.setText("View Indices");
				}
				changeDone();
			}
		});
		setUpButtons(hideIndices);
		addBlank(890);



	}


	private void displayInputDialog() {
		dialog.setVisible(true);
		// JOptionPane.showMessageDialog(window.getJFrame(), "Set Input");

	}

	public void disableSpecificButtons() {
		hideBars.setEnabled(false);
		hideIndices.setEnabled(false);
		newInput.setEnabled(false);

	}

	public void enableSpecificButtons() {
		hideBars.setEnabled(true);
		hideIndices.setEnabled(true);
		newInput.setEnabled(true);
	}

	// Persistence
	public void setData(Object data) {
		sortView.setData(data);
	}

	public Object getData() {
		return sortView.getData();
	}

	public SortView getSortView() {
		return sortView;
	}


	public void setLabel(String label){

		setMsgText(label);

	}

	@Override
	protected void setUpSpeedButton(Box buttonsContainer){
		String[] speedStrings = { "Slow", "Medium", "Fast", "Very Fast" };
		JComboBox<String> speed = new JComboBox<String>(speedStrings);
		speed.setMaximumSize(new Dimension(105, 33)); // set "speed" button the same size of other buttons
		speed.setFont(buttonFont);
		setAnimationSpeeds();
		speed.addActionListener(new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent event) {
										JComboBox<String> speed = (JComboBox<String>) event.getSource();
										switch (speed.getSelectedIndex()) {
											case SLOW_INDEX:
												view.setDelay(slowValue);
												break;
											case MEDIUM_INDEX:
												view.setDelay(mediumValue);
												break;
											case FAST_INDEX:
												view.setDelay(fastValue);
												break;
											case VERY_FAST_INDEX:
												view.setDelay(veryFastValue);
												break;
										}
									}
								}

		);
		speed.setSelectedItem(speedStrings[MEDIUM_INDEX]);
		buttonsContainer.add(speed);
		buttonsContainer.add(Box.createHorizontalStrut(520));

	}
}
