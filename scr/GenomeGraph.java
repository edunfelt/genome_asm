import java.util.*;

public class GenomeGraph implements Iterable<GenomeGraph.Contig> {
    int size;
    // in the adjacency list, an index represents a contig id, and the contig itself is
    // stored in the first element of its adjacency list
    ArrayList<ArrayList<Contig>> adjList;

    GenomeGraph() {
        size = 0;
        adjList = new ArrayList<>();
    }

    public static void main(String[] args) {
        GenomeGraph graph = new GenomeGraph();
        Contig c1 = new Contig(0, 5);
        Contig c2 = new Contig(1, 10);
        Contig c3 = new Contig(2, 15);
        Contig c4 = new Contig(3, 20);
        graph.addContig(c1);
        graph.addContig(c2);
        graph.addContig(c3);
        graph.addContig(c4);

        graph.addOverlap(c1, c2);
        graph.addOverlap(c1, c3);
        graph.addOverlap(c2, c4);

        System.out.println("Size: " + graph.getSize());

        for (Contig c : graph) {
            System.out.println(c.getContigId() + " " + c.getLength() + " " + graph.contigDegree(c));
        }
    }

    public void addContig(Contig c) {
        while(c.getContigId() >= size) {
            size++;
            adjList.add(new ArrayList<>());
        }
        adjList.get(c.getContigId()).add(c);
    }

    public void addOverlap(Contig c1, Contig c2) {
        adjList.get(c1.getContigId()).add(c2);
        adjList.get(c2.getContigId()).add(c1);
    }

    public int getSize() {
        return size;
    }

    public int contigDegree(Contig c) {
        return adjList.get(c.getContigId()).size() - 1;
    }

    public ArrayList<GenomeGraph> getComponents() {
        ArrayList<GenomeGraph> components = new ArrayList<>();

        /* TO DO */

        return components;
    }

    public Iterator<Contig> iterator() {
        return new GraphIterator();
    }

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

    public static class Contig {
        private int contigId;
        private int contigLen;

        public Contig(int id, int length) {
            contigId = id;
            contigLen = length;
        }

        public int getLength() {
            return contigLen;
        }

        public int getContigId() {
            return contigId;
        }

    }
}
