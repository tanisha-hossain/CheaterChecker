import java.util.*;
import java.io.*;
import java.lang.Math.*;

public class CheaterChecker {
    static String RED = "\u001B[31m";
    static String RESET = "\u001B[0m";

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        List<String> files = getFileNamesFromUser(userInput);
        int threshold = getThreshold(userInput);
        List<ArrayList<Integer>> fileInformations = new ArrayList<>(); // each item here will contains a fixed 4-element list of [# of lines, # ifs, # loops, # braces]
        for (String fileName : files) {
            System.out.println("\nAdding File: " + fileName);
            fileInformations.add(processFile(fileName, threshold));
        }
        
        List<ComparisonResult> allResults = new ArrayList<>(); // each item here will hold a fixed 4-element list consisting of an analysis result showing [index i, index j, similarity score, flagged]
        for (int i=0; i<fileInformations.size(); i++) {
            for (int j =i+1; j<fileInformations.size(); j++) {
                double similarity = calculateSimilarityScore(fileInformations.get(i), fileInformations.get(j));
                System.out.println("\nSimilarity score for file " + files.get(i) + " and file " + files.get(j) + ": " + similarity); //had tp add 1 to the indices because index starts at 0
                boolean flagged;
                if (similarity >threshold){
                    flagged=true;
                }
                else{
                    flagged=false;
                }
                allResults.add(new ComparisonResult(files.get(i), files.get(j), similarity, flagged));
            }
        }
       
        System.out.println("\nAll Similarities analyzed. The following file combinations surpass the threshold and are considered to be cheating!\n");
        for (ComparisonResult result : allResults) {
            if (result.isFlagged()) {
                System.out.println(result.formatResults());
            }
        }

        ComparisonResult.writeResultsToFile(allResults);
    }//end main
    
    

    private static List<String> getFileNamesFromUser(Scanner userInput) {
        System.out.println("Welcome to Cheater Checker! Please enter your file names SEPARATED BY COMMAS. NO SPACES!");
        String input = userInput.nextLine();
        String[] parts = input.split(",");
        return Arrays.asList(parts);
    }

    private static int getThreshold(Scanner userInput) {
        System.out.println("Great! Now enter a similarity threshold between 0 and 100.");
        return userInput.nextInt();
    }
   
    private static ArrayList<Integer> processFile(String fileName, int threshold) { 
        List<String> lines = new ArrayList<>();
        int ifCount = 0;
        int loopCount = 0;
        int braceCount = 0;

        File myFile = new File(fileName);
        if (myFile.exists()){
            System.out.printf("\n%sFile name: %s\n%s", RED, myFile.getName(), RESET);
            System.out.printf("%sFile size in bytes: %d\n\n%s", RED, myFile.length(), RESET);
        } else{
            System.out.println("The file does not exist.");
            return new ArrayList<>();
        }
     
        try (Scanner scan = new Scanner(myFile)) {
           
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                lines.add(line);
                if (line.contains("if")){
                    ifCount++;
                }
                if (line.contains("for")){
                    loopCount++;
                }
                if (line.contains("{")) {
                    braceCount++;
                }
            }
            // print statistics about the file
            printFileStatistics(lines.size(), ifCount, loopCount, braceCount);
           
            ArrayList<Integer> fileAnalysisList = new ArrayList<>();
            fileAnalysisList.add(lines.size());
            fileAnalysisList.add(ifCount);
            fileAnalysisList.add(loopCount);
            fileAnalysisList.add(braceCount);
            return fileAnalysisList; //returns integer arraylist for the fileInformations list in main

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return new ArrayList<>();
        }
    }
   
   
    private static void printFileStatistics(int lineCount, int ifCount, int loopCount, int braceCount) {
        System.out.printf("Line count: %d\n", lineCount);
        System.out.printf("Number of if statements: %d\n", ifCount);
        System.out.printf("Loops: %d\n", loopCount);
        System.out.printf("Braces: %d\n", braceCount);
    }
   
    public static double calculateSimilarityScore(List<Integer> fileInfo1, List<Integer> fileInfo2) {
        if (fileInfo1.size() != fileInfo2.size()) {
            return 0.0; // something went wrong anyway to process file, so assume 0 that no similarilty occurs because one of the files are possibly corrupted?
        }
        ArrayList<Double> similarities = new ArrayList<>(); // We will assume based on scale 0-100, similarity = 100 - percentError
        for (int i=0; i<fileInfo1.size(); i++) {
            double numerator = Math.abs((Math.max(fileInfo1.get(i), fileInfo2.get(i))) - (Math.min(fileInfo1.get(i), fileInfo2.get(i))) ); 
            double denominator = Math.min(fileInfo1.get(i),fileInfo2.get(i));
            double percentError = (numerator/denominator)*100;
            System.out.println("Percent error: "+ percentError);
            // Theoretical = fileInfo1
            // Experimental/Actual Data = fileInfo2
            // PercentError = (Experimental - Theoretical)/Theoretical  x 100
           
            similarities.add(100 - percentError);
            //System.out.println(i + ":" + (100-percentError));
        }
       
        double sum = 0;
        for (double num: similarities){
            sum += num;
        }
        System.out.println("Average is" + sum/4.0);
        return sum/4.0; //average of all the 4 doubles in similarities list
    }
}//end class
