import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @brief Класс документа.
 *
 * Данный класс описывает некий документ, находящийся в системе.
 *
 * В полях класса содержатся данные о файле:
 * название файла (поле name),
 * отправитель файла (поле nameSend),
 * получатель файла (поле nameRec),
 * дата отправки файла (поле dateSend),
 * дата предполагаемого ответа (поле dateAnswer),
 * разница в днях между текущей датой и предполагаемой датой ответа (поле days),
 * ключ отправителя файла (поле userKeySend),
 * ключ получателя файла (поле userKeyRec),
 * уникальный идентификатор файла (поле ident) и
 * информация о том, выбран ли файл из списка (поле choose).
 *
 * В классе также определены два конструктора и методы для получения и установки значений в поля класса.
 */
public class DocFile {
    private String name; //название файла
    private String nameSend; //отправитель
    private String nameRec; //получатель
    private String dateSend; //дата отправки
    private String dateAnswer; //дедлайн для ответа
    private int days; //разница в днях между датами
    private String userKeySend; //юзеркей отправителя
    private String userKeyRec; //юзеркей получателя
    private String ident; //идентификатор файла
    private boolean choose; //выбран ли файл

    public DocFile(){}

    public DocFile(String name, String nameRec, String nameSend, String dateSend, String dateAnswer, String userKeySend, String userKeyRec, String ident){
        this.name=name;
        this.nameRec=nameRec;
        this.nameSend=nameSend;
        this.dateSend=dateSend;
        this.dateAnswer=dateAnswer;
        this.userKeySend=userKeySend;
        this.userKeyRec=userKeyRec;
        this.ident=ident;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserKeyRec(String userKeyRec) {
        this.userKeyRec = userKeyRec;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public void setNameRec(String nameRec) {
        this.nameRec = nameRec;
    }

    public void setDateAnswer(String dateAnswer) {
        this.dateAnswer = dateAnswer;
    }

    public void setDateSend(String dateSend) {
        this.dateSend = dateSend;
    }

    public void setNameSend(String nameSend) {
        this.nameSend = nameSend;
    }

    public void setUserKeySend(String userKeySend) {
        this.userKeySend = userKeySend;
    }

    /**
     * Функция устанавливает количество дней до предполагаемой даты ответа
     */
    public void setDays() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = new Date();
            Date ansDate = dateFormat.parse(dateAnswer);
            long mil = ansDate.getTime() - nowDate.getTime();
            days = (int) (mil/(24 * 60 * 60 * 1000));
        }catch (ParseException e){
            System.out.println("Ошибка преобразования даты!");
        }
    }




    public String getName() {
        return name;
    }

    public String getNameRec() {
        return nameRec;
    }

    public String getUserKeyRec() {
        return userKeyRec;
    }

    public String getIdent() {
        return ident;
    }

    public boolean isChoose() {
        return choose;
    }

    public int getDays() {
        return days;
    }

    public String getDateAnswer() {
        return dateAnswer;
    }

    public String getDateSend() {
        return dateSend;
    }

    public String getUserKeySend() {
        return userKeySend;
    }

    public String getNameSend() {
        return nameSend;
    }
}
