package tv.lid.springboot.users.json.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// данные запроса на добавление нового контакта или редактирование
public class ContactRequest {
    // тип
    @JsonProperty(value = "type", required = true)
    public final Byte type;

    // контакт
    @JsonProperty(value = "value", required = true)
    public final String value;

    // конструктор
    @JsonCreator
    public ContactRequest(
        @JsonProperty("type")  final Byte   type,
        @JsonProperty("value") final String value
    ) {
        this.type  = type;
        this.value = value;
    }
}
