package encryption;

import java.util.ArrayList;

/**
 * Зашифровать текст
 */
public class Enciphering {
    /*Размер матрицы ключа*/
    private final static int KEY_SIZE = 6;
    /*Номер первой буквы алфавита*/
    private final static int ALPHA_START = 1072;
    /*Номер буквы ё*/
    private final static int ALPHA_YO = 1105;
    /*Ключ - матрица символов*/
    public char[][] key;

    public Enciphering() {
        this.key = createKey();
    }


    /**
     * Формирование ключа
     *
     * @return ключ
     */
    public static char[][] createKey() {
        char[][] key = new char[KEY_SIZE][KEY_SIZE];
        int n = 0;
        int i = 0, j = 0;
        /*Отслеживает, была ли буква 'ё'*/
        boolean yo = false;
        /*Для всех букв алфавита.
        * Предельная длина алфавита 33-1, потому что
        * добваление буквы 'ё' происходит вне очереди
        * (её номер 1105, а добавить в алфавит её следует
        * после 1077-го символа)*/
        while (n < Encryption.ALPHA_SIZE - 1) {
            /*Переход на следующую строку*/
            if (j == 6) {
                j = 0;
                i++;
            }
            /*Отслеживание буквы 'ё'*/
            if (n == 6 && !yo) {
                key[i][j] = (char) (ALPHA_YO);
                yo = true;
            } else {
                key[i][j] = (char) (ALPHA_START + n);
                n++;
            }
            j++;
        }
//         Отображение ключа
        /*
        for (int k = 0; k < 6; k++) {
            for (int l = 0; l < 6; l++) {
                System.out.print(key[k][l] + " ");
            }
            System.out.println();
        }
        */
        return key;
    }

    /**
     * Зашифровать текст
     * @param file список строк шифруемого текста.
     * @return список зашифрованных строк
     */
    public ArrayList<String> encrypt(ArrayList<String> file) {
        /*Перевести буквы строк текста в нижний регистр*/
        for (int i = 0; i < file.size(); i++) {
            file.set(i, file.get(i).toLowerCase());
        }
        /*Список, в котором будут храниться строки зашифрованного текста*/
        ArrayList<String> encryptedFile = new ArrayList<String>();
        /*Для всех строк текста*/
        for (String str : file) {
            /*Если строка не пустая*/
            if (!str.equals("")) {
                /*Текущая шифруемая строка*/
                String currentString = "";
                int i = 0;
                /*Для каждого символа строки*/
                while (i < str.length()) {
                    /*Переменная текущего символа*/
                    char currentChar = str.charAt(i);
                    /*Если текущий символ принадлежит алфавиту
                    * (т. е. попадает в границы значений номеров букв),
                    * то зашифровать его и добавить в текущую строку*/
                    if ((int) currentChar >= ALPHA_START &&
                            (int) currentChar < ALPHA_START + Encryption.ALPHA_SIZE &&
                            (int) currentChar != 1104) {
                        /*Зашифровать символ и добавить в текущую строку*/
                        currentString += this.getNumberOfLetter(str.charAt(i)) + " ";
                    }
                    /*Перейти к следующему символу*/
                    i++;
                }
                /*Добавить текущую зашифрованную строку в список строк*/
                encryptedFile.add(currentString);
            }
        }
        return encryptedFile;
    }

    /**
     * Зашифровать букву согласно ключу
     * @param letter шифруемая буква
     * @return номер буквы в матрице
     */
    public String getNumberOfLetter(char letter) {
        String number = "-1";
        /*Проход по матрице ключа*/
        for (int i = 0; i < KEY_SIZE; i++) {
            for (int j = 0; j < KEY_SIZE; j++) {
                /*Если найдено совпадение,
                * то вернуть номер буквы в матрице*/
                if (letter == key[i][j]) {
                    number = String.valueOf(i) + String.valueOf(j);
                    return number;
                }
            }
        }
        return number;
    }

}
