import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

/**
 * @brief Класс контроллер окна регистрации.
 */
public class RegistrationPageController {
    @FXML
    private TextField first_name;
    @FXML
    private TextField second_name;
    @FXML
    private TextField middle_name;
    @FXML
    private TextField position;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password1;
    @FXML
    private PasswordField password2;
    @FXML
    private ComboBox<String> boss;

    private ObservableList<String> boss_list;
    private HashMap<String, String> boss_map;
    public static HTTPRequest bl;

    /**
     * Функция, которая вызывается при загрузке диалогового окна регистрации.
     */
    @FXML
    public void initialize() throws IOException{
            JSONArray JSONlist;
            boss_list = FXCollections.observableArrayList();
            boss_map = new HashMap<>();
            if (!bl.getResponseText().equals("")) {
                try {
                    JSONlist = new JSONArray(bl.getResponseText());
                    JSONArray jArr;
                    //Распарсим JSON
                    for (int i = 0; i < JSONlist.length(); i++) {
                        try {
                            jArr = new JSONArray(JSONlist.get(i).toString());
                            boss_list.add(String.valueOf(i) + ". " + jArr.get(0).toString());
                            boss_map.put(String.valueOf(i) + ". " + jArr.get(0).toString(), jArr.get(1).toString());
                        } catch (JSONException e) {
                            System.out.println("1: "+e.getMessage());
                        }
                    }
                } catch (JSONException e) {
                    System.out.println("2: "+e.getMessage());
                }
            }
            if (boss_list.isEmpty()){
                boss.setDisable(true);
            }
            else {
                boss.setDisable(false);
                boss.setItems(boss_list);
            }


    }

    /**
     * @brief Обработчик нажатия кнопки назад
     */
    @FXML
    public void clickBack(ActionEvent event) throws IOException {
        toFirstWin(event);
    }

    /**
     * @brief Обработчик нажатия на кнопку регистрации
     *
     * В методе происходит проверка введенных данных и запрос на регистрацию пользователя.
     */
    @FXML
    public void clickReg(ActionEvent event) throws IOException{
        if(!Log_Pass.checkName(first_name.getText(), second_name.getText(), middle_name.getText())){
            first_name.setText(null);
            second_name.setText(null);
            middle_name.setText(null);
        }else{
            if(!Log_Pass.checkLogPass(login.getText(), password1.getText())){
                login.setText(null);
                password1.setText(null);
            }else{
                if(!password1.getText().equals(password2.getText())){
                    password1.setText(null);
                    password2.setText(null);
                    Main.alert("Пароли не совпали", Alert.AlertType.WARNING);
                }else{
                    System.out.println(login.getText()+" "+password1.getText()+" "+first_name.getText()+" "+second_name.getText()+" "+middle_name.getText()+" "+position.getText());
                    User r;
                    if(boss_list.isEmpty()){
                        System.out.println("Директор: ");
                        r = new User(login.getText(), password1.getText(), "",
                                first_name.getText(), second_name.getText(), middle_name.getText(),
                                position.getText(), "", "@", "");
                    }else {
                        r = new User(login.getText(), password1.getText(), "",
                                first_name.getText(), second_name.getText(), middle_name.getText(),
                                position.getText(), boss.getValue(), boss_map.get(boss.getValue()), "");
                    }
                    HTTPRequest reg = new HTTPRequest(r, HTTPRequest.REGISTRATION);

                    if(!reg.execute()) Main.alert("Ошибка соединения с сервером!", Alert.AlertType.WARNING);
                    else{
                        toFirstWin(event);
                    }
                }
            }
        }
    }

    /**
     * @brief Переход на окно авторизации
     */
    private void toFirstWin(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream("start_page.fxml"));
        primaryStage.setTitle("DockTracer");
        Scene scene = new Scene(root, 1050, 600);
        scene.getStylesheets().add("css/start.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setHTTPRequest(HTTPRequest bl){
        this.bl = bl;
        System.out.println("Получили: "+bl.getResponseText());
    }
}
