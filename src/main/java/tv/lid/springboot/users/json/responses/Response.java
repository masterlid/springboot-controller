package tv.lid.springboot.users.json.responses;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public final class Response {
    // множество кодов ответов сервера
    public static enum Code {
        OK                    (200), // всё в порядке
        BAD_REQUEST           (400), // ошибка на стороне клиента
        UNAUTHORIZED          (401), // ошибка авторизации
        INTERNAL_SERVER_ERROR (500); // ошибка на стороне сервера

        private int code;

        private Code(final int code) {
            this.code = code;
        }

        public int getValue() {
            return this.code;
        }

        public static Code valueOf(final int code) {
            switch (code) {
                case 200:
                    return Code.OK;
                case 400:
                    return Code.BAD_REQUEST;
                case 401:
                    return Code.UNAUTHORIZED;
                case 500:
                    return Code.INTERNAL_SERVER_ERROR;
                default:
                    return null;
            }
        }
    }

    // код ответа
    @JsonProperty(value = "code", required = false)
    public final int code;

    // дополнительная информация
    @JsonProperty(value = "info", required = false)
    public final String  info;

    // конструктор #1
    private Response(final int code, final String info) {
        this.code = code;
        this.info = info;
    }

    // конструктор #2
    private Response(final int code) {
        this(code, null);
    }

    // конструктор #3
    private Response(final Code code, final String info) {
        this(code.getValue(), info);
    }

    // конструктор #4
    private Response(final Code code) {
        this(code, null);
    }

    // успешный ответ
    public static final Response ok() {
        return new Response(Code.OK);
    }

    // ответ с ошибкой, c дополнительной информацией
    public static final Response error(final HttpServletResponse rsp, final Code code, final String info) {
        final Code c = (
            code == Code.BAD_REQUEST  ||
            code == Code.UNAUTHORIZED ||
            code == Code.INTERNAL_SERVER_ERROR
        ) ? code : Code.BAD_REQUEST;

        rsp.setStatus(c.getValue());
        return new Response(c, info);
    }

    // ответ с ошибкой, без дополнительной информации
    public static final Response error(final HttpServletResponse rsp, final Code code) {
        return Response.error(rsp, code, null);
    }
}
