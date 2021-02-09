import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

/**
 * @brief Класс контроллер окна авторизации.
 *
 * Класс контролириющий работу первого диалогового окна.
 */
public class Controller {
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;

    private String log;
    private String pas;
    private User user;

    /**
     * @brief Обработчик нажатия на кнопку авторизации.
     *
     * Если запрос на авторизацию проходит успешно, то открывается окно рабочей области.
     */
    @FXML
    public void clickInput(ActionEvent event) throws IOException {
        log = login.getText();
        pas = password.getText();

        if(!Log_Pass.checkLogPass(log, pas)){ //проверка логина и пароля
            login.setText(null);
            password.setText(null);
        }else{ //запрос на сервер
            user = new User(log, pas, "", "", "","","","","","");
            HTTPRequest author = new HTTPRequest(user, HTTPRequest.AUTHOR);
            if(!author.execute()){
                Main.alert("Ошибка соединения", Alert.AlertType.ERROR);
            }else {
                if (author.getResponseText().equals("Nobody")){
                    login.setText(null);
                    password.setText(null);
                }else{
                    try {
                        JSONArray arr = new JSONArray(author.getResponseText());
                        user.setUserKey(arr.get(0).toString());
                        user.setFirst_name(arr.get(1).toString());
                        user.setSecond_name(arr.get(2).toString());
                        user.setMiddle_name(arr.get(3).toString());
                        user.setPosition(arr.get(4).toString());
                        user.setBoss(arr.get(5).toString());
                        user.setBoss_userKey(arr.get(6).toString());
                    }catch(JSONException e){e.printStackTrace();}

                    toWorkWin(event);
                }
            }
        }
    }

    /**
     * @brief Обработчик нажатия на кнопку регистрации.
     *
     * Если запрос на получение списка сотрудников, то открывается диалоговое окно регистрации.
     */
    @FXML
    public void clickReg(ActionEvent event) throws IOException {
        HTTPRequest bl = new HTTPRequest(HTTPRequest.BOSSLIST);
        if(!bl.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.ERROR);
        else {
                RegistrationPageController.bl = bl;
                FXMLLoader loader = new FXMLLoader();
                Parent root = (Parent) loader.load(getClass().getResourceAsStream("registration_page.fxml"));
                Scene work_page = new Scene(root,1050, 600);
                work_page.getStylesheets().add("css/reg.css");
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(work_page);
                stage.show();
        }
    }

    /**
     * @brief Переход на диалоговое окно рабочей области.
     */
    private void toWorkWin(ActionEvent event) throws IOException{
        WorkPageController.user = user;
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream("work_page.fxml"));
        Scene work_page = new Scene(root,1050, 600);
        work_page.getStylesheets().add("css/work_page.css");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(work_page);
        stage.show();
    }
}
