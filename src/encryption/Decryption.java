package encryption;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Расшифровка русскоязычного текста
 */
public class Decryption {
    /*Хэшсет с уникальными значениями считанных символов*/
    HashSet<String> numbers;
    /*Хэшмэп с вычисленными частотами прочитанных символов*/
    HashMap<String, Double> numFrequency;
    /*Общее количество символов в тексте*/
    int amountOfLetters;

    public Decryption() {
        this.numbers = new HashSet<String>();
        this.numFrequency = new HashMap<String, Double>();
        amountOfLetters = 0;
    }

    /**
     * Заполнить хэшсет считанных символов
     *
     * @param strings список строк текста, который подлежит дешифровке
     */
    private void fillNumbersHashSet(ArrayList<String> strings) {
        /*Цикл исполняется до тех пор, пока размер хэшсета
          не будет равен размеру алфавита.*/
        for (String str : strings) {
            /*Строка символов разбивается на части,
             *разделённые пробелом*/
            String[] parts = str.split(" ");
            /*Каждая часть представляет собой символ -
             * - зашифрованную букву. Если символ не пустое
             * значение, то он добавляется в хэшсет.*/
            for (String partStr : parts) {
                /*Если встретившийся символ не пустой,
                * то добавить его в хэшсет.*/
                if (!partStr.equals("")) {
                    /*Коллекция хэшсет гарантирует, что
                     *каждый символ в коллекции будет уникален.*/
                    numbers.add(partStr);
                    /*Прекращение цикла при заполнении хэшсета*/
                    if (numbers.size() == Encryption.ALPHA_SIZE - 1) {
                        break;
                    }
                }
            }
            /*Прекращение цикла при заполнении хэшсета*/
            if (numbers.size() == Encryption.ALPHA_SIZE - 1) {
                break;
            }
        }
        /*!Примечание!*/
        /*
        * В тексте, используемом в качестве теста, отсутствует
        * буква 'ё', поэтому предельная величина хэшсета равна
        * величина алфавита - 1.
        * */
    }

    /**
     * Вычисляет частоту появления символов в тексте.
     *
     * @param strings список строк текста, который подлежит дешифровке
     * @return связанный хэшмэп символ-частота, отсортированный
     * в порядке убывания по частоте
     */
    public LinkedHashMap<String, Double> countFrequency(ArrayList<String> strings) {
        /*Сначала заполняется хэшсет символов*/
        fillNumbersHashSet(strings);
        /*В хэшмэп заносятся уникальные символы в качестве ключей.
         *Значение частоты пока не подсчитано, поэтому оно null. */
        for (String str : numbers) {
            numFrequency.put(str, null);
        }
        /*Цикл по всем строкам списка строк*/
        for (String str : strings) {
            /*Каждая строка разбивается на символы, разделённые пробелом*/
            String[] parts = str.split(" ");
            /*Для каждого символа*/
            for (String partStr : parts) {
                /*Временное значение - частота текущего символа*/
                Double currentValue = numFrequency.get(partStr);
                /*Если встретившийся символ не пустое значение*/
                if (!partStr.equals("")) {
                    /*Если символ встретился в первый раз - присвоить
                     * его частоте значение ноль. Оно будет инкрементировано
                     * далее.*/
                    if (currentValue == null) {
                        currentValue = 0.0;
                    }
                    /*Инкремент частоты конкретного встретившегося символа*/
                    numFrequency.put(partStr, currentValue + 1.0);
                    /*Инкремент общего количества символов*/
                    amountOfLetters++;
                }
            }
        }

        /*Вот здесь надо подразобраться: ...*/
        /*Хэшмэп*/
        HashMap<Character, Double> charFrequency = new HashMap<Character, Double>();

//
        for (String str : numbers) {
            double currentDouble = numFrequency.get(str);
            numFrequency.put(str, currentDouble / amountOfLetters);
//            charFrequency.put(returnChar(str), currentDouble / amountOfLetters);
//                   , new BigDecimal(currentDouble / amountOfLetters).setScale(5, RoundingMode.UP).doubleValue());
        }
//        System.out.println(numbers);
//        System.out.println(numbers.size());
//        System.out.println(numFrequency);
//        System.out.println(numFrequency.size());
//        System.out.println(FileHelper.sortByValues(charFrequency));
//        return FileHelper.sortByValues(charFrequency);
        return FileHelper.sortByValues(numFrequency);
    }

    public ArrayList<String> substitution(ArrayList<String> encipheredStrings,
                                          HashMap<String, Character> guess) {
        ArrayList<String> decryptedStrings = new ArrayList<String>();
        for (String str : encipheredStrings) {
            String[] parts = str.split(" ");
            String currentString = "";
            for (String part : parts) {
                if(!part.equals("")){
                    currentString+=guess.get(part);
                }
            }
            decryptedStrings.add(currentString);
        }
        return decryptedStrings;
    }

    public static char returnChar(String string) {
        char[] qwe = string.toCharArray();
        int q = Character.getNumericValue(qwe[0]);
        int w = Character.getNumericValue(qwe[1]);
        return Enciphering.createKey()[q][w];
    }
}
