import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.json.JSONArray;

/**
 * @brief Класс контроллер диалогового окна полученных файлов
 */
public class GetController {
    private TableView<DocFile> tabl;
    private ObservableList<DocFile> list;

    @FXML
    private VBox root;

    private Button load;

    /**
     * @brief Функция инициализации диалогового окна.
     *
     * В данной функции происходит создание элементов окна (таблица и кнопка), выполнение запроса на получение списка полученных файлов,
     * парсинг JSON массива, полученного от сервера, заполнение таблицы данными.
     */
    @FXML
    public void initialize(){
        tabl = new TableView<DocFile>();
        tabl.setEditable(true);
        tabl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DocFile, String> fileNameCol = new TableColumn<>("Файл");
        TableColumn<DocFile, String> fileSendCol = new TableColumn<>("Отправитель");
        TableColumn<DocFile, String> fileDateCol = new TableColumn<>("Дата отправки");
        TableColumn<DocFile, Integer> fileFinDateCol = new TableColumn<>("Осталось дней");
        TableColumn<DocFile, Boolean> chooseCol = new TableColumn<>("Выбрать");

        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileSendCol.setCellValueFactory(new PropertyValueFactory<>("nameSend"));
        fileDateCol.setCellValueFactory(new PropertyValueFactory<>("dateSend"));
        fileFinDateCol.setCellValueFactory(new PropertyValueFactory<>("days"));

        chooseCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DocFile, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<DocFile, Boolean> param) {
                SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(param.getValue().isChoose());
                booleanProperty.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                        param.getValue().setChoose(newValue);
                    }
                });
                return booleanProperty;
            }
        });
        chooseCol.setCellFactory(new Callback<TableColumn<DocFile, Boolean>, TableCell<DocFile, Boolean>>() {
            @Override
            public TableCell<DocFile, Boolean> call(TableColumn<DocFile, Boolean> p) {
                CheckBoxTableCell<DocFile, Boolean> cell = new CheckBoxTableCell<>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        list = FXCollections.observableArrayList();
        HTTPRequest fl = new HTTPRequest(WorkPageController.user, HTTPRequest.FILEGETLIST);
        if (!fl.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.WARNING);
        else {
            if (!fl.getResponseText().contains("No files")) {
                JSONArray jArr = new JSONArray(fl.getResponseText());
                JSONArray jFileArr;
                if(jArr.length()!=0) {
                    for (int i = 0; i < jArr.length(); i++) {
                        DocFile docFile = new DocFile();
                        jFileArr = new JSONArray(jArr.get(i).toString());
                        docFile.setName(jFileArr.get(0).toString());
                        docFile.setNameSend(jFileArr.get(1).toString());
                        docFile.setIdent(jFileArr.get(4).toString());
                        docFile.setDateSend(jFileArr.get(2).toString());
                        docFile.setDateAnswer(jFileArr.get(3).toString());
                        docFile.setDays();
                        list.add(docFile);
                    }
                } else Main.alert("Файлов еще нет", Alert.AlertType.INFORMATION);
            }
        }

        tabl.setItems(list);
        tabl.getColumns().addAll(fileNameCol, fileSendCol, fileDateCol, fileFinDateCol, chooseCol);

        root.getChildren().add(tabl);

        load = new Button("Скачать");
        load.setPrefWidth(200);
        VBox.setMargin(load, new Insets(10,10,10,10));
        load.getStyleClass().addAll("lks");
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                download();
            }
        });
        root.getChildren().add(load);
        if(list.isEmpty()) load.setDisable(true);
        else load.setDisable(false);
    }

    /**
     * @brief Обработчик нажатия на кнопку скачивания файла
     */
    private void download(){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).isChoose()){
                HTTPRequest download = new HTTPRequest(list.get(i).getIdent(), WorkPageController.user, HTTPRequest.DOWNLOAD);
                download.setFileName(list.get(i).getName());
                if(!download.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.ERROR);
            }
        }
    }
}
