import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.json.JSONArray;

/**
 * @brief Класс контроллер диалогового окна для отображения списка подчиненных, не выполнивших свои обязанности в срок
 */
public class NotifiController {
    private TableView<DocFile> tabl;
    private ObservableList<DocFile> list;
    @FXML
    private VBox root;

    /**
     * @brief Метод инициализации диалогового окна
     */
    @FXML
    public void initialize() {
        tabl = new TableView<>();
        tabl.setEditable(true);
        tabl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<DocFile, String> recName = new TableColumn<>("Получатель");
        TableColumn<DocFile, String> sendName = new TableColumn<>("Отправитель");
        TableColumn<DocFile, String> fileName = new TableColumn<>("Файл");
        TableColumn<DocFile, String> sendDate = new TableColumn<>("Дата отправки");
        TableColumn<DocFile, String> answDate = new TableColumn<>("Дата ответа");

        recName.setCellValueFactory(new PropertyValueFactory<>("nameRec"));
        sendName.setCellValueFactory(new PropertyValueFactory<>("nameSend"));
        fileName.setCellValueFactory(new PropertyValueFactory<>("name"));
        sendDate.setCellValueFactory(new PropertyValueFactory<>("dateSend"));
        answDate.setCellValueFactory(new PropertyValueFactory<>("dateAnswer"));

        list = FXCollections.observableArrayList();
        HTTPRequest nl = new HTTPRequest(WorkPageController.user, HTTPRequest.NOTIFILIST);
        if (!nl.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.ERROR);
        else {
            if (!nl.getResponseText().contains("No notify")) {
                JSONArray jArr = new JSONArray(nl.getResponseText());
                JSONArray jFileArr;
                for (int i = 0; i < jArr.length(); i++) {
                    DocFile docFile = new DocFile();
                    jFileArr = new JSONArray(jArr.get(i).toString());
                    docFile.setName(jFileArr.get(0).toString());
                    docFile.setNameSend(jFileArr.get(1).toString());
                    docFile.setDateSend(jFileArr.get(2).toString());
                    docFile.setNameRec(jFileArr.get(3).toString());
                    docFile.setDateAnswer(jFileArr.get(4).toString());
                    docFile.setDays();
                    list.add(docFile);
                }
            }
        }

        tabl.setItems(list);
        tabl.getColumns().addAll(recName, sendName, fileName, sendDate, answDate);
        root.getChildren().add(tabl);
    }
}
