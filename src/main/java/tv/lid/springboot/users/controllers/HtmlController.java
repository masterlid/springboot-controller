package tv.lid.springboot.users.controllers;

import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public abstract class HtmlController {
    // выводит заданное сообщение об ошибке с заданным кодом
    private static ModelAndView error(final HttpServletResponse rsp, final int code, final String info) {
        final HashMap<String, Object> model = new HashMap<String, Object>();

        model.put("info", info);
        rsp.setStatus(code);
        return new ModelAndView("layouts/error", model);
    }

    // выводит заданное сообщение об ошибке с кодом 400
    protected static ModelAndView error400(final HttpServletResponse rsp, final String info) {
        return HtmlController.error(rsp, 400, info);
    }

    // выводит заданное сообщение об ошибке с кодом 500
    protected static ModelAndView error500(final HttpServletResponse rsp, final String info) {
        return HtmlController.error(rsp, 500, info);
    }
}
