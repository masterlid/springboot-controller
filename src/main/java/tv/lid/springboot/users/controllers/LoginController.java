package tv.lid.springboot.users.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tv.lid.springboot.users.services.UserService;

import tv.lid.springboot.users.json.requests.AuthRequest;
import tv.lid.springboot.users.json.responses.Response;

@RestController
public class LoginController extends HtmlController {
    // сервис доступа к пользователям
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    // открыть страницу авторизации
    @GetMapping("/login")
    public ModelAndView login() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ModelAndView(
            auth instanceof AnonymousAuthenticationToken // проверка, что пользователь уже авторизован
                ? "layouts/login"
                : "redirect:/"
        );
    }

    // авторизация
    @PostMapping(
        path     = "/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response auth(
        @RequestBody final AuthRequest authReq,
        final HttpServletResponse rsp,
        final HttpSession session
    ) {
        // проверяем корректность входных данных
        if (authReq.loginOrEmail == null || authReq.loginOrEmail.length() == 0 ||
            authReq.password     == null || authReq.password.length()     == 0) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно заданы входные данные!");
        }

        // проверяем авторизацию
        final UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(authReq.loginOrEmail, authReq.password);
        final Authentication auth = this.authManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(auth);

        return Response.ok();
    }

    // деавторизация
    @DeleteMapping(
        path     = "/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response auth(
        final HttpServletRequest  req,
        final HttpServletResponse rsp
    ) {
        try {
            req.logout();
        } catch (Exception exc) {
            return Response.error(rsp, Response.Code.INTERNAL_SERVER_ERROR, "Не удалось выполнить деавторизацию!");
        }

        return Response.ok();
    }
}
