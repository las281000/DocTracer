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

import java.io.IOException;

/**
 * @brief Класс контроллер диалогового окна изменения данных о пользователе
 */
public class EditController {
    @FXML
    private TextField first_name;
    @FXML
    private TextField second_name;
    @FXML
    private TextField middle_name;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password1;
    @FXML
    private PasswordField password2;

    /**
     * @brief Обработчик нажатия на кнопку назад
     */
    public void clickBack(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream("work_page.fxml"));
        Scene work_page = new Scene(root, 1050, 600);
        work_page.getStylesheets().add("css/work_page.css");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(work_page);
        stage.show();
    }

    /**
     * @brief Обработчик нажатия на кнопку смены данных
     */
    public void clickEdit(ActionEvent event) throws IOException {
        if (!Log_Pass.checkName(first_name.getText(), second_name.getText(), middle_name.getText())) {
            first_name.setText(null);
            second_name.setText(null);
            middle_name.setText(null);
        } else {
            if (!Log_Pass.checkLogPass(login.getText(), password1.getText())) {
                login.setText(null);
                password1.setText(null);
            } else {
                if (!password1.getText().equals(password2.getText())) {
                    password1.setText(null);
                    password2.setText(null);
                    Main.alert("Пароли не совпали", Alert.AlertType.WARNING);
                } else {
                    User u = new User();
                    u.setFirst_name(first_name.getText());
                    u.setSecond_name(second_name.getText());
                    u.setMiddle_name(middle_name.getText());
                    u.setLogin(login.getText());
                    u.setPassword(password1.getText());
                    u.setPosition(WorkPageController.user.getPosition());
                    u.setUserKey(WorkPageController.user.getUserKey());
                    u.setBoss(WorkPageController.user.getBoss());
                    u.setBoss_userKey(WorkPageController.user.getBoss_userKey());
                    HTTPRequest edit = new HTTPRequest(u, HTTPRequest.EDITUSER);

                    if(!edit.execute()) Main.alert("Ошибка соединения с сервером!", Alert.AlertType.WARNING);
                    else{
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = (Parent) loader.load(getClass().getResourceAsStream("start_page.fxml"));
                        Scene start_page = new Scene(root, 1050, 600);
                        start_page.getStylesheets().add("css/start.css");
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(start_page);
                        stage.show();
                    }
                }
            }
        }
    }
}
