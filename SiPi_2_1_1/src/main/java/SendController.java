import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @brief Класс контроллер диалогового окна отпраки файла
 */
public class SendController {
    private HashMap<String, String> map;
    private ObservableList<String> list;
    @FXML
    private Button load;
    @FXML
    private ComboBox<String> addressees;
    @FXML
    private Label fileName;
    @FXML
    private Button send;
    @FXML
    private DatePicker date_pick;
    private File file;

    /**
     * @brief Функции инициализации окна.
     *
     * В данной функции формируется список возможных получателей файла.
     */
    @FXML
    public void initialize() {
        map = new HashMap<>();
        list = FXCollections.observableArrayList();
        HTTPRequest pl = new HTTPRequest(WorkPageController.user, HTTPRequest.SUBORDINATELIST);

        if (!pl.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.WARNING);
        else {
            map.put(WorkPageController.user.getBoss(), WorkPageController.user.getBoss_userKey()); //добавляем начальника
            list.add(WorkPageController.user.getBoss());
            if (!pl.getResponseText().equals("No")) {
                JSONArray jarr;
                try {
                    jarr = new JSONArray(pl.getResponseText());
                    JSONObject jUser;
                    JSONArray jUserArr;
                    for (int i = 0; i < jarr.length(); i++) {
                        jUserArr = new JSONArray(jarr.get(i).toString());
                        list.add(String.valueOf(i) + ". " + jUserArr.get(0).toString());
                        map.put(list.get(i+1), jUserArr.get(1).toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            addressees.setItems(list);
            addressees.setValue(list.get(0));
        }

        if(map.isEmpty()) send.setDisable(true);
        else send.setDisable(false);
        if(date_pick.getValue()==null) send.setDisable(true);
    }

    /**
     * @brief Обработчик нажатия на кнопку выбора файла.
     *
     * Загружает диалогове окно выбора файла.
     */
    @FXML
    private void ckickLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор файла");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DOCX","*.docx"),
                new FileChooser.ExtensionFilter("DOC","*.doc"),
                new FileChooser.ExtensionFilter("XLSX","*.xlsx"),
                new FileChooser.ExtensionFilter("PDF","*.pdf")
        );
        file = fileChooser.showOpenDialog(new Stage());
        if(file!=null) {
            fileName.setText(file.getName());
            if(addressees.getValue()!=null){
                send.setDisable(false);
            }
        }
    }

    /**
     * @brief Обработчик нажатия на кнопку отправки
     */
    @FXML
    private void clickSend(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        WorkPageController.user.setRec_userKey(map.get(addressees.getValue()));
        HTTPRequest upload = new HTTPRequest(file.getAbsolutePath(), WorkPageController.user, HTTPRequest.UPLOAD);
        upload.setDate(date_pick.getValue().format(formatter));
        if(!upload.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.ERROR);
    }
}
