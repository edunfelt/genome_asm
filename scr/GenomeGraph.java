import java.util.*;

/**
 * Store length and overlap information about contigs in a graph structure
 * that supports iteration, determining node degree, graph size, and
 * finding the number of connected components.
 * Due to the nature of the class, it is possible to expand this to include
 * more information in each contig.
 */
public class GenomeGraph implements Iterable<GenomeGraph.Contig> {
    // in the adjacency list, an index represents a contig id, and the contig itself is
    // stored in the first element of its adjacency list
    ArrayList<ArrayList<Contig>> adjList;
    int size; // number of contigs in the graph

    private int compSize; // counter for finding the size of components
    private boolean[] status; // keep track of whether a contig is visited

    /**
     * Constructor
     */
    GenomeGraph() {
        size = 0;
        adjList = new ArrayList<>();
    }

    /**
     * Add a new contig to the graph
     * @param c, contig to add
     */
    public void addContig(Contig c) {
        while(c.getContigId() >= adjList.size()) {// increase adjacency list to the required size
            adjList.add(new ArrayList<>());
        }
        if (adjList.get(c.getContigId()).size() == 0) {// add contig only if contig is not already in the graph
            size++;
            adjList.get(c.getContigId()).add(c);
        }
    }

    /**
     * Add a contig overlap in the graph (an edge)
     * @param c1, first contig
     * @param c2, second contig
     */
    public void addOverlap(Contig c1, Contig c2) {
        if (!adjList.get(c1.getContigId()).contains(c2)) {// add only if the edge does not exist
            adjList.get(c1.getContigId()).add(c2);
            adjList.get(c2.getContigId()).add(c1);
        }
    }

    /**
     * Determine the size of this graph
     * @return, size
     */
    public int getSize() {
        return size;
    }

    /**
     * Determine the degree of a contig
     * @param c, contig
     * @return the degree of the contig
     */
    public int contigDegree(Contig c) {
        // remove 1 from the value since the first element is the contig itself
        return adjList.get(c.getContigId()).size() - 1;
    }

    /**
     * Helper method for getComponents to do a DFS in the graph from a start contig
     * @param start, contig from which to initiate the algorithm
     */
    private void DFS(Contig start) {
        for (Contig c : adjList.get(start.getContigId()).subList(1, adjList.get(start.getContigId()).size())) {
            if(!status[c.getContigId()]) {
                status[c.getContigId()] = true;
                compSize++;
                DFS(c);
            }
        }
    }

    /**
     * Recursively find the number of connected components, and their sizes
     * @return an arraylist containing an integer with the number of components
     * and a list containing the size of each component
     * Inspired by: https://www.geeksforgeeks.org/connected-components-in-an-undirected-graph/
     */
    public ArrayList<Object> getComponents() {
        ArrayList<Object> results = new ArrayList<>();
        ArrayList<Integer> sizes = new ArrayList<>();
        status = new boolean[size];
        int components = 0;

        for (int i = 0; i < size; i++) {// no contig is visited at the start
            status[i] = false;
        }

        for (int i = 0; i < size; i++) {
            if (!status[adjList.get(i).get(0).getContigId()]) {// check status of current contig
                status[adjList.get(i).get(0).getContigId()] = true;
                components++; // add new component
                compSize = 1; // increase size of current component

                DFS(adjList.get(i).get(0));
                sizes.add(compSize); // add size of current component
            }
        }
        results.add(components);
        results.add(sizes);

        return results;
    }

    public Iterator<Contig> iterator() {
        return new GraphIterator();
    }

    /**
     * Iterate over contigs in the data structure
     */
    private class GraphIterator implements Iterator<Contig> {
        Stack<Contig> contigs = new Stack<>();

        public GraphIterator() {
            for (int i = 0; i < size; i++) {
                contigs.push(adjList.get(i).get(0));
            }
        }

        public boolean hasNext() {
            return !contigs.empty();
        }

        public Contig next() {
            if(this.hasNext()) {
                return contigs.pop();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Class for creating contigs containing information
     */
    public static class Contig {
        private int contigId;
        private int contigLen;

        /**
         * Create new contig
         * @param id, contig identifier
         * @param length, length of contig
         */
        public Contig(int id, int length) {
            contigId = id;
            contigLen = length;
        }

        /**
         * Retrieve length of contig
         * @return length
         */
        public int getLength() {
            return contigLen;
        }

        /**
         * Retrieve contig identifier
         * @return identifier
         */
        public int getContigId() {
            return contigId;
        }

    }
}
