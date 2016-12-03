package encryption;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.*;

public class Encryption extends Application {
    public final static int ALPHA_SIZE = 33;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        /*Стили написания*/
        Font font = Font.font("Segoe print", FontWeight.EXTRA_BOLD, 20);
        Font commentFont = Font.font("Segoe print", FontWeight.SEMI_BOLD, 14);
        /*Разметка*/
        GridPane grid = new GridPane();
        /*Расположение разметки*/
        grid.setAlignment(Pos.CENTER);
        /*Расстояние между ячейками*/
        grid.setHgap(5);
        grid.setVgap(5);
        /*Расстояния от краёв ячеек*/
        grid.setPadding(new Insets(25, 25, 25, 25));
        /*Кнопка авторизации*/
        Button encipherButton = new Button("Зашифровать текст");
        encipherButton.setPrefSize(300, 40);
        encipherButton.setFont(font);
        /*Кнопка регистрации*/
        Button decryptButton = new Button("Расшифровать текст");
        decryptButton.setPrefSize(300, 40);
        decryptButton.setFont(font);
        /*Кнопка выхода*/
        Button exitButton = new Button("Выход");
        exitButton.setPrefSize(300, 40);
        exitButton.setFont(font);
        /*Добавление кнопок на разметку*/
        grid.add(encipherButton, 0, 0);
        grid.add(decryptButton, 0, 4);
        grid.add(exitButton, 0, 8);
        /*Текст комментария*/
        Text encipherComment = new Text("При помощи шифра замены");
        encipherComment.setFont(commentFont);
        HBox hbEncComment = new HBox(10);
        hbEncComment.setAlignment(Pos.CENTER);
        hbEncComment.getChildren().add(encipherComment);
        grid.add(hbEncComment, 0, 1);
        /*Текст комментария*/
        Text decryptComment = new Text("При помощи частотного анализа");
        decryptComment.setFont(commentFont);
        HBox hbDecComment = new HBox(10);
        hbDecComment.setAlignment(Pos.CENTER);
        hbDecComment.getChildren().add(decryptComment);
        grid.add(hbDecComment, 0, 5);
        /*Действие при нажатии кнопки "Зашифровать"*/
        encipherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                /*Отключение кнопок*/
                encipherButton.setDisable(true);
                decryptButton.setDisable(true);
                /*Считать файл*/
                FileHelper myFileHelper = new FileHelper();
                FileChooser fileChooser = new FileChooser();
                /*Исходная директория*/
                fileChooser.setInitialDirectory(new File("C:\\Users\\Александр\\IDEA\\Encryption"));
                File selectedFile = fileChooser.showOpenDialog(null);
                /*Если файл был выбран*/
                if (selectedFile != null) {
                    ArrayList<String> fileStrings = myFileHelper.readFile(selectedFile.getAbsolutePath());
                    /*Зашифровать текст*/
                    Enciphering enc = new Enciphering();
                    ArrayList<String> encryptedFile = enc.encrypt(fileStrings);
                    /*Отобразить сообщение*/
                    showAlert("Ваш текст был успешно зашифрован");

                    /*Отобразить окно диалога*/
                    Alert nextStep = new Alert(Alert.AlertType.CONFIRMATION);
                    /*Без иконки*/
                    nextStep.initStyle(StageStyle.UTILITY);
                    nextStep.getDialogPane().setPrefSize(400, 200);
                    nextStep.setTitle("Выберите действие");
                    nextStep.setHeaderText("Выберите следующий шаг");
                    nextStep.setContentText("Вы хотите сохранить или посмотреть зашифрованный текст?");
                    /*Кнопки вариантов действий*/
                    ButtonType buttonTypeOne = new ButtonType("Сохранить");
                    ButtonType buttonTypeTwo = new ButtonType("Посмотреть");
                    ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
                    nextStep.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
                    Optional<ButtonType> userChoice = nextStep.showAndWait();
                    if (userChoice.get() == buttonTypeOne) {
                        FileChooser fileSaver = new FileChooser();
                        /*Исходная директория*/
                        fileSaver.setInitialDirectory(new File("C:\\Users\\Александр\\IDEA\\Encryption"));
                        File fileToSave = fileSaver.showSaveDialog(null);
                        if (fileToSave != null) {
                            /*Зашифрованный текст записать в файл*/
                            myFileHelper.writeFile(encryptedFile, fileToSave.getAbsolutePath());
                        }
                    } else if (userChoice.get() == buttonTypeTwo) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.initStyle(StageStyle.UTILITY);
                        alert.setTitle("Подтверждение");
                        alert.setHeaderText("Отображение файла может занять какое-то время");
                        alert.setContentText("Вы уверены, что хотите продолжить?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            /*Запустить окно*/
                            Stage encipheredTextStage = new Stage();
                            new ExtraWindow(encipheredTextStage, encryptedFile,
                                    "Зашифрованный текст",
                                    "Ваш зашифрованный текст выглядит так:");
                        } else {
                            FileChooser fileSaver = new FileChooser();
                            /*Исходная директория*/
                            fileSaver.setInitialDirectory(new File("C:\\Users\\Александр\\IDEA\\Encryption"));
                            File fileToSave = fileSaver.showSaveDialog(null);
                            if (fileToSave != null) {
                            /*Зашифрованный текст записать в файл*/
                                myFileHelper.writeFile(encryptedFile, fileToSave.getAbsolutePath());
                            }
                        }
                    }
                }
                /*Включение кнопок*/
                encipherButton.setDisable(false);
                decryptButton.setDisable(false);


            }
        });
        /*Действие при нажатии кнопки "Расшифровать"*/
        decryptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                /*Отключение кнопок*/
                encipherButton.setDisable(true);
                decryptButton.setDisable(true);
                /*Выбрать файл*/
                FileHelper myFileHelper = new FileHelper();
                FileChooser fileChooser = new FileChooser();
                /*Исходная директория*/
                fileChooser.setInitialDirectory(new File("C:\\Users\\Александр\\IDEA\\Encryption"));
                File selectedFile = fileChooser.showOpenDialog(null);
                /*Если файл был выбран*/
                if (selectedFile != null) {
                    ArrayList<String> encipheredStrings = myFileHelper.readFile(selectedFile.getAbsolutePath());

                    /*Считать из файла частоту появления букв алфавита*/
                    String alphabetName = "frequency.txt";
                    LinkedHashMap<Character, Double> alphabetFreqHashMap;
                    /*Записать частоты появления букв алфавита в хэшмэп*/
                    alphabetFreqHashMap = myFileHelper.fillAlphabet(myFileHelper.readFile(alphabetName));

                    /*Дешифрировать текст*/
                    Decryption decr = new Decryption();
                    LinkedHashMap<String, Double> textFreqHashMap;
                    textFreqHashMap = decr.countFrequency(encipheredStrings);



                    System.out.println();
                    HashMap<String, Character> guessHashMap = new HashMap<String, Character>();

                    Iterator<Map.Entry<Character, Double>> itr4 = alphabetFreqHashMap.entrySet().iterator();
                    Iterator<Map.Entry<String, Double>> itr3 = textFreqHashMap.entrySet().iterator();
                    while (itr3.hasNext()){
                        Map.Entry<String, Double> currentStr = itr3.next();
                        Map.Entry<Character, Double> currentChar = itr4.next();
                        guessHashMap.put(currentStr.getKey(), currentChar.getKey());
                        System.out.print(currentStr.getKey() + "=" + currentChar.getKey() + " ");
                    }
                    System.out.println(guessHashMap);


                    ArrayList<String> decryptedStrings =
                            decr.substitution(encipheredStrings, guessHashMap);

                    Stage tableWindowStage = new Stage();
                    new TableWindow(tableWindowStage,
                            alphabetFreqHashMap, textFreqHashMap,
                            decryptedStrings, guessHashMap);


                }




//        System.out.println(textFreqHashMap);

                HashMap<String, Character> thisIsIt = new HashMap<String, Character>();




                /*Надо просто пройтись по linkedlist и связать два полученных*/
                /*А что если перевести в проценты?*/

                /*Включение кнопок*/
                encipherButton.setDisable(false);
                decryptButton.setDisable(false);
            }
        });
        /*Действие при нажатии на кнопку выхода*/
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        /*Иконка окна*/
        primaryStage.getIcons().add(new Image("file:icon.jpg"));
        /*Название окна*/
        primaryStage.setTitle("Меню");
        /*Создание сцены с разметкой*/
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        /*Отменить возможность изменения размера*/
        primaryStage.setResizable(false);
        /*Отслеживание закрытия окна*/
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        /*Показать окно*/
        primaryStage.show();
    }

    /**
     * Shows Alert with some message
     *
     * @param message message for alert
     */
    public void showAlert(String message) {
        /*Информационное сообщение*/
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        /*Название сообщения*/
        alert.setTitle("Информация");
        /*Заголовок сообщения*/
        alert.setHeaderText(null);
        /*Установить текст сообщения*/
        alert.setContentText(message);
        /*Без иконки*/
        alert.initStyle(StageStyle.UTILITY);
        /*Показать сообщение*/
        alert.showAndWait();
    }
}
