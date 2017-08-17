package adv.panels;

import adv.main.Window;
import adv.views.DirectedGraphView;

import adv.views.View;
import edu.usfca.vas.window.tools.DesignToolsDG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;


public abstract class DirectedGraphPanel extends Panel {

	protected DirectedGraphView graphView;
	protected DesignToolsDG designToolFA;


	public DirectedGraphPanel(Window window) {
		super(window);
		upContainer.add(designToolFA = new DesignToolsDG(this.window));
		addBlank(1019);
	}

	public void setLabel(String label){

		setMsgText(label);

	}


}


