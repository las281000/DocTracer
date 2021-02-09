import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

/**
 * @brief Класс контроллер диалогового окна личного кабинета
 */
public class LksController {
    private HashMap<String, String> map;
    private ObservableList<String> list;
    @FXML
    private ComboBox<String> people;
    @FXML
    private Button del;
    @FXML
    private Label first_name;
    @FXML
    private Label second_name;
    @FXML
    private Label middle_name;
    @FXML
    private Label position;

    /**
     * @brief Метод инициализации окна.
     *
     * В данном методе отправляется запрос на получение списка подчиненных сотрудника и заполнение полей данных пользователя.
     */
    @FXML
    public void initialize() {
        first_name.setText(WorkPageController.user.getFirst_name());
        second_name.setText(WorkPageController.user.getSecond_name());
        middle_name.setText(WorkPageController.user.getMiddle_name());
        position.setText(WorkPageController.user.getPosition());

        System.out.println(WorkPageController.user.getUserKey());

        map = new HashMap<>();
        list = FXCollections.observableArrayList();
        HTTPRequest pl = new HTTPRequest(WorkPageController.user, HTTPRequest.SUBORDINATELIST);
        if (!pl.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.WARNING);
        else {
            if (!pl.getResponseText().equals("No")) {
                JSONArray jarr;
                try {
                    jarr = new JSONArray(pl.getResponseText());
                    if(jarr.length()!=0) {
                        JSONArray jUserArr;
                        for (int i = 0; i < jarr.length(); i++) {
                            jUserArr = new JSONArray(jarr.get(i).toString());
                            list.add(String.valueOf(i) + ". " + jUserArr.get(0).toString());
                            map.put(list.get(i), jUserArr.get(1).toString());
                        }
                        people.setItems(list);
                        people.setValue(list.get(1));
                    }else Main.alert("Подчиненных пока нет", Alert.AlertType.INFORMATION);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if(map.isEmpty()) {
            del.setDisable(true);
            people.setDisable(true);
        }
        else del.setDisable(false);
    }

    /**
     * @brief Обработчик нажатия на кнопку изменения данных.
     *
     * Происходит загрузка диалогового окна изменения данных.
     */
    @FXML
    private void clickEdit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream("edit.fxml"));
        Scene edit_page = new Scene(root, 1050, 600);
        edit_page.getStylesheets().add("css/edit.css");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(edit_page);
        stage.show();
    }

    /**
     * @brief Обработчик нажатия на кнопку удаления подчиненного.
     */
    @FXML
    private void clickDel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление сотрудника");
        alert.setHeaderText("Удалить сотрудника и всех его подчиненных?");
        alert.setContentText("Сотрудник: "+people.getValue()+" и все его подчиненные будут удалены");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
        } else if (option.get() == ButtonType.OK) {
            User u = new User();
            u.setUserKey(map.get(people.getValue()));
            HTTPRequest del = new HTTPRequest(u, HTTPRequest.DELSUBORDINATE);
            if(!del.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.WARNING);
            else{
                list.remove(people.getValue());
                initialize(); //перезапускаем окно, чтобы список поменялся
            }
        } else if (option.get() == ButtonType.CANCEL) {
        } else {
        }
    }
}
