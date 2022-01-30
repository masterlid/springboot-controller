package tv.lid.springboot.users.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tv.lid.springboot.users.json.responses.Response;

@RestController
public class SeekController extends HtmlController {
    public final static String SEEK_SESSION_ATTRIBUTE = "seek";

    @GetMapping("/seek")
    public ModelAndView seek(final Map<String, Object> model) {
        return new ModelAndView("layouts/seek", model);
    }

    // поиск пользователя по заданной строке
    @PostMapping(path = "/seek")
    public ModelAndView seek(final HttpServletRequest request, final HttpSession session) {
        final String seek = (String) request.getParameter(SeekController.SEEK_SESSION_ATTRIBUTE);
        session.setAttribute(SeekController.SEEK_SESSION_ATTRIBUTE, seek);
        return new ModelAndView("redirect:/list");
    }

    // больше не искать пользователя
    @DeleteMapping(
        path     = "/unseek",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response unseek(final HttpSession session) {
        session.removeAttribute(SeekController.SEEK_SESSION_ATTRIBUTE);

        // отправляем ответ о готовности
        return Response.ok();
    }
}
