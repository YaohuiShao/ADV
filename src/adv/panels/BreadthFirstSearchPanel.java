package adv.panels;

import adv.main.Window;
import adv.views.BreadthFirstSearchView;

import java.awt.*;

public class BreadthFirstSearchPanel extends DirectedGraphPanel {

    public BreadthFirstSearchPanel(Window window) {
        super(window);
        this.add(view = graphView = new BreadthFirstSearchView(this), BorderLayout.CENTER);
        graphView.setDesignToolsPanel(this.designToolFA);
        setUpAnimationPanel(view);
    }

    public void setLabel(String label){

        setMsgText(label);

    }
}
