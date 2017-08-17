package adv.avlTreeModel;

import edu.usfca.ds.shapes.DSShapeLink;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GElementLabel;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

// Bookkeeping structure used to model AVL tree
public class AVLTreeVertex {

    public Integer key;
    public AVLTreeVertex parent;
    public AVLTreeVertex leftChild;
    public AVLTreeVertex rightChild;
    public DSShapeLink leftLink;
    public DSShapeLink rightLink;
    public Integer height;
    public Integer depth;
    public Integer rowIndex; // Used to store the location of a vertex in a row of the tree

    public GElement graphicVertex; //Circle (internal vertex) , Square (leaf vertex)
    public GElementLabel label; // Used for the height of a vertex

    protected Vector2D position = new Vector2D();
    protected final Object lock = new Object();
    protected List elements = new ArrayList();

    public static AVLTreeVertex newVertex(int value) {
        AVLTreeVertex newVertex = new AVLTreeVertex();
        newVertex.key = value;
        newVertex.height = 1;

        return newVertex;
    }

    public static AVLTreeVertex newVertex(Vector2D position) {
        AVLTreeVertex newVertex = new AVLTreeVertex();
        newVertex.key = null;
        newVertex.setPosition(position);
        return newVertex;
    }

    public static AVLTreeVertex newLeafVertex() {
        AVLTreeVertex leafVertex = new AVLTreeVertex();
        leafVertex.key = null;
        leafVertex.leftChild = null;
        leafVertex.rightChild = null;
        leafVertex.height = 0;

        return leafVertex;
    }

    public boolean hasTwoLeafChildren() {
        return isInternalVertex() && leftChild.isLeafVertex() && rightChild.isLeafVertex();
    }

    public boolean isInternalVertex() {
        return !isLeafVertex();
    }


    public boolean isLeafVertex() {
        return key == null;
    }

    public boolean isRightLeafVertex() {
        return isLeafVertex() && isRightChild();
    }

    public boolean isLeftLeafVertex() {
        return isLeafVertex() && isLeftChild();
    }

    public boolean rightChildIsInternalVertex() {
        return isInternalVertex() && rightChild.isInternalVertex();
    }

    public boolean leftChildIsInternalVertex() {
        return isInternalVertex() && leftChild.isInternalVertex();
    }

    public void setPosition(int x, int y) {
        this.graphicVertex.setPosition(x, y);
    }

    public void setPosition(Vector2D position) {
        this.position = position;
        elementPositionDidChange();
    }

    public Vector2D getPosition() {return this.graphicVertex.getPosition();}

    public boolean isLeftChild() {
        return hasParent() && parent.leftChild == this;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean isRightChild() {
        return hasParent() && parent.rightChild == this;
    }

    public void removeValue() {

        // Don't set to null since
        // vertex then becomes a leaf vertex
        this.key = -1;
        graphicVertex.setLabel("");

    }

    public void setKey(Integer key) {

        if (key >= 0) {
            this.key = key;
            graphicVertex.setLabel(String.valueOf(key));
        } else {
            throw new InvalidParameterException();
        }

    }

    public void move(double dx, double dy) {
        position.shift(dx, dy);

        // Recursively move every other children objects
        synchronized (lock) {
            for (int i = 0; i < elements.size(); i++) {
                GElement element = (GElement) elements.get(i);
                element.move(dx, dy);
            }
        }

        elementPositionDidChange();
    }

    public void moveToPosition(Vector2D position) {
        double dx = position.x - getPosition().x;
        double dy = position.y - getPosition().y;
        move(dx, dy);
    }

    public void elementPositionDidChange() {
        updateAnchors();
    }

    public void updateAnchors() {
    }



}
