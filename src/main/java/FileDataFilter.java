import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

public class FileDataFilter {

    public void filter(String outputPath,
                                String outputFilePrefix,
                                boolean collectShortStatistics,
                                boolean collectFullStatistics,
                                List<String> inputFiles,
                                boolean appendToOutputFiles){

        String integersOutputPath = outputPath + outputFilePrefix + "integers.txt";
        String floatsOutputPath = outputPath + outputFilePrefix + "floats.txt";
        String stringsOutputPath = outputPath + outputFilePrefix + "strings.txt";

        HashMap<String, BufferedWriter> outputFileWriters = new HashMap();
        outputFileWriters.put(integersOutputPath, null);
        outputFileWriters.put(floatsOutputPath, null);
        outputFileWriters.put(stringsOutputPath, null);

        Statistics statistics = new Statistics();

        for(var inputFile: inputFiles){
            try(BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                String line = br.readLine();

                while (line != null){
                    try{
                        if (isInteger(line)){
                            writeToFile(line, integersOutputPath, outputFileWriters,
                                    outputPath, appendToOutputFiles);
                            statistics.updateIntegersStatistics(line, collectShortStatistics, collectFullStatistics);
                        } else if (isFloat(line)){
                            writeToFile(line, floatsOutputPath, outputFileWriters,
                                    outputPath, appendToOutputFiles);
                            statistics.updateFloatsStatistics(line, collectShortStatistics, collectFullStatistics);
                        } else {
                            writeToFile(line, stringsOutputPath, outputFileWriters,
                                    outputPath, appendToOutputFiles);
                            statistics.updateStringsStatistics(line, collectShortStatistics, collectFullStatistics);
                        }
                    } catch(FileNotFoundException e){
                        System.out.println(String.format("Строка \"%s\" из входного файла \"%s\" " +
                                "не может быть записана, поскольку выходной файл не был найден." +
                                "\nВыполнение программы будет продолжено." +
                                "\nСообщение исключения: %s\n", line, inputFile, e.getLocalizedMessage()));
                    }
                    catch (IOException e){
                        System.out.println(String.format("Во время обработки строки \n%s\" из входного файла \"%s\n. " +
                                "возникло необработанное исключение. " +
                                "Строка будет проигнорирована и выполнение программы будет продолжено. " +
                                "\nСообщение исключения: %s\n", line, inputFile, e.getLocalizedMessage()));
                    }
                    line = br.readLine();
                }
            } catch (FileNotFoundException e) {
                System.out.println(String.format("Входной файл \"%s\" не был найден." +
                        "\nФайл будет проигнорирован и выполнение программы будет продолжено.\n", inputFile));
            } catch (IOException e) {
                System.out.println(String.format("When processing input file \"%s\" unhandled exception was thrown. " +
                        "This file will be ignored and execution of the program will continue. " +
                        "Exception message:%s\n", inputFile, e.getMessage()));
            }
        }
        if (collectShortStatistics || collectFullStatistics){
            System.out.println(statistics.getStatisticsReport(collectShortStatistics, collectFullStatistics));
        }
    }

    private  void writeToFile(String data,
                              String outputFileName,
                              HashMap<String, BufferedWriter> outputFileWriters,
                              String outputPath,
                              boolean appendToOutputFiles) throws IOException {
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

    public  boolean isInteger(String string){
        return string.matches("-?[1-9]\\d*|0");
    }

    public  boolean isFloat(String string){
        return string.matches("[+-]?(\\d+([.]\\d*)?([eE][+-]?\\d+)?|[.]\\d+([eE][+-]?\\d+)?)");
    }
}
