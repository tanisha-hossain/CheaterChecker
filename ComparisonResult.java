import java.util.*;
import java.io.*;
import java.lang.Math.*;

public class ComparisonResult {
    private String file1Name;
    private String file2Name;
    private double similarityScore;
    private boolean flagged;

    public ComparisonResult(String file1Name, String file2Name, double similarityScore, boolean flagged) {
        this.file1Name = file1Name;
        this.file2Name = file2Name;
        this.similarityScore = similarityScore;
        this.flagged = flagged;
    }

    public String formatResults(){
        return String.format("File '%s' and File '%s' have a Similarity Score of: %f. Flagged for cheating: %s\n", file1Name, file2Name, similarityScore, flagged?"YES":"No");
    }

    public static void writeResultsToFile(List<ComparisonResult> results) {
        try {
            FileWriter writer = new FileWriter("results.txt");
            for (ComparisonResult result : results) {
                writer.write(result.formatResults());
            }
            writer.close();
            System.out.println("\nGo check results.txt!");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public boolean isFlagged() {
        return flagged;
    }
}
