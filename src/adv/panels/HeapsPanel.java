package adv.panels;


import adv.utility.InputConstraints;
import adv.main.Window;
import adv.views.HeapsView;
import edu.usfca.ds.shapes.DSShapeRect;
import edu.usfca.xj.appkit.gview.object.GElementLabel;
import edu.usfca.xj.appkit.utils.XJAlert;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.usfca.xj.appkit.gview.object.GElementRect;
import adv.views.View;

public class HeapsPanel extends Panel implements DocumentListener, ActionListener {

    private HeapsView heapsView;
    private JLabel insertLabel;
    private JTextField insertField;
    private Document insertFieldDocument;
    private JLabel userInputMessage;
    private JButton maxElementButton;
    private JButton removeMaxButton;
    private JButton isEmptyButton;
    private Font f = new Font(Font.DIALOG,Font.PLAIN,16);


    protected String label = null;
    private int nonEmptyField;
    private String nonEmptyFieldInput;

    private static final int INSERT = 1;
    private static final int DELETE = 2;


    private final static int SLOW_INDEX = 0;
    private final static int MEDIUM_INDEX = 1;
    private final static int FAST_INDEX = 2;
    private final static int VERY_FAST_INDEX = 3;

    protected GElementRect boundary;
    protected GElementLabel arraySymbol;
    protected GElementLabel treeSymbol;
    public Font symbolFont = new Font(Font.DIALOG, Font.PLAIN,16);

    public HeapsPanel(Window window) {
        super(window);

        setUpInsertField();
        setUpMaxElementButton();
        setUpRemoveMaxButton();
        setUpIsEmptyButton();

        // add space behind "isEmpty" button to make all the buttons and field align with left margin.
        addBlank(670);

        userInputMessage = new JLabel("");
        userInputMessage.setForeground(Color.RED);

        this.add(view = heapsView = new HeapsView(this));
        setUpAnimationPanel(view);

        //the boundary for differentiate the array and the abstract tree.
        boundary = heapsView.createRectangle("",0,56,50000,0.0001);
        boundary.setOutlineColor(Color.LIGHT_GRAY);

        //the labels for showing the two types of diagrams in the screen.
        arraySymbol = heapsView.createLabel("Array",40,25);
        arraySymbol.setFont(symbolFont);
        treeSymbol = heapsView.createLabel("Abstract Tree", 70,78);
        treeSymbol.setFont(symbolFont);
    }

    @Override
    protected void setUpGoButton(Box buttonsContainer) {
        goButton = new JButton("Go");
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if(!running) {
                    if (!userInputValid(nonEmptyFieldInput) || nonEmptyField < 0) {
                        showInvalidInputDialog();
                        return;
                    }
                }
                if (!running) {
                    beginAnimation(nonEmptyField, nonEmptyFieldInput);
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
        buttonsContainer.add(goButton);
        goButton.setMaximumSize(new Dimension(84,33));
        goButton.setFont(buttonFont);
    }
    @Override
    public void enableSpecificButtons() {
        insertField.setEnabled(true);
        maxElementButton.setEnabled(true);
        removeMaxButton.setEnabled(true);
        isEmptyButton.setEnabled(true);

    }

    @Override
    public void disableSpecificButtons() {
        insertField.setEnabled(false);
        maxElementButton.setEnabled(false);
        removeMaxButton.setEnabled(false);
        isEmptyButton.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextField inputField = ((JTextField) e.getSource());
        if (userInputValid(nonEmptyFieldInput) && nonEmptyField > 0) {
            goButton.requestFocusInWindow();
            goButton.doClick();
        } else {
            showInvalidInputDialog();
        }
    }


    @Override
    protected void setUpSkipButton(Box buttonsContainer) {
        skipButton = new JButton("Skip");
        skipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!running) {
                    beginAnimation(nonEmptyField,nonEmptyFieldInput);
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
        skipButton.setMaximumSize(new Dimension(84,33));
        buttonsContainer.add(skipButton);
        skipButton.setFont(buttonFont);
    }

    @Override
    protected void setUpRestartButton(Box buttonsContainer) {
        super.setUpRestartButton(buttonsContainer);
        buttonsContainer.remove(restartButton);
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
        buttonsContainer.add(Box.createHorizontalStrut(620));

    }

    @Override
    protected void setUpAnimationRunning(){
        getRunningMsg().setText("Animation Running");
        getRunningMsg().setForeground(Color.GREEN);
        disableSpecificButtons();
    }




    private void showInvalidInputDialog() {
        XJAlert.display(window.getJavaContainer(), "Invalid Input", "Please enter a number.\n\n" + "Note: The number can't be greater than " + InputConstraints.MAX_INPUT_VALUE);
    }

    private boolean userInputValid(String nonEmptyFieldInput) {
        return InputConstraints.isValidNumber(nonEmptyFieldInput);
    }


    @Override
    protected void endAnimation() {
        clearAllFields();
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


    public void setUpInsertField(){

        Dimension labelSize = new Dimension(50, 50);
        Dimension fieldSize = new Dimension(80, 50);

        insertLabel = new JLabel("InsertItem: ");
        setUpLabels(insertLabel);           // add insertLabel to upContainer
        insertLabel.setFont(f);
        insertField = new JTextField("");
        setTextField(insertField);          // add insertField to upContainer
        insertField.addActionListener(this);
        insertFieldDocument = insertField.getDocument();
        insertFieldDocument.addDocumentListener(this);
        insertField.setMaximumSize(fieldSize);
    }

    public void setUpMaxElementButton(){
        maxElementButton = new JButton("maxElement");
        maxElementButton.setFont(f);
        maxElementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setLabel("");
                heapsView.highlightMaxElement();
                changeDone();
            }
        });
        maxElementButton.setEnabled(true);
        setUpButtons(maxElementButton);
    }

    public void setUpRemoveMaxButton(){

        removeMaxButton = new JButton("removeMax");
        removeMaxButton.setFont(f);
        removeMaxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                if (!running) {
                    Thread v = new Thread() {
                        public void run() {
                            goButton.setEnabled(true);
                            startAnimation();
                            heapsView.removeMax();
                            endAnimation();
                            repaint();
                        }
                    };
                    v.start();
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
        removeMaxButton.setEnabled(true);
        setUpButtons(removeMaxButton);
    }

    public void setUpIsEmptyButton(){
        isEmptyButton = new JButton("isEmpty");
        isEmptyButton.setFont(f);
        isEmptyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setLabel("");
                heapsView.showIsEmpty();
                changeDone();
            }
        });
        setUpButtons(isEmptyButton);
    }

    // Persistence

    public void setData(Object data) {
        heapsView.setData(data);
    }

    public Object getData() {
        return heapsView.getData();
    }

    @Override
    public void insertUpdate(DocumentEvent docEvent) {
        Document doc = docEvent.getDocument();
        disableOtherFields(doc);
        setNonEmptyField(doc);
        try {
            String currentFieldInput = doc.getText(0, doc.getLength());
            nonEmptyFieldInput = currentFieldInput;
            if (currentFieldInput.length() > 0) {
                enableGoAndSkip();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void removeUpdate(DocumentEvent docEvent) {

        Document doc = docEvent.getDocument();

        try {
            String currentFieldInput = doc.getText(0, doc.getLength());
            nonEmptyFieldInput = currentFieldInput;
            if (currentFieldInput.length() == 0) {
                enableSpecificButtons();
                nonEmptyField = -1;
                disableGoAndSkip();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //Does nor fire any of these events
    }

    // Disable the textfield other than the
    // one whose document we are working with
    private void disableOtherFields(Document doc) {
        if (!doc.equals(insertFieldDocument)) {
            insertField.setEnabled(false);
        }


    }


    private void clearAllFields() {
        insertField.setText("");

    }

    private void setNonEmptyField(Document doc) {

        if (doc.equals(insertFieldDocument)) {
            nonEmptyField = INSERT;
        }
    }


    //set the label for displaying the algorithm running information
    public void setLabel(String label){

        setMsgText(label);

    }

}
