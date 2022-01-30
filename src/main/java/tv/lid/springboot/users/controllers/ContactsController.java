package tv.lid.springboot.users.controllers;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tv.lid.springboot.users.entities.Contact;
import tv.lid.springboot.users.services.ContactService;
import tv.lid.springboot.users.services.UserService;

import tv.lid.springboot.users.json.requests.ContactRequest;
import tv.lid.springboot.users.json.responses.Response;

@RestController
public class ContactsController extends HtmlController {
    // сервис доступа к пользователям
    @Autowired
    private UserService userService;

    // сервис доступа к контактам
    @Autowired
    private ContactService contactService;

    // отобразить список контактов заданного пользователя
    @GetMapping("/contacts/{userId}")
    public ModelAndView contacts(
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

        // запрашиваем список контактов заданного пользователя
        List<Contact> list;
        try {
            list = this.contactService.list(__userId, ContactService.Order.DEFAULT);
        } catch (Exception exc) {
            return HtmlController.error500(rsp, "Не удалось отобразить список контактов!");
        }

        // формируем ответ
        model.put("list", list);
        model.put("userId", __userId);

        return new ModelAndView("layouts/contacts", model);
    }

    // проверить существование в базе заданного контакта
    @GetMapping(
        path     = "/contacts/check/{type}/{contact}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response checkLogin(
        @PathVariable final String type,
        @PathVariable final String contact,
        final HttpServletResponse rsp
    ) {
        Byte __type;

        // парсим тип контакта из строкового значения
        try {
            __type = Byte.valueOf(type);
            if (!Contact.isValid(__type)) {
                throw new Exception();
            }
        } catch (Exception exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан тип контакта!");
        }

        // считаем количество контактов
        final long countContacts = this.contactService.countByTypeAndContact(__type, contact);
        return countContacts == 0
            ? Response.ok()
            : Response.error(rsp, Response.Code.BAD_REQUEST);
    }

    // создать контакт для заданного пользователя
    @PostMapping(
        path     = "/contacts/{userId}/create",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response create(
        @PathVariable final String userId,
        @RequestBody  final ContactRequest contactReq,
        final HttpServletResponse rsp
    ) {
        Integer __userId;

        // парсим идентификатор пользователя из строкового значения
        try {
            __userId = Integer.valueOf(userId);
            if (!this.userService.exists(__userId)) {
                throw new Exception();
            }
        } catch (Exception exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор пользователя!");
        }

        // проверяем корректность входных данных
        if (contactReq.type == null || !Contact.isValid(contactReq.type) || contactReq.value == null) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно заданы входные данные!");
        }

        // проверяем, что такой контакт отсутствует в базе
        final long countContacts = this.contactService.countByTypeAndContact(contactReq.type, contactReq.value);
        if (countContacts != 0) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Контакт с заданными параметрами уже был создан!");
        }

        // создаём контакт по заданным параметрам
        final Contact contact = new Contact(__userId, contactReq.type, contactReq.value, true);

        // сохраняем его
        this.contactService.store(contact);

        // отправляем ответ о готовности
        return Response.ok();
    }

    // отредактировать контакт для заданного пользователя
    @PutMapping(
        path     = "/contacts/edit/{contactId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response edit(
        @PathVariable final String         contactId,
        @RequestBody  final ContactRequest contactReq,
        final HttpServletResponse rsp
    ) {
        Integer __contactId;

        // парсим идентификатор контакта из строкового значения
        try {
            __contactId = Integer.valueOf(contactId);
            if (!this.contactService.exists(__contactId)) {
                throw new Exception();
            }
        } catch (Exception exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор контакта!");
        }

        // запрашиваем контакт по заданному идентификатору
        final Contact contact = this.contactService.find(__contactId);

        // проверяем, что контакт найден
        if (contact == null) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Не найден контакт с заданным идентификатором!");
        }

        // проверяем корректность входных данных
        if (contactReq.type == null || !Contact.isValid(contactReq.type) || contactReq.value == null) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно заданы входные данные!");
        }

        // если параметры контакта поменялись, то проверяем, что в базе нет таких же
        if (contactReq.type.byteValue() != contact.getType().byteValue() ||
            !contactReq.value.equals(contact.getContact())) {
            final long countByTypeAndContact = this.contactService.countByTypeAndContact(contactReq.type, contactReq.value);
            if (countByTypeAndContact > 0) {
                return Response.error(rsp, Response.Code.BAD_REQUEST, "Контакт с заданными параметрами уже существует!");
            }
        }

        // изменяем контакт
        contact.setType(contactReq.type);
        contact.setContact(contactReq.value);

        // сохраняем его
        this.contactService.store(contact);

        // отправляем ответ о готовности
        return Response.ok();
    }

    // активирует заданный контакт
    @PutMapping(
        path     = "/contacts/activate/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response activate(@PathVariable final String contactId, final HttpServletResponse rsp) {
        Integer __contactId;

        // парсим идентификатор контакта из строкового значения
        try {
            __contactId = Integer.valueOf(contactId);
        } catch (NumberFormatException exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор контакта!");
        }

        // активируем
        this.contactService.activate(true, __contactId);

        // отправляем ответ о готовности
        return Response.ok();
    }

    // деактивирует заданный контакт
    @PutMapping(
        path     = "/contacts/deactivate/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response deactivate(@PathVariable final String contactId, final HttpServletResponse rsp) {
        Integer __contactId;

        // парсим идентификатор контакта из строкового значения
        try {
            __contactId = Integer.valueOf(contactId);
        } catch (NumberFormatException exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор контакта!");
        }

        // деактивируем
        this.contactService.activate(false, __contactId);

        // отправляем ответ о готовности
        return Response.ok();
    }

    // удаляет заданный контакт
    @DeleteMapping(
        path     = "/contacts/kill/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) Response kill(@PathVariable final String contactId, final HttpServletResponse rsp) {
        Integer __contactId;

        // парсим идентификатор контакта из строкового значения
        try {
            __contactId = Integer.valueOf(contactId);
        } catch (NumberFormatException exc) {
            return Response.error(rsp, Response.Code.BAD_REQUEST, "Некорректно задан идентификатор контакта!");
        }

        // удаляем контакт
        this.contactService.kill(__contactId);

        // отправляем ответ о готовности
        return Response.ok();
    }
}
