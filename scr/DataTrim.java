import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class to trim large data files containing overlap data, formatted as
 * described in the project instructions
 *
 * Returns a trimmed file in which complete overlaps are removed, contig
 * identifiers are simplified to integer values, and unnecessary columns are removed
 */

public class DataTrim {
    /**
     * Method to determine if a line is a complete overlap
     * @param s, string array formatted as described in the project instructions
     * @return false if s describes a complete overlap, true otherwise
     */
    private static boolean Unique(String[] s) {
        if(s[5].equals("0") && s[6].equals(s[7])) {// the first contig is completely overlapped
            return false;
        } else if(s[9].equals("0") && s[10].equals(s[11])) {// the second contig is completely overlapped
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        try {
            File file = new File(args[0]);
            Scanner sc = new Scanner(file);
            FileWriter outf = new FileWriter("trim_" + args[0]);

            HashMap<String, Integer> ids = new HashMap<>(); // for storing identifiers and integer keys
            int curId = 0;
            
            while(sc.hasNextLine()) {
            	String[] line = sc.nextLine().split("\\s+");

                if(Unique(line)) {
                    for (int i = 0; i < 2; i++) {// the first two values are identifiers
                    	String key = line[i];
                    	if (!ids.containsKey(key)) {
                    		ids.put(key, curId);
                    		curId++;
                    	} 
                        outf.write(ids.get(key) + "    ");
                    }

                    for (int i = 2; i < 12; i++) {// write contents of useful columns
                        if (i == 11) {
                            outf.write(line[i] + "\n");
                        } else if ((i >= 5 && i < 8) || (i >= 9)) {
                            outf.write(line[i] + "    ");
                        }
                    }
                } 
            }

            sc.close();
            outf.close();
            System.out.println("File trimmed successfully.");

        } catch(FileNotFoundException E) {
            System.err.println("Could not find file " + args[0] + " in the current directory.");

        } catch (IOException e) {
            System.err.println("Could not write to file!");
        }
    }
}