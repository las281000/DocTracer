import javafx.scene.control.Alert;
import okhttp3.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @brief Класс соединения
 *
 * Данный класс отвечает за отправку запросов на сервер.
 * В полях класса указаны константы, определяющие, какой запрос будет послан на сервер.
 * Определены 12 запросов: запрос на регистрацию, запрос на авторизацию, запрос на загрузку файла на сервер,
 * загпрос на скачивание файла с сервера, запрос на удаление файла из системы, запрос на получение списка всех сотрудников,
 * запрос на получение списка подчиненных определенного сотрудника, запрос на удаление сотрудника и всех его подчиненных,
 * запрос на изменение данных о сотруднике, запрос на список полученных сотрудником файлов,
 * запрос на получение списка отправленных сотрудником файлов,
 * запрос на получение списка подчиненных, вовремя неответевших на полученный файл.
 *
 */
public class HTTPRequest {

    public static final int REGISTRATION = 0;
    public static final int AUTHOR = 1; //возвращает объект JSON
    public static final int UPLOAD = 2; //загрузить на сервер с записью даты ответа
    public static final int DOWNLOAD = 3;//загрузка файла на комп
    public static final int FILELIST = 4;//список отправленных файлов
    public static final int DROP = 5; //удаление файла по идентификатору
    public static final int BOSSLIST = 6; //для регистрации(список всех сотрудников)
    public static final int SUBORDINATELIST = 7; //список подчиненных
    public static final int DELSUBORDINATE = 8; //удалить сотрудника и его ветку
    public static final int EDITUSER = 9; //смена данных о сотруднике
    public static final int FILEGETLIST = 10; //список полученных файлов
    public static final int NOTIFILIST = 11; //список подчиненных

    private static final String URL = "https://calyx.space";
    private static final String REG = "/registration.php";
    private static final String LOG = "/login.php";
    private static final String FILES = "/filelist.php";
    private static final String UPLD = "/upload.php";
    private static final String DWNL = "/download.php";
    private static final String DRP = "/drop.php";
    private static final String BSLT = "/bosslist.php";
    private static final String SUBL = "/suordinatelist.php";
    private static final String DELSUB = "/delsuordinate.php";
    private static final String ED = "/edituser.php";
    private static final String GETLIST = "/filegetlist.php";
    private static final String NOT = "/notifylist.php";

    private String uri;
    private RequestBody formBody;
    private Request request;
    private Response response;
    private String responseText;

    private int task;
    private User user;
    private String path;
    private File file;
    private String date; //дата ответа на заявку
    private String fileName; //имя файла для скачивания

    private boolean success;
    private boolean pg; //POST или GET
    private String msg;

    /**
     * КОНСТРУКТОР ДЛЯ РЕГИСТРАЦИИ,ВХОДА и ПОЛУЧЕНИЯ СПИСКА ФАЙЛОВ
     * @param user Пользователь, который отправил запрос
     * @param task Тип запроса
     */
    public HTTPRequest(User user, int task) {
        this.user = user;
        this.task = task;
        responseText = "";
        path = null;
        file = null;
        msg = "";
        success = true;
    }

    /**
     * КОНСТРУКТОР ДЛЯ СПИСКА НАЧАЛЬНИКОВ
     * @param task Тип запроса
     */
    public HTTPRequest(int task){
        this.user = null;
        this.task = task;
        responseText = "";
        path = null;
        file = null;
        msg = "";
        success = true;
    }

    /**
     * КОНСТРУКТОР ДЛЯ ПОЛУЧЕНИЯ ФАЙЛА С СЕРВЕРА, ИЛИ ЗАГРУЗКИ
     * @param path Путь к файлу
     * @param user Пользователь, отправляющий запрос
     * @param task Тип запроса
     */
    public HTTPRequest(String path, User user, int task) {
        this.path = path;
        this.user = user;
        this.task = task;
        responseText = "";
        success = true;
        msg="";

        if(task == UPLOAD){
            file = new File(path);
            URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        }

    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public User getUser() {
        return this.user;
    }

    /**
     * @return Данные, полученные от сервера в следствие выполнения запроса
     */
    public String getResponseText() {return responseText;}

    /**
     * В данном методе осуществляет построение запроса, отправка запроса на сервер и получение ответа от сервера.
     */
    private void doInBackground() {

        Thread thread = new Thread(new Runnable() {
            public void run() {

                OkHttpClient client = new OkHttpClient();

                switch (task) { //составление адреса php-скрипта в зависимости от задачи
                    case REGISTRATION:
                        uri = URL + REG;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("login", user.getLogin())
                                .add("password", user.getPassword())
                                .add("first_name", user.getFirst_name())
                                .add("second_name", user.getSecond_name())
                                .add("middle_name", user.getMiddle_name())
                                .add("position", user.getPosition())
                                .add("boss_userKey", user.getBoss_userKey())
                                .build();
                        break;

                    case AUTHOR:
                        uri = URL + LOG;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("login", user.getLogin())
                                .add("password", user.getPassword())
                                .build();
                        break;

                    case UPLOAD:
                        uri = URL + UPLD;
                        pg = true;
                        formBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", file.getName(),
                                        RequestBody.create(MediaType.parse("multipart/form-data"),file))
                                .addFormDataPart("userKey", user.getUserKey())
                                .addFormDataPart("originalName", file.getName())
                                .addFormDataPart("recKey", user.getRec_userKey())
                                .addFormDataPart("send_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                                .addFormDataPart("deadline", date)
                                .build();
                        break;

                    case FILELIST:
                        uri = URL + FILES;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("userKey", user.getUserKey())
                                .build();
                        break;

                    case DOWNLOAD:
                        uri = URL + DWNL;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("userKey", user.getUserKey())
                                .add("file_id", path)
                                .build();
                        break;

                    case DROP:
                        uri = URL + DRP;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("userKey", user.getUserKey())
                                .add("file_id", path)
                                .build();
                        break;

                    case BOSSLIST:
                        uri = URL + BSLT;
                        pg = false;
                        break;

                    case SUBORDINATELIST:
                        uri = URL + SUBL;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("userKey", user.getUserKey())
                                .build();
                        break;

                    case DELSUBORDINATE:
                        uri = URL + DELSUB;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("userKey", user.getUserKey())
                                .build();
                        break;

                    case EDITUSER:
                        uri = URL + ED;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("user_key", user.getUserKey())
                                .add("login", user.getLogin())
                                .add("password", user.getPassword())
                                .add("first_name", user.getFirst_name())
                                .add("second_name", user.getSecond_name())
                                .add("middle_name", user.getMiddle_name())
                                .build();
                        break;

                    case FILEGETLIST:
                        uri = URL + GETLIST;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("userKey", user.getUserKey())
                                .build();
                        break;

                    case NOTIFILIST:
                        uri = URL + NOT;
                        pg = true;
                        formBody = new FormBody.Builder()
                                .add("userKey", user.getUserKey())
                                .build();
                        break;
                }

                if(pg) {
                    request = new Request.Builder() //Формирование запроса
                            .url(uri)
                            .post(formBody)
                            .build();
                }else{
                    request = new Request.Builder()
                            .url(uri)
                            .get()
                            .build();
                }

                try {
                    response = client.newCall(request).execute();
                    success = true;
                }
                catch (IOException e) {
                    System.out.println("Невозможно выполнить запрос (142)");
                    success = false;
                }

                if (success == true) { //если получилось установить соединение

                    try {
                        responseText = response.body().string();
                    } catch (IOException e) {
                        System.out.println("Невозможно получить результат запроса (154)");
                    }

                    //Обработка того, что пришло в ответ
                    switch (task) {
                        case REGISTRATION:
                            if (responseText.contains("Duplicate")) msg = "Пользователь с таким логином или паролем уже зарегистрирован!";
                            else msg = "Регистрация успешно завершена!";
                            break;

                        case AUTHOR:
                            if (!responseText.equals("Nobody")) {
                                System.out.println(responseText);
                            } else { //если получен отказ (Deny)
                                msg = "Неверный логин или пароль!";
                            }
                            break;

                        case FILELIST:
                            System.out.println(responseText + " - Список файлов.");
                            if (responseText.contains("No files")) {
                                msg = "Вы еще не загрузили ни одного файла.";
                            }
                            break;

                        case UPLOAD:
                            System.out.println(responseText + " - Загрузка файла.");
                            if (responseText.equals("success")) msg = "Файл успешно загружен.";
                            else msg = "Не удалось загрузить файл!";
                            break;

                        case DOWNLOAD:
                            System.out.println(responseText + " - Ссылка");
                            if (!responseText.equals("")) {
                                request = new Request.Builder().url(responseText).build();
                                try {
                                    response = client.newCall(request).execute();
                                }
                                catch (IOException e) {
                                    System.out.println("Не удалось скачать файл.");
                                    msg = "Не удалось скачать файл.";
                                }

                                System.out.println(System.getProperty("user.home") + "- Путь ");
                                //Запись полученного по URL файла
                                File output = new File(System.getProperty("user.home") + "/Downloads/" + fileName);
                                try {
                                    FileOutputStream out = new FileOutputStream(output);
                                    out.write(response.body().bytes());
                                    out.flush();
                                    out.close();
                                }
                                catch (FileNotFoundException e) {
                                    System.out.println("Директория для сохранения не найдена.");
                                    msg = "Директория для сохранения не найдена.";
                                }
                                catch (IOException e) {
                                    System.out.println("Невозможно записать файл.");
                                    msg = "Невозможно записать файл.";
                                }
                                msg = "Файл успешно загружен!\n"+output.getAbsolutePath();
                            }
                            break;

                        case DROP:
                            System.out.println(responseText+" - при дропе");
                            if(responseText.contains("Success")) {
                                msg = "Файл успешно удален.";
                                success = true;
                            }
                            else {
                                msg = "Не удалось удалить файл!";
                                success = false;
                            }
                            break;

                        case BOSSLIST:
                            System.out.println(responseText+" - Список всех сотрудников");
                            if(responseText.contains("No")){
                                msg="Сотрудников пока нет";
                            }
                            break;

                        case SUBORDINATELIST:
                            System.out.println(responseText+" - Список подчиненных");
                            if(responseText.equals("No")){
                                System.out.println("Подчиненных пока нет");
                            }
                            break;

                        case DELSUBORDINATE:
                            System.out.println(responseText+" - Пришло при удалении");
                            if(responseText.contains("Success")) {
                                msg = "Сотрудник успешно удален.";
                                success = true;
                            }
                            else {
                                msg = "Не удалось удалить сотрудника!";
                                success = false;
                            }
                            break;

                        case EDITUSER:
                            System.out.println("Изменение: "+responseText);
                            if (responseText.contains("Duplicate")) msg = "Пользователь с таким логином или паролем уже зарегистрирован!";
                            else msg = "Изменение успешно завершено!";
                            break;

                        case FILEGETLIST:
                            System.out.println(responseText + " - Список файлов.");
                            if (responseText.contains("No files")) {
                                msg = "Вам еще не прислали ни одного файла.";
                            }
                            break;

                        case NOTIFILIST:
                            System.out.println(responseText + " - Косяки.");
                            if (responseText.contains("No notify")) {
                                msg = "Вам еще не прислали ни одного файла.";
                            }
                            break;
                    }
                    client.connectionPool().evictAll();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        }
        catch(InterruptedException e){
            System.out.println("Поток сломался");
        }
    }

    /**
     * @return Запрос прошел успешно или нет
     */
    public boolean execute() {
        doInBackground();
        if((task != FILELIST)&&(!msg.equals(""))) {
            //Вместо тоста
            Main.alert(msg, Alert.AlertType.INFORMATION);
        }
        return success;
    }
}

