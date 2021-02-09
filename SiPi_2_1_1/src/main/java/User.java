/**
 * @brief Класс пользователя.
 *
 * В данном классе хранятся личные данные пользователья:
 * логин (поле login),
 * пароль (поле password),
 * ключ (поле userKey),
 * ФИО (поля first_name, second_name, middle_name),
 * должность (поле position),
 * имя и должность начальника (поле boss),
 * ключ начальника (поле boss_userKey).
 *
 * В классе определены 3 конструктора, методы для получения значений полей класса
 * и методы для установки значений в поля класса.
 */
public class User {
    private String login;
    private String password;
    private String userKey;
    private String first_name;
    private String second_name;
    private String middle_name;
    private String position;

    private String boss;
    private String boss_userKey;

    private String rec_userKey;

    public User(){}

    public User(String login, String password, String userKey, String first_name, String second_name, String middle_name, String position, String boss, String boss_userKey, String rec_userKey){
        this.login = login;
        this.password = password;
        this.userKey = userKey;
        this.first_name = first_name;
        this.second_name = second_name;
        this.middle_name = middle_name;
        this.position = position;
        this.boss = boss;
        this.boss_userKey = boss_userKey;
        this.rec_userKey = rec_userKey;
    }

    public User(String login, String password, String userKey){
        this.login = login;
        this.password = password;
        this.userKey = userKey;
    }

    public  String getLogin(){
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getBoss_userKey() {
        return boss_userKey;
    }

    public String getPosition() {
        return position;
    }

    public String getBoss() {
        return boss;
    }

    public String getRec_userKey() {
        return rec_userKey;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public void setBoss_userKey(String boss_userKey) {
        this.boss_userKey = boss_userKey;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public void setRec_userKey(String rec_userKey) {
        this.rec_userKey = rec_userKey;
    }
}
