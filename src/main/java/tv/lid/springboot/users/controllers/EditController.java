package tv.lid.springboot.users.controllers;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tv.lid.springboot.users.entities.User;
import tv.lid.springboot.users.services.UserService;

import tv.lid.springboot.users.json.requests.UserRequest;
import tv.lid.springboot.users.json.responses.Response;

@RestController
public class EditController extends HtmlController {
    // сервис доступа к пользователям
    @Autowired
    private UserService userService;

    // проверить доступность заданного логина
    @GetMapping(
        path     = "/check/login/{login}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response checkLogin(@PathVariable final String login, final HttpServletResponse rsp) {
        final long countByLogin = this.userService.countByLogin(login);
        return countByLogin == 0
            ? Response.ok()
            : Response.error(rsp, Response.Code.BAD_REQUEST);
    }

    // проверить доступность заданного email
    @GetMapping(
        path     = "/check/email/{email}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response checkEmail(@PathVariable final String email, final HttpServletResponse rsp) {
        final long countByEmail = this.userService.countByEmail(email);
        return countByEmail == 0
            ? Response.ok()
            : Response.error(rsp, Response.Code.BAD_REQUEST);
    }

    // открыть страницу для создания нового пользователя
    @GetMapping("/create")
    public ModelAndView create(final Map<String, Object> model) {
        model.put("user", new User());
        return new ModelAndView("layouts/create", model);
    }

    // создать нового пользователя
    @PostMapping(
        path     = "/create",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response create(@RequestBody final UserRequest userReq, final HttpServletResponse rsp) {
        // проверяем корректность входных данных
        if (userReq.login      == null || userReq.login.length()     == 0 ||
            userReq.password   == null || userReq.password.length()  == 0 ||
            userReq.email      == null || userReq.email.length()     == 0 ||
            userReq.lastName   == null || userReq.lastName.length()  == 0 ||
            userReq.firstName  == null || userReq.firstName.length() == 0 ||
            userReq.privileges == null ||
            userReq.gender     == null) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно заданы входные данные!");
        }

        // проверяем, что нет пользователей с заданным логином
        final long countByLogin = this.userService.countByLogin(userReq.login);
        if (countByLogin > 0) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Пользователь с заданным логином уже существует!");
        }

        // проверяем, что нет пользователей с заданным email
        final long countByEmail = this.userService.countByEmail(userReq.email);
        if (countByEmail > 0) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Пользователь с заданным логином уже существует!");
        }

        // создаём пользователя по заданным параметрам
        final User user = new User(
            userReq.login,
            userReq.password,
            userReq.email,
            userReq.lastName,
            userReq.firstName,
            userReq.patronymic,
            userReq.privileges,
            userReq.gender,
            userReq.isActive
        );

        // сохраняем его
        this.userService.store(user);

        // отправляем ответ о готовности
        return Response.ok();
    }

    @GetMapping("/edit/{userId}")
    public ModelAndView edit(
        @PathVariable final String userId,
        final Map<String, Object> model,
        final HttpServletResponse rsp
    ) {
        Integer __userId;

        // парсим идентификатор пользователя из строкового значения
        try {
            __userId = Integer.valueOf(userId);
        } catch (NumberFormatException exc) {
            return HtmlController.error400(rsp, "Некорректно задан идентификатор пользователя!");
        }

        // запрашиваем пользователя по заданному идентификатору
        final User user = this.userService.find(__userId);

        // проверяем, что пользователь найден
        if (user == null) {
            return HtmlController.error400(rsp, "Не найден пользователь с заданным идентификатором!");
        }

        // формируем ответ
        model.put("user", user);

        return new ModelAndView("layouts/edit", model);
    }

    @PutMapping(
        path     = "/edit/{userId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response edit(
        @PathVariable final String userId,
        @RequestBody  final UserRequest userReq,
        final HttpServletResponse rsp
    ) {
        Integer __userId;

        // парсим идентификатор пользователя из строкового значения
        try {
            __userId = Integer.valueOf(userId);
        } catch (NumberFormatException exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор пользователя!");
        }

        // запрашиваем пользователя по заданному идентификатору
        final User user = this.userService.find(__userId);

        // проверяем, что пользователь найден
        if (user == null) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Не найден пользователь с заданным идентификатором!");
        }

        // проверяем корректность входных данных
        if (userReq.login      == null || userReq.login.length()     == 0 ||
            userReq.email      == null || userReq.email.length()     == 0 ||
            userReq.lastName   == null || userReq.lastName.length()  == 0 ||
            userReq.firstName  == null || userReq.firstName.length() == 0 ||
            userReq.privileges == null ||
            userReq.gender     == null) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно заданы входные данные!");
        }

        // если логин поменялся, то проверяем, что нет пользователей с новым заданным логином
        if (!userReq.login.equals(user.getLogin())) {
            final long countByLogin = this.userService.countByLogin(userReq.login);
            if (countByLogin > 0) {
                return Response.error(rsp, Response.Code.BAD_REQUEST, "Пользователь с заданным логином уже существует!");
            }
        }

        // если email поменялся, то проверяем, что нет пользователей с новым заданным email
        if (!userReq.email.equals(user.getEmail())) {
            final long countByEmail = this.userService.countByEmail(userReq.email);
            if (countByEmail > 0) {
                return Response.error(rsp, Response.Code.BAD_REQUEST, "Пользователь с заданным логином уже существует!");
            }
        }

        // изменяем пользователя
        user.setLogin(userReq.login);
        user.setEmail(userReq.email);
        user.setLastName(userReq.lastName);
        user.setFirstName(userReq.firstName);
        user.setPatronymic(userReq.patronymic);
        user.setPrivileges(userReq.privileges);
        user.setGender(userReq.gender);
        user.setIsActive(userReq.isActive);
        // пароль меняется опционально
        if (userReq.password != null) {
            user.setPassword(userReq.password);
        }

        // сохраняем его
        this.userService.store(user);

        // отправляем ответ о готовности
        return Response.ok();
    }
}
