package adv.views;


import adv.avlTreeModel.AVLTreeVertex;
import adv.utility.ColorConstants;
import adv.utility.InputConstraints;
import adv.panels.*;
import edu.usfca.ds.shapes.DSShapeLink;
import edu.usfca.ds.shapes.DSShapeRect;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GElementRect;
import edu.usfca.xj.appkit.gview.object.*;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class BuildHeapView extends View {

    protected final static int STARTING_X_COORD = 400;
    protected final static int STARTING_Y_COORD = 30;

    // Algorithmic operations
    private static final int INSERT = 1;
    private static final int DELETE = 2;
    private static final int REMOVE = 3;


    // The width/height difference between two levels in the tree for insertion

    private static final int WIDTH_DELTA = 50;
    private static final int HEIGHT_DELTA = 100;
    private static final int INSERTION_X_POSITION = 200;
    private static final int INSERTION_Y_POSITION = 100;

    //Length for a square leaf vertex
    private static final int LEAF_LENGTH = 20;
    private static final int STEPS = 40;
    Font font = new Font(Font.DIALOG, Font.PLAIN, 12);

    AVLTreeVertex root;
    private GElementCircle highlightCircle;
    private GElementCircle highlightCircle2;

    protected int[] arrayLocationValue = new int[30];
    protected GElement arrayLocationTextValue[] = new GElement[30];
    protected GElement arrayLocationFrame[] = new GElement[30];
    protected GElement arrayLocationIndex[] = new GElement[30];
    protected int arrayLocationValueCopy[];
    protected int currentNumElements;

    protected int[] Xpos = new int[30];
    protected int[] Ypos = new int[30];
    private int arrayIndexFlag;
    private int treeIndexFlag;
    protected GElementLabel keyLabel[] = new GElementLabel[30];
    protected AVLTreeVertex arrayVertex[] = new AVLTreeVertex[30];
    Font symbolFont = new Font(Font.DIALOG, Font.PLAIN, 16);

    protected GElementRect boundary;
    protected GElementLabel arraySymbol;
    protected GElementLabel treeSymbol;

    protected BuildHeapPanel panel;

    public BuildHeapView() {
        super();
    }

    public BuildHeapView(BuildHeapPanel panel) {
        super();
        this.panel = panel;

    }


    public void runAlgorithm() {
        runBuildHeap();
    }



    private void runBuildHeap() {
        panel.setLabel("Now begin to build Heap.");
        repaintwait();
        buildHeap();
    }


    //buildHeap algorithm from INF2B
    private void buildHeap(){
        int n = currentNumElements;
        int m = (n - 2) / 2;
        while(m >= 0) {
            heapify(arrayVertex[m], m);
            m--;
        }
        panel.setLabel("BuildHeap finished.");

    }

    //heapify algorithm from INF2B.
    private void heapify(AVLTreeVertex vertex, int top){
        panel.setLabel("Do heapify for "+ vertex.key+".");
        setHighlightCircle2AtVertex(vertex);
        repaintwait();
        if(vertex == root) {
            root.graphicVertex.setOutlineColor(Color.BLACK);
        }else{
            removeHighlightCircle2();
        }
        if(vertex.hasTwoLeafChildren()){
            panel.setLabel("Finish heapify for this element.");
        }else {
            int left = top * 2 + 1;
            int right = top * 2 + 2;

            // when rightChild is a leafVertex
            if (vertex.rightChild.isLeafVertex()) {
                setHighlightCircleAtVertex(arrayVertex[left]);
                setHighlightCircle2AtVertex(arrayVertex[top]);
                if (arrayLocationValue[left] > arrayLocationValue[top]) {
                    panel.setLabel(arrayLocationValue[left] + " > " + arrayLocationValue[top] + ", swap elements.");
                    repaintwait();
                    removeHighlightCircle();
                    if (arrayVertex[top] == root) {
                        arrayVertex[top].graphicVertex.setOutlineColor(Color.BLACK);
                    } else {
                        removeHighlightCircle2();
                    }
                    swapItems(vertex, top, left);
                } else {
                    panel.setLabel(arrayLocationValue[left] + " < = " + arrayLocationValue[top] + ". No swap required.");
                    repaintwait();
                    removeHighlightCircle();
                    if (arrayVertex[top] == root) {
                        arrayVertex[top].graphicVertex.setOutlineColor(Color.BLACK);
                    } else {
                        removeHighlightCircle2();
                    }
                    panel.setLabel("Finish heapify for this element.");
                }
            }

            //when the leftChild and rightChild are all internal vertices
            else {
                int max = arrayLocationValue[top];
                int largeIndex = top;
                panel.setLabel("Compare " + arrayLocationValue[top] + " and " + arrayLocationValue[left] + ".");
                setHighlightCircleAtVertex(arrayVertex[left]);
                setHighlightCircle2AtVertex(arrayVertex[top]);
                repaintwait();

                //if parent.left is an internal vertex and parent.left.key > parent.key, then max <- parent.left.key
                if (arrayLocationValue[left] > arrayLocationValue[top]) {
                    panel.setLabel(arrayLocationValue[left] + " > " + arrayLocationValue[top] +
                            ", the max element is " + arrayLocationValue[left] + ".");
                    max = arrayLocationValue[left];
                    largeIndex = left;
                } else {
                    panel.setLabel(arrayLocationValue[left] + " < = " + arrayLocationValue[top] +
                            ", the max element is " + arrayLocationValue[top] + ".");
                }
                repaintwait();
                removeHighlightCircle();
                if (arrayVertex[top] == root) {
                    arrayVertex[top].graphicVertex.setOutlineColor(Color.BLACK);
                } else {
                    removeHighlightCircle2();
                }
                repaintwait();

                panel.setLabel("Compare " + arrayLocationValue[right] + " and max Element " + max + ".");
                setHighlightCircleAtVertex(arrayVertex[right]);
                setHighlightCircle2AtVertex(arrayVertex[largeIndex]);
                repaintwait();
                int tem = largeIndex;

                //if parent.right is an internal vertex and parent.right.key > max, then max <- parent.right.key.
                if (arrayLocationValue[right] > max) {
                    panel.setLabel(arrayLocationValue[right] + " > " + max +
                            ", the max element is " + arrayLocationValue[right] + ".");
                    max = arrayLocationValue[right];
                    largeIndex = right;
                    repaintwait();
                } else {
                    panel.setLabel(arrayLocationValue[right] + " < = " + max +
                            ", the max element is " + max + ".");
                    repaintwait();
                }
                removeHighlightCircle();
                if (tem == 0) {
                    arrayVertex[top].graphicVertex.setOutlineColor(Color.BLACK);
                } else {
                    removeHighlightCircle2();
                }
                repaintwait();

                //if parent.key != max then exchange parent and max element.
                if (arrayLocationValue[top] != max) {
                    panel.setLabel("The parent is not the max element.");
                    repaintwait();
                    panel.setLabel("Swap the max element " + max + " and parent " + arrayLocationValue[top] + ".");
                    repaintwait();
                    swapItems(vertex, top, largeIndex);

                } else {
                    panel.setLabel("The parent is the max element. No swap required");
                    repaintwait();
                    panel.setLabel("Finish heapify for this element.");
                    repaintwait();
                }
            }
        }
    }

    private void swapItems(AVLTreeVertex vertex, int top, int largeIndex){

        //swap items in tree and array.
        setHighlightCircleAtVertex(arrayVertex[top]);
        setHighlightCircle2AtVertex(arrayVertex[largeIndex]);
        repaintwait();

        swapVertex(vertex,arrayVertex[largeIndex], top, largeIndex);
        panel.setLabel("Swap elements in array.");
        swap(top, largeIndex);                   //swap the two items in the array
        repaintwait();
        heapify(arrayVertex[largeIndex],largeIndex);
        panel.setLabel("Finish heapify for this element.");
        repaintwait();

    }

    // Heap insertion algorithm from INF2B course notes.
    private AVLTreeVertex insertIntoTree(int vertexToInsert) {

        //panel.setLabel("Insert vertex " + vertexToInsert + " at leaf location.");
        AVLTreeVertex newVertex = AVLTreeVertex.newVertex(vertexToInsert);
        setVertexAtAbsolutePosition(newVertex, INSERTION_X_POSITION, INSERTION_Y_POSITION);

        AVLTreeVertex leafVertexToReplace = findInsertionLocation(vertexToInsert);
        positionVertex(newVertex, leafVertexToReplace);
        replaceLeafVertex(newVertex, leafVertexToReplace);
        setVertexLabel(newVertex);

        //set a label contains key value at the positon of current vertex.
        //This label is for swapping.
        keyLabel[getTreeIndexFlag()] = createLabel(String.valueOf(vertexToInsert),newVertex.getPosition());
        GElement newVt = newVertex.graphicVertex;
        newVt.setLabelVisible(false);

        arrayVertex[getTreeIndexFlag()] = newVertex;
        repaint();

        return newVertex;
    }

    private AVLTreeVertex findInsertionLocation(int k) {
        if (root == null) {
            return null;
        } else {
            AVLTreeVertex u = root;
            while (!u.isLeafVertex()) {
                if(u.leftChild.isLeafVertex()){
                    u = u.leftChild;
                }else if(u.rightChild.isLeafVertex()){
                    u = u.rightChild;
                }else if(isFullTree(u) || !isFullTree(u.leftChild)){
                    u = u.leftChild;
                }else {
                    u = u.rightChild;
                }
            }
            return u;
        }
    }

    private boolean isFullTree(AVLTreeVertex vertex){
        return leftMostDepth(vertex.leftChild) == rightMostDepth(vertex.rightChild);
    }

    // compute the leftmost depth of the tree
    private int leftMostDepth(AVLTreeVertex vertex){
        if(vertex == null){
            return 0;
        }else{
            int leftDepth = leftMostDepth(vertex.leftChild);
            return leftDepth + 1;
        }
    }

    // compute the rightmost depth of the tree
    private int rightMostDepth(AVLTreeVertex vertex){
        if(vertex == null){
            return 0;
        }else{
            int rightDepth = rightMostDepth(vertex.rightChild);
            return rightDepth + 1;
        }
    }

    private void animateVertexVisit(AVLTreeVertex u) {
        if (highlightCircle == null) {
            setHighlightCircleAtVertex(u);
            repaintwait();
        } else {

            AnimatePath(highlightCircle, highlightCircle.getPosition(), u.graphicVertex.getPosition(), STEPS);
            repaintwait();
        }

    }

    private void replaceLeafVertex(AVLTreeVertex newVertex, AVLTreeVertex leafVertexToReplace) {
        AVLTreeVertex parent = leafVertexToReplace.parent;

        // Replace the leaf vertex with the new vertex
        // by adopting new vertex as child
        if (leafVertexToReplace.isLeftChild()) {
            addLeftChild(parent, newVertex);
        } else {
            addRightChild(parent, newVertex);
        }

        addGraphicEdge(parent, newVertex);
        removeLink(parent.graphicVertex, leafVertexToReplace.graphicVertex);
        removeAny(leafVertexToReplace.graphicVertex);
        removeAny(leafVertexToReplace.label);
        removeHighlightCircle();
    }

    private AVLTreeVertex getParentVertex(AVLTreeVertex vertex){
        if(vertex.parent != null){
            return vertex.parent;
        }else{
            return null;
        }
    }

    private void swapVertex(AVLTreeVertex child, AVLTreeVertex parent, int index1, int index2){

        // Swap the keys of parent and child.
        AnimatePath(keyLabel[index1],child.getPosition(),parent.getPosition(),keyLabel[index2],
                parent.getPosition(), child.getPosition(),STEPS);

        repaintwait();
        removeHighlightCircle();
        if(parent == root){
            parent.graphicVertex.setOutlineColor(Color.BLACK);
        }else {
            removeHighlightCircle2();
        }

        swapVertexKeyPointer(child,parent,index1,index2);
    }

    private void swapVertexKeyPointer(AVLTreeVertex child, AVLTreeVertex parent,int index1,int index2){

        int temKey = child.key;
        child.key = parent.key;
        parent.key = temKey;

        GElementLabel temKeyLabel = keyLabel[index1];
        keyLabel[index1] = keyLabel[index2];
        keyLabel[index2] = temKeyLabel;

    }


    protected void swap(int index1, int index2) {

        Vector2D pathForFirstTextValue[];
        Vector2D pathForSecondTextValue[];

        int numsteps = Math.max(
                (int) (Math.abs(arrayLocationTextValue[index1].getPositionX()
                        - arrayLocationTextValue[index2].getPositionX()) / 2), 10);


        // 1) Compute paths for graphical object to follow
        pathForFirstTextValue = createPath(arrayLocationTextValue[index1].getPosition(), arrayLocationTextValue[index2].getPosition(),
                numsteps);
        pathForSecondTextValue = createPath(arrayLocationTextValue[index2].getPosition(), arrayLocationTextValue[index1].getPosition(),
                numsteps);


        // 2) Animate movement along computed paths
        arrayLocationFrame[index1].setOutlineColor(Color.RED);
        arrayLocationFrame[index2].setOutlineColor(Color.RED);
        repaintwait();

        for (int i = 0; i < numsteps; i++) {

            arrayLocationTextValue[index1].setPosition(pathForFirstTextValue[i]);
            arrayLocationTextValue[index2].setPosition(pathForSecondTextValue[i]);

            repaintwaitmin();
        }

        arrayLocationFrame[index1].setOutlineColor(Color.BLACK);
        arrayLocationFrame[index2].setOutlineColor(Color.BLACK);
        // 3) Update pointers
        swapPointers(index1,index2);
        repaintwait();
    }


    private void swapPointers(int index1, int index2) {



        int tmp = arrayLocationValue[index1];
        arrayLocationValue[index1] = arrayLocationValue[index2];
        arrayLocationValue[index2] = tmp;

        GElement tmp2 = arrayLocationTextValue[index1];
        arrayLocationTextValue[index1] = arrayLocationTextValue[index2];
        arrayLocationTextValue[index2] = tmp2;

    }


    private void setAsRoot(AVLTreeVertex vertex) {
        root = vertex;
        vertex.depth = 0;
        vertex.rowIndex = 1;
        vertex.parent = null;
    }

    private AVLTreeVertex insertRootVertex(int vertexToInsert) {
        AVLTreeVertex rootVertex = AVLTreeVertex.newVertex(vertexToInsert);
        setAsRoot(rootVertex);
        setVertexAtTreePosition(rootVertex);
        setVertexLabel(rootVertex);

        keyLabel[getTreeIndexFlag()] = createLabel(String.valueOf(vertexToInsert),rootVertex.getPosition());
        GElement newVt = rootVertex.graphicVertex;
        newVt.setLabelVisible(false);

        arrayVertex[0] = rootVertex;

        return rootVertex;
    }

    private double getVertexTreeXPosition(AVLTreeVertex vertex) {
        double canvasWidth = getRealSize().getWidth();
        return (canvasWidth * vertex.rowIndex) / (Math.pow(2, vertex.depth) + 1);
    }

    private double getVertexTreeYPosition(AVLTreeVertex vertex) {
        return (vertex.depth + 1) * HEIGHT_DELTA;
    }



    private void setVertexAtAbsolutePosition(AVLTreeVertex newVertex, int insertionXPosition, int insertionYPosition) {
        GElement graphicVertex = createCircle(String.valueOf(newVertex.key), insertionXPosition, insertionYPosition);
        newVertex.graphicVertex = graphicVertex;
    }


    private void positionVertex(AVLTreeVertex newVertex, AVLTreeVertex leafVertexToReplace) {
        GElement newGraphicVertex = newVertex.graphicVertex;
        GElement leafGraphicVertex = leafVertexToReplace.graphicVertex;

        //AnimatePath(newGraphicVertex, newGraphicVertex.getPosition(), leafGraphicVertex.getPosition(), 1);
        newGraphicVertex.setPosition(leafGraphicVertex.getPosition());

    }

    private void setVertexAtTreePosition(AVLTreeVertex vertex) {
        double vertexXPosition = getVertexTreeXPosition(vertex);
        double vertexYPosition = getVertexTreeYPosition(vertex);

        GElement graphicVertex = createCircle(String.valueOf(vertex.key), vertexXPosition, vertexYPosition);
        vertex.graphicVertex = graphicVertex;
    }

    private void setVertexLabel(AVLTreeVertex vertex) {
        Font font = new Font(Font.DIALOG, Font.PLAIN, 12);

        Vector2D labelPosition = getLabelPosition(vertex, vertex.graphicVertex.getPosition());
        GElementLabel graphicLabel = createLabel(String.valueOf(getTreeIndexFlag()), labelPosition.x, labelPosition.y, font);
        graphicLabel.setLabelColor(Color.BLUE);
        vertex.label = graphicLabel;
    }

    private void setLeafLabel(AVLTreeVertex vertex) {
        Font font = new Font(Font.DIALOG, Font.PLAIN, 12);

        Vector2D labelPosition = getLabelPosition(vertex, vertex.graphicVertex.getPosition());
        GElementLabel graphicLabel = createLabel("", labelPosition.x, labelPosition.y, font);
        graphicLabel.setLabelColor(Color.BLUE);
        vertex.label = graphicLabel;
    }

    private Vector2D getLabelPosition(AVLTreeVertex vertex, Vector2D assumedPosition) {
        int sign = -1;
        if (vertex.isRightChild()) {
            sign = 1;
        }

        GElement graphicVertex = vertex.graphicVertex;
        double length;
        if (vertex.isLeafVertex()) {
            length = ((GElementRect) graphicVertex).getWidth() / 2;
        } else {
            length = ((GElementCircle) graphicVertex).getRadius();
        }

        double labelXPosition = assumedPosition.x + (sign * length);
        double labelYPosition = assumedPosition.y - length - 5;

        return new Vector2D(labelXPosition, labelYPosition);
    }

    private void setLeafVertexAtTreePosition(AVLTreeVertex vertex) {
        double vertexXPosition = getVertexTreeXPosition(vertex);
        double vertexYPosition = getVertexTreeYPosition(vertex);

        GElement graphicVertex = createRectangle("", vertexXPosition, vertexYPosition, LEAF_LENGTH, LEAF_LENGTH);
        vertex.graphicVertex = graphicVertex;
    }

    private void addLeafChildren(AVLTreeVertex parentVertex) {
        AVLTreeVertex leftLeafVertex = AVLTreeVertex.newLeafVertex();
        AVLTreeVertex rightLeafVertex = AVLTreeVertex.newLeafVertex();

        addLeftChild(parentVertex, leftLeafVertex);
        addRightChild(parentVertex, rightLeafVertex);

        setLeafVertexAtTreePosition(leftLeafVertex);
        setLeafVertexAtTreePosition(rightLeafVertex);
        //setVertexLabel(leftLeafVertex);
        //setVertexLabel(rightLeafVertex);

        setLeafLabel(leftLeafVertex);
        setLeafLabel(rightLeafVertex);

        addGraphicEdge(parentVertex, leftLeafVertex);
        addGraphicEdge(parentVertex, rightLeafVertex);
    }

    private void addLeftChild(AVLTreeVertex parent, AVLTreeVertex leftChild) {
        parent.leftChild = leftChild;
        leftChild.parent = parent;
        setChildDepth(leftChild, parent);
        setLeftChildRowIndex(leftChild, parent);
    }

    private void setLeftChildRowIndex(AVLTreeVertex leftChild, AVLTreeVertex parent) {
        leftChild.rowIndex = parent.rowIndex * 2 - 1;
    }

    private void setChildDepth(AVLTreeVertex child, AVLTreeVertex parent) {
        child.depth = parent.depth + 1;
    }

    private void addRightChild(AVLTreeVertex parent, AVLTreeVertex rightChild) {
        parent.rightChild = rightChild;
        rightChild.parent = parent;
        setChildDepth(rightChild, parent);
        setRightChildRowIndex(rightChild, parent);
    }

    private void setRightChildRowIndex(AVLTreeVertex rightChild, AVLTreeVertex parent) {
        rightChild.rowIndex = parent.rowIndex * 2;
    }

    private void addGraphicEdge(AVLTreeVertex parent, AVLTreeVertex child) {
        DSShapeLink edge;

        //If the child is a leaf vertex , edge should be anchored to the top of square
        if (child.isLeafVertex()) {
            edge = createLink(parent.graphicVertex, child.graphicVertex, GLink.SHAPE_ARC, GElement.ANCHOR_CENTER, GElement.ANCHOR_TOP, "", 1);
        } else {
            // Otherwise anchor the edge centrally
            edge = createLink(parent.graphicVertex, child.graphicVertex, GLink.SHAPE_ARC, GElement.ANCHOR_CENTER, GElement.ANCHOR_CENTER, "", 1);
        }
        edge.setArrowVisible(false);
    }

    private void removeHighlightCircle() {
        removeAny(highlightCircle);
        highlightCircle = null;
    }

    private void removeHighlightCircle2() {
        removeAny(highlightCircle2);
        highlightCircle2 = null;
    }

    private void setHighlightCircleAtRoot() {
        setHighlightCircleAtVertex(root);
        repaintwait();
    }


    private void setHighlightCircleAtVertex(AVLTreeVertex vertex) {
        highlightCircle = createCircle("", vertex.graphicVertex.getPositionX(), vertex.graphicVertex.getPositionY());
        highlightCircle.setOutlineColor(Color.RED);
    }

    private void setHighlightCircle2AtVertex(AVLTreeVertex vertex) {
        if(vertex == root) {
            vertex.graphicVertex.setOutlineColor(Color.RED);
        }else{
            highlightCircle2 = createCircle("", vertex.graphicVertex.getPositionX(), vertex.graphicVertex.getPositionY());
            highlightCircle2.setOutlineColor(Color.RED);
        }
    }

    private void removeGraphicalElementsForVertex(AVLTreeVertex vertexToDelete) {

        if (vertexToDelete.hasParent()) {
            removeLink(vertexToDelete.parent.graphicVertex, vertexToDelete.graphicVertex);
        }
        removeAny(vertexToDelete.graphicVertex);
        removeAny(vertexToDelete.label);

        removeLink(vertexToDelete.graphicVertex, vertexToDelete.leftChild.graphicVertex);
        removeAny(vertexToDelete.leftChild.graphicVertex);
        removeAny(vertexToDelete.leftChild.label);

        removeLink(vertexToDelete.graphicVertex, vertexToDelete.rightChild.graphicVertex);
        removeAny(vertexToDelete.rightChild.graphicVertex);
        removeAny(vertexToDelete.rightChild.label);

        repaintwaitmin();
    }

    @Override
    public void restart() {
        setNewInput(arrayLocationValueCopy.clone());
    }

    public void setNewInput(int[] numArray){

        setRootElement(null);
        panel.setLabel("");
        nullLabel = createLabel("",20,20);
        setHeapSymbols();

        currentNumElements = numArray.length;
        intialiseElements();
        assignElements(numArray);
    }

    public void setHeapSymbols(){
        //the boundary for differentiate the array and the abstract tree.
        boundary = createRectangle("",0,56,50000,0.0001);
        boundary.setOutlineColor(Color.LIGHT_GRAY);

        //the labels for showing the two types of diagrams in the screen.
        arraySymbol = createLabel("Array",40,25);
        arraySymbol.setFont(symbolFont);
        treeSymbol = createLabel("Abstract Tree", 70,78);
        treeSymbol.setFont(symbolFont);
    }

    private void intialiseElements() {

        arrayLocationValue = new int[currentNumElements];
        arrayLocationValueCopy = new int[currentNumElements];
        arrayLocationTextValue = new GElement[currentNumElements];
        arrayLocationIndex = new GElement[currentNumElements];
        arrayLocationFrame = new GElement[currentNumElements];

        Xpos = new int[currentNumElements];
        Ypos = new int[currentNumElements];

        setArrayIndexFlag(0);
        setTreeIndexFlag(0);
        keyLabel = new GElementLabel[currentNumElements];
        arrayVertex = new AVLTreeVertex[currentNumElements];
        root = null;
    }

    private void assignElements(int[] numArray) {

        Font font = new Font(Font.DIALOG, Font.PLAIN,12);
        arrayLocationValue = numArray;
        cloneCurrentInput();

        for (int i = 0; i < currentNumElements; i++) {
            Xpos[i] = STARTING_X_COORD + i * 28;
            Ypos[i] = STARTING_Y_COORD;

            arrayLocationIndex[i] = createLabel(String.valueOf(i), Xpos[i], Ypos[i] -24, false, font);
            arrayLocationIndex[i].setLabelColor(Color.BLUE);
            arrayLocationFrame[i] = createRectangle("", Xpos[i], Ypos[i] + 2, 28, 28, false);
        }

        // This has to be done in a seperate loop to ensure Z-ordering of
        // elements on the canvas
        for (int i = 0; i < currentNumElements; i++) {
            arrayLocationTextValue[i] = createLabel(String.valueOf(arrayLocationValue[i]), Xpos[i], Ypos[i],font);
        }


        for(int i = 0; i < currentNumElements; i++) {
            int vertexToInsert = arrayLocationValue[i];
            if (root == null) {
                root = insertRootVertex(vertexToInsert);
                addLeafChildren(root);
            } else {
                AVLTreeVertex vertexInserted = insertIntoTree(vertexToInsert);
                addLeafChildren(vertexInserted);
            }
            setTreeIndexFlag(getTreeIndexFlag()+1);
        }
    }

    private void cloneCurrentInput() {
        arrayLocationValueCopy = arrayLocationValue.clone();
    }

    public int getArrayIndexFlag() {
        return arrayIndexFlag;
    }

    public void setArrayIndexFlag(int indexFlag) {
        this.arrayIndexFlag = indexFlag;
    }


    public int getTreeIndexFlag() {
        return treeIndexFlag;
    }

    public void setTreeIndexFlag(int treeIndexFlag) {
        this.treeIndexFlag = treeIndexFlag;
    }
}

