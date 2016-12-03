package encryption;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.*;

public class FileHelper {
    private final static int FIRST_CHAR = 65279;
    private FileWriter myFileWriter;
    private BufferedWriter myBufferedWriter;
    private FileReader myFileReader;
    private BufferedReader myBufferedReader;

    /**
     * Save User
     */
    public void writeFile(ArrayList<String> stringArray, String fileName) {
        try {
            myFileWriter = new FileWriter(fileName);
            myBufferedWriter = new BufferedWriter(myFileWriter);
            for (String str : stringArray) {
                myBufferedWriter.write(str + "\r\n");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                myBufferedWriter.flush();
                myBufferedWriter.close();
                myFileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Reads data
     *
     * @return massive of data
     */
    public ArrayList<String> readFile(String fileName) {
        ArrayList<String> tempArray = new ArrayList<String>();
        try {
            myFileReader = new FileReader(fileName);
            myBufferedReader = new BufferedReader(myFileReader);
            while (true) {
                String line = myBufferedReader.readLine();
                if (line == null) break;
                /*Разбить считанные данные по символу новой строки*/
                String[] parts = line.split("\n");
                for (String str : parts) {
                    tempArray.add(str);
                }
            }
            return tempArray;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            /*Сообщение об ошибке*/
            Alert alert = new Alert(Alert.AlertType.ERROR);
            /*Название сообщения*/
            alert.setTitle("Error");
            /*Заголовок сообщения*/
            alert.setHeaderText(null);
            /*Установить текст сообщения*/
            alert.setContentText("File is not found");
            /*Без иконки*/
            alert.initStyle(StageStyle.UTILITY);
            /*Показать сообщение*/
            alert.showAndWait();
        } finally {
            try {
                myBufferedReader.close();
                myFileReader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Заполняет хэшмэп буквами алфавита и соответствующими частотами их появления в текста
     *
     * @param letterFreq массив строк, считанных из файла
     * @return алфавит с частотами появления букв
     */
    public LinkedHashMap<Character, Double> fillAlphabet(ArrayList<String> letterFreq) {
        /*Хэшмэп, содержащий буквы и соответствующие им частоты появления*/
        HashMap<Character, Double> alphabetFreqHashMap = new HashMap<Character, Double>();
        /*Первый char файла необходимо удалить*/
        letterFreq.set(0, letterFreq.get(0).replace((char) FIRST_CHAR, ' ').trim());
        /*Последовательный перебор строк*/
        for (String str : letterFreq) {
            /*Разбить каждую строку по символу "-"*/
            String[] parts = str.split("-");
            /*Первая часть - буква*/
            Character letter = parts[0].charAt(0);
            /*Вторая часть - частота*/
            Double frequency = Double.parseDouble(parts[1]);
            /*Поместить букву и её частоту в хэшмэп*/
            alphabetFreqHashMap.put(letter, frequency);
        }

        return sortByValues(alphabetFreqHashMap);
    }

    /**
     * (From Internet)
     *
     * @param map
     * @return
     */
    public static LinkedHashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        LinkedHashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
