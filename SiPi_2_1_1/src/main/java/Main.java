import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * @brief Основной класс.

 * Запускает работу программы
 */

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    /**
     * Функция, вызывающая первое диалоговое окно.
     * В функции указан fxml файл, в котором указаны основные объекты сцены и css файл, отвечающий за дизайн элементов сцены.окна
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream("start_page.fxml"));
        stage.setTitle("DocTracer");
        Scene scene = new Scene(root, 1050, 600);
        scene.getStylesheets().add("css/start.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Функция отвечает за показ предупредительных сообщений пользователю.
     * @param s Текст сообщения.
     * @param x Тип сообщения (информационное, предупредительное или сообщение об ошибке)
     */
    public static void alert(String s, Alert.AlertType x) {
        Alert alert = new Alert(x);
        alert.setTitle("Ошибка");
        alert.setContentText(s);
        alert.showAndWait().ifPresent((rs) -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }
}
