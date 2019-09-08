package stack.study.service;

import stack.study.model.Currency;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FileService {
    public static final String SEP = System.getProperty("file.separator");
    public static final String FILE_DIR = System.getProperty("user.dir") + SEP + "files";
    public static final String MAIN_DIR = System.getProperty("user.dir") + SEP;

    public static Set getCurrencyFromFile(String fileName) {
        Set<Currency> currencies = new HashSet<>();
        try (
                FileReader fileReader = new FileReader(FILE_DIR + SEP + fileName);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                Currency currency = parseCurrensyLine(line);
                currencies.add(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencies;
    }

    private static Currency parseCurrensyLine(String line) {
        Currency currency = new Currency();
        String[] words = line.split(" ");
        for (int i = 0; i < words.length; i++) {
            currency.setName(words[0]);
            currency.setShortName(words[1]);
            currency.setSymbol(words[2]);
            currency.setPrice(Integer.valueOf(words[3]));
        }
        return currency;
    }

    public static void writeCurrenciesToFile(Collection<Currency> currencies, String fileName) {

        try (FileWriter fileWriter = new FileWriter(FILE_DIR + SEP + fileName)) {
            String lines = "";
            for (Currency currency : currencies) {
                lines += currency.getShortName() + " " + currency.getSymbol() +
                        " Sell: " + formatNumber(currency.getPrice() * 1.05 / 100) + " Buy: " + formatNumber(currency.getPrice() * 1.1 / 100) + "\n";
            }
            fileWriter.write(lines);
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatNumber(double number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.toString();
    }

    public static byte[] getBytesFromFile(String fileNaame) {
        File file = new File(FILE_DIR + SEP + fileNaame);
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static void checkDir(String newDir){
        File file = new File(MAIN_DIR + SEP + newDir);
        if (!file.exists()){
            file.mkdir();
        }
    }

    public static void writeBytesToFile(byte[] bytes, String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_DIR + SEP + fileName)) {
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeBytesToFile(byte[] bytes, String newDir, String fileName) {
        checkDir(newDir);
        try (FileOutputStream fileOutputStream = new FileOutputStream(MAIN_DIR + SEP + newDir + SEP + fileName)) {
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(String sourceName, String copyName){
        byte[] bytes = getBytesFromFile(sourceName);
        writeBytesToFile(bytes, copyName);
    }

    public static void moveFile(String fileName, String newDir){
        byte[] bytes = getBytesFromFile(fileName);
        writeBytesToFile(bytes, newDir, fileName);
        File originFile = new File(FILE_DIR + SEP + fileName);
        originFile.delete();
    }
}
