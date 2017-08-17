package adv.panels;

import adv.main.Window;
import adv.views.View;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.JLabel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import adv.views.HeapsView;

import com.sun.codemodel.internal.*;
import edu.usfca.xj.appkit.gview.GView;

public abstract class Panel extends JPanel {

	protected Window window;
	protected JButton restartButton;
	protected JButton stepButton;
	protected JButton goButton;
	protected JButton skipButton;
	protected JButton zoomIn;
	protected JButton zoomOut;
	protected JButton selectAllButton;
	protected JTextField line;
	protected JComboBox<String> speed;
	private JLabel runningMsg = new JLabel("Animation Completed");
	public JLabel messageText;
	protected boolean paused;
	protected boolean running;
	protected View view;
	protected Vector DirtyDisplay;
	protected int runFlag = 0;
	Box upContainer = Box.createHorizontalBox();
	GView gView = new GView();
	HeapsView heapsView;

	private final static int SLOW_INDEX = 0;
	private final static int MEDIUM_INDEX = 1;
	private final static int FAST_INDEX = 2;
	private final static int VERY_FAST_INDEX = 3;

	// zoom factors index
	private final static int zoomOut25_INDEX = 0;
	private final static int zoomOut50_INDEX = 1;
	private final static int zoomOut75_INDEX = 2;
	private final static int zoom100_INDEX = 3;
	private final static int zoomIn120_INDEX = 4;
	private final static int zoomIn140_INDEX = 5;
	private final static int zoomIn160_INDEX = 6;
	private final static int zoomIn200_INDEX = 7;

	protected static int slowValue;
    protected static int mediumValue;
    protected static int fastValue;
    protected static int veryFastValue;
    protected static Font f = new Font(Font.DIALOG, Font.PLAIN, 14);
	protected static Font labelFont = new Font(Font.DIALOG, Font.PLAIN, 18);
	protected static Font buttonFont = new Font(Font.DIALOG, Font.PLAIN, 16);


	public Panel(Window window) {
		super(new BorderLayout());
		DirtyDisplay = new Vector();
		this.window = window;
	}

	protected void animate(final int Function, final Object param1, final Object param2) {

		Thread v = new Thread() {
			public void run() {
				startAnimation();
				view.Animate(Function, param1, param2);
				endAnimation();
				repaint();
			}
		};
		v.start();
	}

	protected void beginAnimation(final int Function, final Object param) {

		Thread v = new Thread() {
			public void run() {
				startAnimation();
				view.Animate(Function, param);
				endAnimation();
				repaint();
			}
		};
		v.start();
	}

	protected void beginAnimation() {

		Thread v = new Thread() {
			public void run() {
				startAnimation();
				view.Animate();
				endAnimation();
				repaint();
			}
		};
		v.start();
	}


	protected void beginAnimation2() {

		Thread v = new Thread() {
			public void run() {
				startAnimation();
				heapsView.runAlgorithm2();
				endAnimation();
				repaint();
			}
		};
		v.start();
	}

	protected void setUpAnimationPanel(final View view) {

		Box upPanel = Box.createVerticalBox();
		Box labelContainer = Box.createHorizontalBox();
		Box bottomContainer = Box.createHorizontalBox();

		//labelContainer is for putting runningMsg and messageText
		//set the attributes of messageText and runningMsg
		messageText = new JLabel("Message Text");
		messageText.setFont(labelFont);
		messageText.setMaximumSize(new Dimension(650, 50));
		labelContainer.add(Box.createHorizontalStrut(7));
		labelContainer.add(messageText);
		labelContainer.add(Box.createHorizontalStrut(500));
		getRunningMsg().setMaximumSize(new Dimension(450, 50));
		setRunningMsg(new JLabel("Animation Completed"));
		labelContainer.add(getRunningMsg());
		labelContainer.add(Box.createHorizontalStrut(28));
		getRunningMsg().setFont(labelFont);

		// upPanel is for putting upButtons and labels


		JLabel la = new JLabel(" ");
		la.setMaximumSize(new Dimension(2000,10));
		Box lc = Box.createHorizontalBox();
		lc.add(la);
		lc.add(Box.createHorizontalStrut(800));
		upPanel.add(upContainer);
		upPanel.add(lc);
		upPanel.add(labelContainer);
		labelContainer.setOpaque(true);
		labelContainer.setBackground(Color.DARK_GRAY);
		getRunningMsg().setForeground(Color.WHITE);
		messageText.setForeground(Color.WHITE);


		this.add(upPanel, BorderLayout.NORTH);

		bottomContainer.add(Box.createHorizontalStrut(5)); // leave space ahead of "go" button for legibility
		setUpGoButton(bottomContainer);
		setUpStepButton(bottomContainer);
		setUpSkipButton(bottomContainer);
		setUpRestartButton(bottomContainer);
		setUpSpeedButton(bottomContainer);
		setUpZoomInButton(bottomContainer);
		setUpZoomOutButton(bottomContainer);
		setUpZoomButton(bottomContainer);
		setUpSelectAllButton(bottomContainer);

		this.add(bottomContainer, BorderLayout.SOUTH);
		disableGoAndSkip();
	}


	protected void setAnimationSpeeds() {
		slowValue = 75;
		mediumValue = 50;
		fastValue = 15;
		veryFastValue = 5;
	}


	protected void setUpGoButton(Box buttonsContainer) {
		goButton = new JButton("Go");

		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				if (!running) {
					beginAnimation();
					goButton.setText("Pause");
				} else {

					if (!paused) {
						paused = true;
						view.pause();
						goButton.setText("Go");
						setUpAnimationPaused();
					} else {
						paused = false;
						view.go();
						goButton.setText("Pause");
						setUpAnimationRunning();
					}
				}
			}
		});
		goButton.setFont(buttonFont);
		goButton.setMaximumSize(new Dimension(84,33));
		buttonsContainer.add(goButton);

	}

	protected void setUpSkipButton(Box buttonsContainer) {
		skipButton = new JButton("Skip");
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!running) {
					beginAnimation();
					view.skip();
				} else {
					if (!paused) {
						view.skip();
					} else {
						view.skip();
						paused = false;
						view.go();
					}

				}
				changeDone();
			}
		});
		buttonsContainer.add(skipButton);
		skipButton.setMaximumSize(new Dimension(84,33));
		skipButton.setFont(buttonFont);

	}

	private void setUpStepButton(Box buttonsContainer) {
		stepButton = new JButton("Step");
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				view.step();
				changeDone();
			}
		});
		stepButton.setEnabled(false);
		stepButton.setFont(buttonFont);
		stepButton.setMaximumSize(new Dimension(84,33));
		buttonsContainer.add(stepButton);

	}

	protected void setUpRestartButton(Box buttonsContainer) {
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				view.go();
				paused = false;
				view.restart();
				restartButton.setEnabled(false);
				beginAnimation();
				goButton.setEnabled(true);
				goButton.setText("Pause");
				changeDone();
			}
		});
		restartButton.setEnabled(false);
		restartButton.setMaximumSize(new Dimension(84,33));
		buttonsContainer.add(restartButton);
		restartButton.setFont(buttonFont);
	}


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
		buttonsContainer.add(Box.createHorizontalStrut(470));

	}


	protected void setUpZoomInButton(Box buttonsContainer){
		zoomIn = new JButton("+");
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(view.getRootElement()!=null) {
					view.zoomIn();
					changeDone();
				}
			}
		});
		buttonsContainer.add(zoomIn);
		zoomIn.setFont(buttonFont);
	}

	protected void setUpZoomOutButton(Box buttonsContainer){
		zoomOut = new JButton("-");
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(view.getRootElement()!=null) {
					view.zoomOut();
					changeDone();
				}
			}
		});
		buttonsContainer.add(zoomOut);
		zoomOut.setFont(buttonFont);
	}


	protected void setUpZoomButton(Box buttonsContainer){
		// zoom control bar for selecting specific zooming factor
		String[] zoomStrings = { "25%", "50%", "75%", "100%","120%","140%","160%","200%" };
		JComboBox<String> zoom = new JComboBox<String>(zoomStrings);
		zoom.setMaximumSize(new Dimension(80, 33)); // set "zoom" button the same size of other buttons
		zoom.setFont(buttonFont);
		zoom.addActionListener(new ActionListener() {

								   @Override
								   public void actionPerformed(ActionEvent event) {
									   if(view.getRootElement()!=null) {
										   JComboBox<String> zoom = (JComboBox<String>) event.getSource();
										   switch (zoom.getSelectedIndex()) {
											   case zoomOut25_INDEX:
												   view.setZoomFactor(0.25f);
												   break;
											   case zoomOut50_INDEX:
												   view.setZoomFactor(0.5f);
												   break;
											   case zoomOut75_INDEX:
												   view.setZoomFactor(0.75f);
												   break;
											   case zoom100_INDEX:
												   view.setZoomFactor(1f);
												   break;
											   case zoomIn120_INDEX:
												   view.setZoomFactor(1.2f);
												   break;
											   case zoomIn140_INDEX:
												   view.setZoomFactor(1.4f);
												   break;
											   case zoomIn160_INDEX:
												   view.setZoomFactor(1.6f);
												   break;
											   case zoomIn200_INDEX:
												   view.setZoomFactor(2f);
												   break;
										   }
									   }
								   }
							   }
		);
		zoom.setSelectedItem(zoomStrings[zoom100_INDEX]);
		buttonsContainer.add(zoom);
		buttonsContainer.add(Box.createHorizontalStrut(5));
	}

	// the button for selecting all elements in the graph
	protected void setUpSelectAllButton(Box buttonsContainer){
		selectAllButton = new JButton("√ All");
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//if(selectAllButton.getText() == "√ All"){
				view.sltAllElements();
				changeDone();
			}
		});
		buttonsContainer.add(selectAllButton);
//		buttonsContainer.add(Box.createHorizontalStrut(10));
		selectAllButton.setFont(buttonFont);
	}

	public void disableSpecificButtons() {
		/* Override this method to disable specific buttons */
	}

	public void startAnimation() {

		running = true;
		if (paused) {
			setUpAnimationPaused();
		} else {
			setUpAnimationRunning();
		}
		skipButton.setEnabled(true);
		disableSpecificButtons();
	}

	protected void setUpAnimationPaused() {
		setRunFlag(1);
		getRunningMsg().setText("Animation Paused");
		getRunningMsg().setForeground(Color.RED);
		stepButton.setEnabled(true);
	}

	protected void setUpAnimationRunning() {
		setRunFlag(2);
		getRunningMsg().setText("Animation Running");
		getRunningMsg().setForeground(Color.GREEN);
		stepButton.setEnabled(false);
	}

	protected int extractInt(String text, int digits) {
		int extracted;

		try {
			extracted = Integer.parseInt(extractString(text, digits));
		} catch (Exception e) {
			extracted = Integer.MAX_VALUE;
		}
		return extracted;
	}

	protected String extractString(String val, int maxsize) {
		if (val.length() <= maxsize)
			return val;
		return val.substring(0, maxsize);
	}

	public void enableSpecificButtons() {
		/* Override this method to enable specific buttons */
	}

	protected void endAnimation() {
		setRunFlag(0);
		running = false;
		getRunningMsg().setText("Animation Completed");
		getRunningMsg().setForeground(Color.WHITE);
		stepButton.setEnabled(false);
		skipButton.setEnabled(false);
		restartButton.setEnabled(true);
		goButton.setText("Go");
		goButton.setEnabled(false);
		enableSpecificButtons();
	}

	// This method mark the document as dirty (the Save menu item is enabled)
	public void changeDone() {
		window.getDocument().changeDone();
	}

	public BufferedImage getImage() {
		return view.getImage();
	}

	public String getEPS() {
		return view.getEPS();
	}

	public void disableGoAndSkip() {
		goButton.setEnabled(false);
		skipButton.setEnabled(false);
	}

	public void enableGoAndSkip() {
		goButton.setEnabled(true);
		skipButton.setEnabled(true);
	}

	public void enableRestartButton() {
		restartButton.setEnabled(true);
	}

	public void disableRestartButton(){
		restartButton.setEnabled(false);
	}

	public Window getWindow() {
		return window;
	}

	public int getRunFlag() {
		return runFlag;
	}

	public void setRunFlag(int runFlag) {
		this.runFlag = runFlag;
	}
	/*public String getRunningMsg(){
		String runMsg = new String();
		switch(getRunFlag()) {
			case 0:
				runMsg = "Animation Completed";
			case 1:
				runMsg =  "Animation Paused";
			case 2:
				runMsg =  "Animation Running";
		}
		return runMsg;
	}*/

	// For adding buttons for algs(tree and sort) on top of the window
	public void setUpButtons(JButton button){
		upContainer.add(button);
	}

	// For adding labels for algs on top of the window
	public void setUpLabels(JLabel label){
		upContainer.add(label);

	}

	// to make upContainer align with left margin
	public void addBlank(int blank){
		upContainer.add(Box.createHorizontalStrut(blank));
	}

	// For adding text field for algs on top of the window
	public void setTextField(JTextField textField){
		upContainer.add(textField);
		upContainer.add(Box.createHorizontalStrut(5));
	}

	// For setting the content of messageText
	public  void setMsgText(String label){

		messageText.setText(label);
		messageText.update(messageText.getGraphics());
		messageText.setFont(labelFont);
	}


	public JLabel getRunningMsg() {
		return runningMsg;
	}

	public void setRunningMsg(JLabel runningMsg) {
		this.runningMsg = runningMsg;
	}



}
