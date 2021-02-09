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
 * @brief Класс контроллер диалогового окна отправленных файлов
 */
public class SentController {

    private TableView<DocFile> tabl;
    private ObservableList<DocFile> list;

    @FXML
    private VBox root;

    private Button del;

    /**
     * @brief Инициализации окна.
     *
     *  В данной функции происходит создание элементов окна (таблица и кнопка), запрос на получение списка отправленных файлов,
     *  парсинг JSON массив, полученного от сервера, заполнение таблицы.
     */
    @FXML
    public void initialize(){
        tabl = new TableView<DocFile>();
        tabl.setEditable(true);
        tabl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DocFile, String> fileNameCol = new TableColumn<>("Файл");
        TableColumn<DocFile, String> fileRecCol = new TableColumn<>("Получатель");
        TableColumn<DocFile, String> fileDateCol = new TableColumn<>("Дата отправки");
        TableColumn<DocFile, Boolean> chooseCol = new TableColumn<>("Выбрать");

        //процентное соотношение ширины столбцов
        fileNameCol.setMaxWidth(1f * Integer.MAX_VALUE * 30 );
        fileRecCol.setMaxWidth(1f * Integer.MAX_VALUE * 50 );
        fileDateCol.setMaxWidth(1f * Integer.MAX_VALUE * 15 );
        chooseCol.setMaxWidth(1f * Integer.MAX_VALUE * 5 );

        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileRecCol.setCellValueFactory(new PropertyValueFactory<>("nameRec"));
        fileDateCol.setCellValueFactory(new PropertyValueFactory<>("dateSend"));

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

        HTTPRequest fl = new HTTPRequest(WorkPageController.user, HTTPRequest.FILELIST);
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
                        docFile.setNameRec(jFileArr.get(1).toString());
                        docFile.setDateSend(jFileArr.get(2).toString());
                        docFile.setUserKeySend(WorkPageController.user.getUserKey());
                        docFile.setIdent(jFileArr.get(3).toString());
                        list.add(docFile);
                    }
                }else Main.alert("Отправленных файлов нет", Alert.AlertType.INFORMATION);
            }
        }

        tabl.setItems(list);
        tabl.getColumns().addAll(fileNameCol, fileRecCol, fileDateCol, chooseCol);

        root.getChildren().add(tabl);

        del = new Button("Удалить");
        del.setPrefWidth(200);
        VBox.setMargin(del, new Insets(10,10,10,10));
        del.getStyleClass().addAll("lks");
        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clickDel();
            }
        });
        if((list.isEmpty())||(list==null)) del.setDisable(true);
        else del.setDisable(false);
        root.getChildren().add(del);
    }

    /**
     * @brief Обработчик нажатия на кнопку удаления файла
     */
    private void clickDel(){
        for(int i=0; i<list.size(); i++){
            System.out.println(list.get(i).getName()+" "+list.get(i).isChoose());
            if(list.get(i).isChoose()){
                HTTPRequest drop = new HTTPRequest(list.get(i).getIdent(), WorkPageController.user, HTTPRequest.DROP);
                if(!drop.execute()) Main.alert("Ошибка соединения с сервером", Alert.AlertType.ERROR);
            }
        }
    }
}
