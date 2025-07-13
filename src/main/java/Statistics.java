import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Statistics {
    private int integersCount = 0;
    private int floatsCount = 0;
    private int stringsCount = 0;
    private BigInteger minInteger = null;
    private BigInteger maxInteger = null;
    private BigInteger integersSum = BigInteger.ZERO;
    private BigDecimal minFloat = null;
    private BigDecimal maxFloat = null;
    private BigDecimal floatsSum = BigDecimal.ZERO;
    private int minStringLength = Integer.MAX_VALUE;
    private int maxStringLength = Integer.MIN_VALUE;
    public void updateIntegersStatistics(String line,
                                         boolean collectShortStatistics,
                                         boolean collectFullStatistics){
        if (collectShortStatistics || collectFullStatistics){
            integersCount++;
        }
        if (collectFullStatistics){
            BigInteger integer = new BigInteger(line);
            if (minInteger == null || integer.compareTo(minInteger) < 0){
                minInteger = integer;
            }
            if (maxInteger == null || integer.compareTo(maxInteger) > 0){
                maxInteger = integer;
            }
            integersSum = integersSum.add(integer);
        }
    }

    public void updateFloatsStatistics(String line,
                                       boolean collectShortStatistics,
                                       boolean collectFullStatistics){
        if (collectShortStatistics || collectFullStatistics){
            floatsCount++;
        }
        if (collectFullStatistics){
            BigDecimal floatData = new BigDecimal(line);
            if (minFloat == null || floatData.compareTo(minFloat) < 0){
                minFloat = floatData;
            }
            if (maxFloat == null || floatData.compareTo(maxFloat) > 0){
                maxFloat = floatData;
            }
            floatsSum = floatsSum.add(floatData);
        }
    }

    public void updateStringsStatistics(String line,
                                        boolean collectShortStatistics,
                                        boolean collectFullStatistics){
        if (collectShortStatistics || collectFullStatistics){
            stringsCount++;
        }
        if (collectFullStatistics){
            if (line.length() > maxStringLength){
                maxStringLength = line.length();
            }
            if (line.length() < minStringLength){
                minStringLength = line.length();
            }
        }
    }

    public String getStatisticsReport(boolean collectShortStatistics, boolean collectFullStatistics){
        StringBuilder integersReport = new StringBuilder();
        StringBuilder floatsReport = new StringBuilder();
        StringBuilder stringsReport = new StringBuilder();
        if (collectShortStatistics || collectFullStatistics){
            integersReport.append("По целым числам:\n");
            integersReport.append(String.format("Количество: %d\n", integersCount));
            floatsReport.append("По вещественным числам:\n");
            floatsReport.append(String.format("Количество: %d\n", floatsCount));
            stringsReport.append("По строкам:\n");
            stringsReport.append(String.format("Количество: %d\n", stringsCount));
        }
        if (collectFullStatistics){
            if (integersCount > 0){
                integersReport.append(String.format("Минимальное значение: %s\n", minInteger));
                integersReport.append(String.format("Максимальное значение: %s\n", maxInteger));
                integersReport.append(String.format("Сумма: %s\n", integersSum));
                integersReport.append(String.format("Среднее: %s\n", getIntegersAverage()));
            } else {
                integersReport.append("Так как количество записанных целых чисел равно нулю, " +
                        "подробная статистика не будет показана.\n");
            }
            if (floatsCount > 0){
                floatsReport.append(String.format("Минимальное значение: %s\n", minFloat));
                floatsReport.append(String.format("Максимальное значение: %s\n", maxFloat));
                floatsReport.append(String.format("Сумма: %s\n", floatsSum));
                floatsReport.append(String.format("Среднее: %s\n", getFloatsAverage()));
            } else {
                floatsReport.append("Так как количество записанных вещественных чисел равно нулю, " +
                        "подробная статистика не будет показана.\n");
            }
            if (stringsCount > 0){
                stringsReport.append(String.format("Минимальная длина строки: %s\n", minStringLength));
                stringsReport.append(String.format("Максимальная длина строки: %s\n", maxStringLength));
            } else {
                stringsReport.append("Так как количество записанных строк равно нулю, " +
                                        "подробная статистика не будет показана.\n");
            }
        }
        return String.format("Статистика: \n----------------------\n%s----------------------\n%s----------------------\n%s",
                integersReport, floatsReport, stringsReport);
    }
    private BigDecimal getIntegersAverage() {
        return new BigDecimal(integersSum).divide(BigDecimal.valueOf(integersCount), 3, RoundingMode.HALF_UP);
    }
    private BigDecimal getFloatsAverage() {
        return floatsSum.divide(BigDecimal.valueOf(floatsCount), 3, RoundingMode.HALF_UP);
    }
}
