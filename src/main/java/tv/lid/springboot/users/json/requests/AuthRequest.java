package tv.lid.springboot.users.json.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// данные запроса на авторизацию
public class AuthRequest {
    // логин или email
    @JsonProperty(value = "loginOrEmail", required = true)
    public final String loginOrEmail;

    // пароль
    @JsonProperty(value = "password", required = true)
    public final String password;

    // конструктор
    @JsonCreator
    public AuthRequest(
        @JsonProperty("loginOrEmail") final String loginOrEmail,
        @JsonProperty("password")     final String password
    ) {
        this.loginOrEmail = loginOrEmail;
        this.password     = password;
    }
}
