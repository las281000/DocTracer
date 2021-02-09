import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @brief Класс проверки введенных данных
 *
 * В данном классе происходит проверка данных, введенных пользователем при регистрации или авторизации.
 * Если какие-то данные были введены некорректно, то вызовется окно с предупреждением.
 */
public class Log_Pass {
    public static final int LOGIN = 0;
    public static final int PASSWORD = 1;

    /**
     * Функция проверяет логи или пароль на соответствие длине.
     * Длина логина должна составлять от 6 до 32 сиволов.
     * Длина пароля должна составлять от 8 до 32 символов.
     * @return Текс предупреждения
     */
    private static String checkLength(String str, int type){

        String warning = "";
        switch (type) {
            case LOGIN:
                if ((str.length() < 6) || (str.length() > 32)) {
                    warning = "Логин должен содержать от 6 до 32 символов!\n";
                }
                break;
            case PASSWORD:
                if ((str.length() < 8) || (str.length() > 32)) {
                    warning = "Пароль должен содержать от 8 до 32 символов!\n";
                }
                break;
        }
        return warning;
    }

    /**
     * Функция проверяет данные на наличие запрещенных символов (', ", /, пробел).
     * @return Текст предупреждения
     */
    private static String frbdnCharacters(String str){
        String warning ="";
        if (str.contains(" ") || str.contains("/") ||
                (str.contains("\'")) || str.contains("\"")){
            warning +="Логин и пароль НЕ МОГУТ содержать следующие символы: \' \" / и пробелы!";
        }
        return warning;
    }

    /**
     * Проверка логина и пароля на корректность.
     */
    public static boolean checkLogPass(String log, String pass) {

        String warning = checkLength(log, Log_Pass.LOGIN)+checkLength(pass, Log_Pass.PASSWORD);
        if (!frbdnCharacters(log).equals("")){
            warning += frbdnCharacters(log);
        }
        else{
            warning += frbdnCharacters(pass);
        }

        if (!warning.equals("")) { //Если есть какие то замечания
            //Вместо тоста
            Main.alert(warning, Alert.AlertType.WARNING);
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Проверка ФИО на корректность.
     */
    public static boolean checkName(String first_name, String second_name, String middel_name){
        String warning ="";
        Pattern pattern = Pattern.compile("[А-Я][а-я]+");
        Matcher matcher = pattern.matcher(first_name);
        if(!matcher.matches()){
            warning+="Ошибка в имени!\n";
        }
        matcher = pattern.matcher(second_name);
        if(!matcher.matches()){
            warning+="Ошибка в фамилии!\n";
        }
        pattern = Pattern.compile("( )|([А-Я][а-я]+)");
        matcher = pattern.matcher(middel_name);
        if(!matcher.matches()){
            warning+="Ошибка в отчестве!\n";
        }

        if (!warning.equals("")) { //Если есть какие то замечания
            //Вместо тоста
            Main.alert(warning, Alert.AlertType.WARNING);
            return false;
        }
        else{
            return true;
        }
    }
}
