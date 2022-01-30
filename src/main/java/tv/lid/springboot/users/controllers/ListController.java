package tv.lid.springboot.users.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tv.lid.springboot.users.entities.User;
import tv.lid.springboot.users.services.UserService;

import tv.lid.springboot.users.json.responses.Response;
import tv.lid.springboot.users.helpers.PaginationHelper;

@RestController
public class ListController extends HtmlController {
    // сервис доступа к пользователям
    @Autowired
    private UserService userService;

    // выводит список пользователей по заданной странице
    @GetMapping(path = {"/list", "/list/{page}"})
    public ModelAndView list(
        @PathVariable final Optional<String> page,
        final Map<String, Object> model,
        final HttpServletResponse rsp,
        final HttpSession session
    ) {
        final Object seek   = session.getAttribute(SeekController.SEEK_SESSION_ATTRIBUTE);
        final String __seek = seek != null ? seek.toString() : null;

        Integer __page = 0;

        // парсим номер страницы
        if (page.isPresent()) {
            try {
                __page = Integer.valueOf(page.get()) - 1;
                if (__page < 0) {
                    __page = 0;
                }
            } catch (Exception exc) {
                __page = 0;
            }
        }

        // запрашиваем список пользователей по заданному номеру страницы
        List<User> list;
        try {
            list = this.userService.list(
                __seek,
                __page,
                UserService.DEFAULT_COUNT,
                UserService.Order.CREATED_AT_DESC
            );
        } catch (Exception exc) {
            return HtmlController.error500(rsp, "Не удалось отобразить список пользователей!");
        }

        // считаем общее количество страниц
        final int totalPages = (int) Math.ceil(((double) this.userService.count()) / UserService.DEFAULT_COUNT);

        // формируем ответ
        model.put("list", list);
        model.put("seek", __seek);
        model.put("page", __page + 1);
        model.put("pagination", PaginationHelper.generate(__page + 1, totalPages));

        return new ModelAndView("layouts/list", model);
    }

    // активирует заданного пользователя
    @PutMapping(
        path     = "/list/activate/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response activate(@PathVariable final String userId, final HttpServletResponse rsp) {
        Integer __userId;

        // парсим идентификатор пользователя из строкового значения
        try {
            __userId = Integer.valueOf(userId);
        } catch (NumberFormatException exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор пользователя!");
        }

        // активируем
        this.userService.activate(true, __userId);

        // отправляем ответ о готовности
        return Response.ok();
    }

    // деактивирует заданного пользователя
    @PutMapping(
        path     = "/list/deactivate/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response deactivate(@PathVariable final String userId, final HttpServletResponse rsp) {
        Integer __userId;

        // парсим идентификатор пользователя из строкового значения
        try {
            __userId = Integer.valueOf(userId);
        } catch (NumberFormatException exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор пользователя!");
        }

        // деактивируем
        this.userService.activate(false, __userId);

        // отправляем ответ о готовности
        return Response.ok();
    }

    // удаляет заданного пользователя
    @DeleteMapping(
        path     = "/list/kill/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response kill(@PathVariable final String userId, final HttpServletResponse rsp) {
        Integer __userId;

        // парсим идентификатор пользователя из строкового значения
        try {
            __userId = Integer.valueOf(userId);
        } catch (NumberFormatException exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор пользователя!");
        }

        // удаляем пользователя
        this.userService.kill(__userId);

        // отправляем ответ о готовности
        return Response.ok();
    }
}
