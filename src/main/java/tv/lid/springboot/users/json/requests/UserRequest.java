package tv.lid.springboot.users.json.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// данные запроса на добавление нового пользователя или редактирование
public class UserRequest {
    // идентификатор
    @JsonProperty(value = "id", required = false)
    public final Integer id;

    // логин
    @JsonProperty(value = "login", required = true)
    public final String login;

    // пароль
    @JsonProperty(value = "password", required = true)
    public final String password;

    // email
    @JsonProperty(value = "email", required = true)
    public final String email;

    // фамилия
    @JsonProperty(value = "lastName", required = true)
    public final String lastName;

    // имя
    @JsonProperty(value = "firstName", required = true)
    public final String firstName;

    // отчество
    @JsonProperty(value = "patronymic", required = false)
    public final String patronymic;

    // привилегии
    @JsonProperty(value = "privileges", required = true)
    public final Byte privileges;

    // пол
    @JsonProperty(value = "gender", required = true)
    public final Byte gender;

    // включён/выключен
    @JsonProperty(value = "isActive", required = true)
    public final Boolean isActive;

    // конструктор
    @JsonCreator
    public UserRequest(
        @JsonProperty("id")         final Integer id,
        @JsonProperty("login")      final String  login,
        @JsonProperty("password")   final String  password,
        @JsonProperty("email")      final String  email,
        @JsonProperty("lastName")   final String  lastName,
        @JsonProperty("firstName")  final String  firstName,
        @JsonProperty("patronymic") final String  patronymic,
        @JsonProperty("privileges") final Byte    privileges,
        @JsonProperty("gender")     final Byte    gender,
        @JsonProperty("isActive")   final Boolean isActive
    ) {
        this.id         = id;
        this.login      = login;
        this.password   = password;
        this.email      = email;
        this.lastName   = lastName;
        this.firstName  = firstName;
        this.patronymic = patronymic;
        this.privileges = privileges;
        this.gender     = gender;
        this.isActive   = isActive;
    }
}
