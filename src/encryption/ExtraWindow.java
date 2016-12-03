package encryption;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class ExtraWindow extends Application {
    private ArrayList<String> text;
    private String title;
    private String header;

    public ExtraWindow(Stage stage, ArrayList<String> text, String title, String header) {
        this.title = title;
        this.header = header;
        this.text = text;
        this.start(stage);
    }

    @Override
    public void start(Stage primaryStage) {
        /*Стили написания*/
        Font font = Font.font("Segoe print", FontWeight.NORMAL, 20);
        Font textFont = Font.font("Segoe print", FontWeight.NORMAL, 14);
        /*Разметка*/
        GridPane grid = new GridPane();
        /*Расположение разметки*/
        grid.setAlignment(Pos.CENTER);
        /*Расстояние между ячейками*/
        grid.setHgap(10);
        grid.setVgap(10);
        /*Расстояния от краёв ячеек*/
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label message = new Label(header);
        message.setFont(font);
        grid.add(message, 0, 0);

        TextArea encipheredText = new TextArea();
        encipheredText.setPrefSize(1000, 500);
        for (String str : text) {
            encipheredText.appendText(str + "\n");
        }
        encipheredText.setFont(textFont);
        grid.add(encipheredText, 0, 1);


        Button saveButton = new Button("Сохранить");
        saveButton.setFont(font);

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(saveButton);
        grid.add(hBox, 0, 2);

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                /*Считать файл*/
                FileHelper myFileHelper = new FileHelper();
                FileChooser fileChooser = new FileChooser();
                /*Исходная директория*/
                fileChooser.setInitialDirectory(new File("C:\\Users\\Александр\\IDEA\\Encryption"));
                File selectedFile = fileChooser.showOpenDialog(null);
                /*Если файл был выбран*/
                if (selectedFile != null) {
                    myFileHelper.writeFile(text, selectedFile.getAbsolutePath());
                }
            }
        });

        /*Иконка окна*/
        primaryStage.getIcons().add(new Image("file:icon.jpg"));
        /*Название окна*/
        primaryStage.setTitle(title);
        /*Создание сцены с разметкой*/
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        /*Отменить возможность изменения размера*/
        primaryStage.setResizable(false);
        /*Показать окно*/
        primaryStage.show();
    }
}
