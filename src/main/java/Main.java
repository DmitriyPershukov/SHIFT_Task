import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args){
        String outputPath = "";
        String outputFilePrefix = "";
        boolean collectShortStatistics = false;
        boolean collectFullStatistics = false;
        List<String> inputFiles = new ArrayList<>();
        boolean appendToOutputFiles = false;

        for (int i = 0; i < args.length; i++){
            var arg = args[i];
            switch(arg){
                case "-o":
                    if(i == args.length - 1){
                        System.out.println("Значение для опции -o не было предоставлено. " +
                                "Опция будет проигнорирована.");
                    }
                    else{
                        i++;
                        outputPath = "." + args[i];
                    }

                    break;
                case "-p":
                    if(i == args.length - 1){
                        System.out.println("Значение для опции -p не было предоставлено. " +
                                "Опция будет проигнорирована.");
                    }
                    else{
                        i++;
                        outputFilePrefix = args[i];
                    }
                    break;
                case "-s":
                    collectShortStatistics = true;
                    break;
                case "-f":
                    collectFullStatistics = true;
                    break;
                case "-a":
                    appendToOutputFiles = true;
                    break;
                default:
                    inputFiles.add(args[i]);
            }
        }
        
        var FileDataFilter = new FileDataFilter();
        FileDataFilter.filter(outputPath,
                                        outputFilePrefix,
                                        collectShortStatistics,
                                        collectFullStatistics,
                                        inputFiles,
                                        appendToOutputFiles);
    }
}
