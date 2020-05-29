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

    public void addContig(Contig c) {
        while(c.getContigId() >= adjList.size()) {
            adjList.add(new ArrayList<>());
        }
        if (adjList.get(c.getContigId()).size() == 0) {
            size++;
            adjList.get(c.getContigId()).add(c);
        }
    }

    public void addOverlap(Contig c1, Contig c2) {
        if (!adjList.get(c1.getContigId()).contains(c2)) {
            adjList.get(c1.getContigId()).add(c2);
            adjList.get(c2.getContigId()).add(c1);
        }
    }

    public int getSize() {
        return size;
    }

    public int contigDegree(Contig c) {
        return adjList.get(c.getContigId()).size() - 1;
    }

    public ArrayList<GenomeGraph> getComponents() {
        ArrayList<GenomeGraph> components = new ArrayList<>();
        ArrayList<Boolean> status = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            status.add(false);
        }

        while (status.contains(false)) {
            int notVisited = status.indexOf(false);

            GenomeGraph component = new GenomeGraph();
            Contig start = adjList.get(notVisited).get(0);
            Queue<Contig> contigQueue = new LinkedList<>();

            contigQueue.add(start);
            status.set(notVisited, true);
            component.addContig(start);

            while (contigQueue.peek() != null) {
                Contig current = contigQueue.remove();

                for (Contig neighb : adjList.get(current.getContigId())) {
                    if (current != neighb) {
                        if (!status.get(neighb.getContigId())) {
                            contigQueue.add(neighb);
                            status.set(neighb.getContigId(), true);
                        }
                        component.addContig(neighb);
                        component.addOverlap(current, neighb);
                    }
                }
            }

            components.add(component);
        }
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