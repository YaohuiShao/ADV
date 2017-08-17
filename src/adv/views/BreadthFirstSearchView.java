package adv.views;

import adv.directedGraphModel.Edge;
import adv.directedGraphModel.GElementVertex;
import adv.panels.BreadthFirstSearchPanel;
import adv.panels.DirectedGraphPanel;
import adv.panels.Panel;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class BreadthFirstSearchView extends DirectedGraphView {

    protected static final Color UNVISITED = Color.WHITE;
    protected static final Color VISITED_AND_PROCESSING = Color.LIGHT_GRAY;
    protected static final Color VISITED_AND_FINISHED = Color.decode("#424242");
    protected static final Color DEFAULT_EDGE_COLOR = Color.BLACK;
    protected static final Color CONSIDER_EDGE_COLOR = Color.decode("#FF6F00"); //AMBER
    protected static final Color TRAVERSED_EDGE_COLOR = Color.BLUE;
    protected GElement highlightCircle;
    protected HashMap<Integer, TreeSet<Integer>> connectedVertices;
    protected ArrayList<Integer> visitPath;
    protected Panel panel;

    public BreadthFirstSearchView(Panel panel) {
        super(panel);
        this.panel = panel;
    }



    public void runAlgorithm() {
        performBreadthFirstSearch();
    }


    public void performBreadthFirstSearch() {
        lockCanvas();
        runSetup();
        bfs();
        showResults();
        unlockCanvas();
    }

    // Method for locking canvas so users can't add vertices during visualisation
    protected void lockCanvas() {
        canvasLocked = true;
    }

    // Method for unlocking canvas after visualisation
    protected void unlockCanvas() {
        canvasLocked = false;
    }

    protected void runSetup() {
        connectedVertices = directedGraph.adjacencyList();
        visitPath = new ArrayList<Integer>();
    }

    // Breadth first search visualisation
    protected void bfs() {

        int N = directedGraph.getNumberVertices();

        for (int i = 0 ; i < N ; i++) {
            //if (vertexUnvisited(i)) {
            bfsFromVertex(i);
            //}
            removeHighlightCircle();
        }
    }

    // Breadth first search algorithm from INF2B notes
    protected void bfsFromVertex(int vertex) {
        visitVertex(vertex);
        TreeSet<Integer> vertexNeighbours = connectedVertices.get(vertex);

        if (vertexNeighbours != null) {
            panel.setMsgText("Process vertex " + vertex + "'s neighbours.");
//            displayMessage("Process vertex " + vertex + "'s neighbours.");
            repaintwait();

            for (Integer neighbor : vertexNeighbours) {

                considerTraversingEdge(vertex, neighbor);

                if (vertexUnvisited(neighbor)) {
                    setEdgeAsTraversed(vertex, neighbor);
                    visitVertex(neighbor);
                    backTrackTo(vertex);
                } else {
                    abortEdgeTraversal(vertex, neighbor);
                }

            }
        } else {
            panel.setMsgText("Vertex " + vertex + " has no neighbours to be processed.");
//            displayMessage("Vertex " + vertex + " has no neighbours to be processed.");
            repaintwait();
        }

        recordVertexFinish(vertex);
    }

    // Animation method for turning edge amber
    protected void considerTraversingEdge(Integer fromVertex, Integer toVertex) {
        panel.setMsgText("Consider traversing edge from vertex " + fromVertex + " to vertex " + toVertex + ".");
//        displayMessage("Consider traversing edge from vertex " + fromVertex + " to vertex " + toVertex + ".");
        getEdge(fromVertex, toVertex).setOutlineColor(CONSIDER_EDGE_COLOR);
        repaintwait();
    }

    // Animation method for turning edge blue
    protected void setEdgeAsTraversed(Integer fromVertex, Integer toVertex) {
        getEdge(fromVertex, toVertex).setOutlineColor(TRAVERSED_EDGE_COLOR);
    }

    // Record that a vertex has turned black
    protected void recordVertexFinish(int vertex) {
        panel.setMsgText("Finished processing vertex " + vertex + ".");
//        displayMessage("Finished processing vertex " + vertex + ".");
        getVertex(vertex).setFillColor(VISITED_AND_FINISHED);
        getVertex(vertex).setLabelColor(Color.WHITE);
        repaintwait();
    }

    // Animation method for performing backtracking
    protected void backTrackTo(int vertex) {
        panel.setMsgText("Backtrack to vertex " + vertex + ".");
//        displayMessage("Backtrack to vertex " + vertex + ".");
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
        repaintwait();
    }

    // Animation method for visiting a vertex
    protected void visitVertex(int vertex) {
        panel.setMsgText("Visit vertex " + vertex + ".");
//        displayMessage("Visit vertex " + vertex + ".");

        if (highlightCircle == null) {
            setHighlightCircleAtVertex(vertex);
            repaintwait();
        } else {
            AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
            repaintwait();
        }


        if (skipAnimation) {
            repaintwaitmin();
        } else {
            repaintwaitmin(10);
        }
        if (vertexUnvisited(vertex)) {
            recordVertexVisit(vertex);
        }
    }

    // Animation method for positioning highlight circle at a vertex
    private void setHighlightCircleAtVertex(int vertex) {
        highlightCircle = createCircle("", vertexPosition(vertex).getX(), vertexPosition(vertex).getY());
        highlightCircle.setOutlineColor(Color.RED);
    }

    // Show the visit order
    protected void showResults() {
        removeHighlightCircle();
        String bfsTraversalOrder = "";
        for (int i = 0; i < visitPath.size(); i++) {
            bfsTraversalOrder = bfsTraversalOrder + visitPath.get(i);

            if (i != visitPath.size() - 1) {
                bfsTraversalOrder = bfsTraversalOrder + ", ";
            }

        }

        panel.setMsgText("Traversal order: " + bfsTraversalOrder);
//        displayMessage("Finished traversal," + " vertices visited in the order: " + bfsTraversalOrder);
    }

    // Show the visit order
    protected void recordVertexVisit(int vertex) {
        visitPath.add(vertex);
        panel.setMsgText("Vertex " + vertex + " has been visited.");
//        displayMessage("Vertex " + vertex + " has been visited.");
        getVertex(vertex).setFillColor(VISITED_AND_PROCESSING);
        repaintwait();
    }

    // Retrieve the (x,y) coordinate for graphical vertex
    protected Vector2D vertexPosition(int vertex) {
        return directedGraph.vertexPosition(vertex);
    }

    // Check if vertex is white
    protected boolean vertexUnvisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == UNVISITED;
    }

    // Check if vertex is grey
    protected boolean vertexBeingProcessed(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == VISITED_AND_PROCESSING;
    }

    // Check if vertex is black
    protected boolean vertexVisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == VISITED_AND_FINISHED;
    }

    @Override
    public void restart() {
        setDefaultGraphColours();
    }

    // Reset graphical objects to their default colours
    // This is used when changes are made to the graph
    // after a visualisation. This created a new graph
    // hence we reset the state.
    @Override
    protected void setDefaultGraphColours() {
//        displayMessage("Message Text");
        setDefaultVertexColor();
        setDefaultEdgeColor();
        removeHighlightCircle();
        repaint();
    }

    private void setDefaultVertexColor() {
        int N = directedGraph.getNumberVertices();

        for (int i = 0 ; i < N ; i++) {
            getVertex(i).setFillColor(UNVISITED);
            getVertex(i).setLabelColor(Color.BLACK);
        }

    }

    private void setDefaultEdgeColor() {
        for (Edge edge : directedGraph.getEdgeSet()) {
            int fromVertex = edge.getFromVertex();
            int toVertex = edge.getToVertex();
            resetEdgeToDefaultColor(fromVertex, toVertex);
        }
    }

    protected void removeHighlightCircle() {
        removeAny(highlightCircle);
        highlightCircle = null;
    }

    protected GElementVertex getVertex(int vertex) {
        return directedGraph.getVertex(vertex);
    }

    private void resetEdgeToDefaultColor(Integer fromVertex, Integer toVertex) {
        getEdge(fromVertex, toVertex).setOutlineColor(DEFAULT_EDGE_COLOR);
    }

    protected GLink getEdge(int fromVertex, int toVertex) {
        return directedGraph.getEdge(fromVertex, toVertex);
    }

    protected void abortEdgeTraversal(Integer fromVertex, Integer toVertex) {
        panel.setMsgText("Vertex " + toVertex + " already visited, abort visit.");
//        displayMessage("Vertex " + toVertex + " already visited, abort visit.");
        getEdge(fromVertex, toVertex).setOutlineColor(DEFAULT_EDGE_COLOR);
        repaintwait();
    }


}
