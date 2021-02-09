import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * @brief Класс контроллер диалогового окна рабочей области
 */
public class WorkPageController {
    @FXML
    private BorderPane workZone;
    @FXML
    private Button lks;
    @FXML
    private Button send;
    @FXML
    private Button sent;
    @FXML
    private Button get;
    @FXML
    private Button notifi;
    @FXML
    private Button exit;

    public static User user;

    @FXML
    public void initialize() throws IOException{
        ImageView img = new ImageView("images/lks.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        lks.setGraphic(img);

        img = new ImageView("images/send.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        send.setGraphic(img);

        img = new ImageView("images/sent.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        sent.setGraphic(img);

        img = new ImageView("images/poluch.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        get.setGraphic(img);

        img = new ImageView("images/notifi.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        notifi.setGraphic(img);

        img = new ImageView("images/exit.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        exit.setGraphic(img);

        try {
            workZone.setCenter(FXMLLoader.load(getClass().getResource("lks.fxml")));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Обработчик нажатия на кнопки
     *
     * В зависимости от нажатой кнопки загружается определенный fxml файл.
     */
    public void clickButton(ActionEvent event) throws IOException {
        String butID = ((Button)event.getSource()).getId();
        if(butID.equals("exit")){
            FXMLLoader loader = new FXMLLoader();
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = (Parent) loader.load(getClass().getResourceAsStream("start_page.fxml"));
            primaryStage.setTitle("DockTracer");
            Scene scene = new Scene(root, 1050, 600);
            scene.getStylesheets().add("css/start.css");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else {
            try {
                workZone.setCenter(FXMLLoader.load(getClass().getResource(butID + ".fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void setUser(User userA){
        user = userA;
    }

    public BorderPane getWorkZone(){
        return workZone;
    }
}
