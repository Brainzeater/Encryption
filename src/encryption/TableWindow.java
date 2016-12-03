package encryption;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TableWindow extends Application {
    private HashMap<Character, Double> alphabet;
    private HashMap<String, Double> newAlphabet;
    private ArrayList<String> decryptedStrings;
    private HashMap<String, Character> guess;

    public TableWindow(Stage primaryStage,
                       HashMap<Character, Double> alphabet,
                       HashMap<String, Double> newAlphabet,
                       ArrayList<String> decryptedStrings,
                       HashMap<String, Character> guess) {
        this.alphabet = alphabet;
        this.newAlphabet = newAlphabet;
        this.decryptedStrings = decryptedStrings;
        this.guess = guess;
        start(primaryStage);
    }

    @Override
    public void start(Stage primaryStage) {
        /*Стили написания*/
        Font font = Font.font("Segoe print", FontWeight.EXTRA_BOLD, 16);
        Font lbFont = Font.font("Segoe print", FontWeight.BOLD, 14);
        Font btnFont = Font.font("Segoe print", FontWeight.NORMAL, 20);
        /*Разметка*/
        GridPane grid = new GridPane();
        /*Расположение разметки*/
        grid.setAlignment(Pos.CENTER);
        /*Расстояние между ячейками*/
        grid.setHgap(20);
        grid.setVgap(0);
        /*Расстояния от краёв ячеек*/
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label leftSide = new Label("Исходный алфавит:");
        leftSide.setFont(font);
        grid.add(leftSide, 0, 0);

        Label rightSide = new Label("Полученный алфавит:");
        rightSide.setFont(font);
        grid.add(rightSide, 1, 0);

        Iterator<Map.Entry<Character, Double>> itr1 = alphabet.entrySet().iterator();
        Iterator<Map.Entry<String, Double>> itr2 = newAlphabet.entrySet().iterator();
        Iterator<Map.Entry<String, Character>> itr3 = guess.entrySet().iterator();

        int n = 1;
        int matches = 0;
        ArrayList<Character> rightChars = new ArrayList<Character>();
        while (n <= Encryption.ALPHA_SIZE) {
            Label leftLabel = new Label();
            leftLabel.setFont(lbFont);
            Map.Entry<Character, Double> currentLeftElement = itr1.next();
            leftLabel.setText(n + ": " + currentLeftElement.getKey()
                    + " - " + String.format("%.5f", currentLeftElement.getValue()));

            HBox hBoxLeft = new HBox(0);
            hBoxLeft.setAlignment(Pos.CENTER_RIGHT);
            hBoxLeft.getChildren().add(leftLabel);

            grid.add(hBoxLeft, 0, n);

            Label rightLabel = new Label();
            if (itr2.hasNext()) {
                Map.Entry<String, Double> currentRightElement = itr2.next();
                rightLabel.setFont(lbFont);
                rightLabel.setText(n + ": " + Decryption.returnChar(currentRightElement.getKey())
                        + " - " + String.format("%.5f", currentRightElement.getValue()));
                grid.add(rightLabel, 1, n);

                Map.Entry<String, Character> currentElement = itr3.next();
                if(Decryption.returnChar(currentElement.getKey()) ==
                        currentElement.getValue()){
                    matches++;
                    rightChars.add(currentElement.getValue());
                }
            }
            n++;
        }


        System.out.println(matches);
        System.out.println((float)((matches*100)/Encryption.ALPHA_SIZE));
        Button textButton = new Button("Показать текст");
        textButton.setFont(btnFont);
        grid.add(textButton, 0, n + 1);

        Button newIteration = new Button("Новая итерация");
        newIteration.setFont(btnFont);
        grid.add(newIteration, 1, n + 1);

        textButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                /*Запустить окно*/
                Stage decryptedTextStage = new Stage();
                new ExtraWindow(decryptedTextStage, decryptedStrings,
                        "Расшифрованный текст",
                        "Ваш расшифрованный текст выглядит так:");
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

        String message = "Совпало букв: " + matches + "\n";
        message += "Совпавшие буквы: \n";
        for(Character ch : rightChars){
            message += ch + "\n";
        }
        message += "Это составляет " + (int)(float)((matches*100)/Encryption.ALPHA_SIZE) + " %";
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
