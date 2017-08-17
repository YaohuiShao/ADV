package adv.views;

import adv.panels.BubbleSortPanel;

public class BubbleSortView extends SortView {


	public BubbleSortView(BubbleSortPanel panel) {
		this.panel = panel;
	}

	public void bubbleSort() {
		for (int i = 0; i < currentNumElements - 1; i++) {
			for (int j = 0; j < currentNumElements - 1 - i; j++) {
				if (greaterThan(j, j+1))
					swap(j + 1, j);
			}
		}

	}

	public void runAlgorithm() {
		bubbleSort();
		panel.setLabel("Elements sorted.");
	}

}
