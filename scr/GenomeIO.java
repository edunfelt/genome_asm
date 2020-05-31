import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Take a file returned from DataTrim and return a file with four lines,
 * 1. the number of unique contigs in the graph
 * 2. the degree of each contig in the graph (whitespace separated)
 * 3. the number of components in the graph
 * 4. the size of each component in the graph (whitespace separated)
 */
public class GenomeIO {
    /**
     * Read a file and return a GenomeGraph from the data
     * @param file, file name
     * @return GenomeGraph
     */
    public static GenomeGraph read(File file) {
        GenomeGraph graph = new GenomeGraph();

        try {
            Scanner sc = new Scanner(file);

            while(sc.hasNextLine()) {
                String[] entries = sc.nextLine().split("\\s+");
                int[] values = new int[8];

                for(int i = 0; i < 8; i++) {
                    values[i] = Integer.parseInt(entries[i]);
                }

                GenomeGraph.Contig c1 = new GenomeGraph.Contig(values[0], values[4]);
                GenomeGraph.Contig c2 = new GenomeGraph.Contig(values[1], values[7]);
                graph.addContig(c1);
                graph.addContig(c2);
                graph.addOverlap(c1, c2);
            }
            sc.close();

        } catch (FileNotFoundException E) {
            System.err.println("Could not find file " + file + " in current directory.");
        }
        return graph;
    }

    /**
     * Return a file with relevant information about the graph
     * @param args, file name
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        String[] name = args[0].split("\\.");
        FileWriter dataOut = new FileWriter("data_" + name[0]);
        GenomeGraph graph = read(file);

        ArrayList<Integer> contigDeg = new ArrayList<>();
        ArrayList<Object> components = graph.getComponents();
        int parts = (int) components.get(0); // the number of components
        ArrayList<Integer> sizes = (ArrayList<Integer>) components.get(1); // component sizes

        for(GenomeGraph.Contig c : graph) {
            contigDeg.add(graph.contigDegree(c));
        }

        dataOut.write(graph.getSize() + "\n");

        for(int i = 0; i < contigDeg.size(); i++) {
            dataOut.write(contigDeg.get(i) + "    ");
        }

        dataOut.write("\n" + parts + "\n");

        for (int i = 0; i < sizes.size(); i++) {
            dataOut.write(sizes.get(i) + "    ");
        }

        dataOut.close();

        System.out.println("data_" + name[0]);
    }
}
