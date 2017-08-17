package adv.panels;


import adv.sortingInputHandler.HeapInputDialog;
import adv.main.Window;
import adv.views.BuildHeapView;
import edu.usfca.xj.appkit.gview.object.GElementLabel;
import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.usfca.xj.appkit.gview.object.GElementRect;
import adv.views.View;

public class BuildHeapPanel extends Panel {

    private BuildHeapView buildHeapView;
    private JButton newInput;
    private Font f = new Font(Font.DIALOG,Font.PLAIN,16);
    private HeapInputDialog dialog;
    private final static int SLOW_INDEX = 0;
    private final static int MEDIUM_INDEX = 1;
    private final static int FAST_INDEX = 2;
    private final static int VERY_FAST_INDEX = 3;

    protected String label = null;
    protected GElementRect boundary;
    protected GElementLabel arraySymbol;
    protected GElementLabel treeSymbol;

    public Font symbolFont = new Font(Font.DIALOG, Font.PLAIN,16);

    public BuildHeapPanel(Window window) {
        super(window);

        newInput = new JButton("New Input");
        newInput.setFont(f);
        newInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                restartButton.setEnabled(false);
                displayInputDialog();
            }

        });
        setUpButtons(newInput);

         // add space behind insertField to make all the buttons and field align with left margin.
        addBlank(1100);

        this.add(view = buildHeapView = new BuildHeapView(this));
        setUpAnimationPanel(view);

        //the boundary for differentiate the array and the abstract tree.
        boundary = buildHeapView.createRectangle("",0,56,50000,0.0001);
        boundary.setOutlineColor(Color.LIGHT_GRAY);

        //the labels for showing the two types of diagrams in the screen.
        arraySymbol = buildHeapView.createLabel("Array",40,25);
        arraySymbol.setFont(symbolFont);
        treeSymbol = buildHeapView.createLabel("Abstract Tree", 70,78);
        treeSymbol.setFont(symbolFont);

        dialog = new HeapInputDialog(this);
    }

    private void displayInputDialog() {
        dialog.setVisible(true);
    }

    @Override
    public void enableSpecificButtons() {
        newInput.setEnabled(true);

    }

    @Override
    public void disableSpecificButtons() {
        newInput.setEnabled(false);
    }

    @Override
    protected void setUpRestartButton(Box buttonsContainer) {
        super.setUpRestartButton(buttonsContainer);
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
        buttonsContainer.add(Box.createHorizontalStrut(550));

    }

    @Override
    protected void setUpAnimationRunning(){
        getRunningMsg().setText("Animation Running");
        getRunningMsg().setForeground(Color.GREEN);
        disableSpecificButtons();
    }


    @Override
    protected void endAnimation() {
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

    public void setData(Object data) {
        heapsView.setData(data);
    }

    public Object getData() {
        return heapsView.getData();
    }

    public BuildHeapView getBuildHeapView() {
        return buildHeapView;
    }

    //set the label for displaying the algorithm running information
    public void setLabel(String label){
        setMsgText(label);
    }

}
