import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StringFilter {
    private static String outputPath;
    private static String outputFilePrefix;
    private static boolean collectShortStatistics;
    private static boolean collectFullStatistics;
    private static List<String> inputFiles;
    private static boolean appendToOutputFiles;
    private static HashMap<String, BufferedWriter> outputFileWriters;

    public static void main(String[] args){
        parseArguments(args);

        String integersOutputPath = outputPath + outputFilePrefix + "integers.txt";
        String floatsOutputPath = outputPath + outputFilePrefix + "floats.txt";
        String stringsOutputPath = outputPath + outputFilePrefix + "strings.txt";

        outputFileWriters = new HashMap();
        outputFileWriters.put(integersOutputPath, null);
        outputFileWriters.put(floatsOutputPath, null);
        outputFileWriters.put(stringsOutputPath, null);

        for(var inputFile: StringFilter.inputFiles){
            try(BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                String line = br.readLine();

                while (line != null){
                    try{
                        if (isInteger(line)){
                            writeToFile(line, integersOutputPath);
                        } else if (isFloat(line)){
                            writeToFile(line, floatsOutputPath);
                        } else {
                            writeToFile(line, stringsOutputPath);
                        }
                    } catch(FileNotFoundException e){
                        System.out.println("Line \"" + line
                                + "\" from input file \"" + inputFile + "\" cant be written to output file because file wa not found." +
                                "\nIt will be ignored and execution of " +
                                "program will continue. \nException message: " + e.getLocalizedMessage() + "\n");
                    }
                    catch (IOException e){
                        System.out.println("Unhandled exception occurred when writing line: \n" + line
                                + "\" from input file \"" + inputFile + "\n. Line will be ignored and execution of " +
                                "program will continue. \nException message: " + e.getLocalizedMessage() + "\n");
                    }

                    line = br.readLine();
                }
            } catch (FileNotFoundException e) {
                System.out.println("File \"" + inputFile +
                        "\" was not found. \nIt will be ignored and execution of the program will continue.\n");
            } catch (IOException e) {
                System.out.println("When processing file \"" + inputFile + "\" unhandled exception was thrown. " +
                        "This file will be ignored and execution of the program will continue. Exception message:"
                        + e.getMessage() + "\n");
            }
        }


        System.out.println("resultPath: " + outputPath);
        System.out.println("outputFilePrefix: " + outputFilePrefix);
        System.out.println("collectShortStatistics: " + collectShortStatistics);
        System.out.println("collectFullStatistics: " + collectFullStatistics);
        System.out.print("inputFiles:");
        for(String inputFile: inputFiles){
            System.out.print(" " + inputFile + ",");
        }
        System.out.println();
        System.out.println("appendToOutputFiles: " + appendToOutputFiles);
    }

    private static void parseArguments(String[] args){
        StringFilter.outputPath = "";
        StringFilter.outputFilePrefix = "";
        StringFilter.collectShortStatistics = false;
        StringFilter.collectFullStatistics = false;
        StringFilter.inputFiles = new ArrayList<>();
        StringFilter.appendToOutputFiles = false;

        for (int i = 0; i < args.length; i++){
            var arg = args[i];
            switch(arg){
                case "-o":
                    if(i == args.length - 1){
                        System.out.println("Value for -o option was not provided. Option will be ignored.");
                    }
                    else{
                        i++;
                        StringFilter.outputPath = "." + args[i];
                    }
                    break;
                case "-p":
                    if(i == args.length - 1){
                        System.out.println("Value for -p option was not provided. Option will be ignored.");
                    }
                    else{
                        i++;
                        StringFilter.outputFilePrefix = args[i];
                    }
                    break;
                case "-s":
                    StringFilter.collectShortStatistics = true;
                    break;
                case "-f":
                    StringFilter.collectFullStatistics = true;
                    break;
                case "-a":
                    StringFilter.appendToOutputFiles = true;
                    break;
                default:
                    StringFilter.inputFiles.add(args[i]);
            }
        }
    }

    private static void writeToFile(String data, String outputFileName) throws IOException {
        if (outputFileWriters.get(outputFileName) == null){
            File dir = new File(outputPath);
            if (!dir.exists()){
                dir.mkdirs();
            }
            outputFileWriters.replace(outputFileName, new BufferedWriter(new FileWriter(outputFileName, appendToOutputFiles)));
        }
        var writer = outputFileWriters.get(outputFileName);
        writer.write(data);
        writer.newLine();
        writer.flush();
    }

    public static boolean isInteger(String string){
        return string.matches("-?[1-9]\\d*|0");
    }

    public static boolean isFloat(String string){
        return string.matches("[+-]?(\\d+([.]\\d*)?([eE][+-]?\\d+)?|[.]\\d+([eE][+-]?\\d+)?)");
    }
}
