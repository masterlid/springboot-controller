package tv.lid.springboot.users;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tv.lid.springboot.users.entities.User;
import tv.lid.springboot.users.services.UserService;
import tv.lid.springboot.users.entities.Contact;
import tv.lid.springboot.users.services.ContactService;

@SpringBootTest
class UsersApplicationTests {
    private Integer userId;

    private Integer phoneContactId;
    private Integer emailContactId;
    private Integer skypeContactId;

    // сервис доступа к пользователям
    @Autowired
    private UserService userService;

    // сервис доступа к контактам
    @Autowired
    private ContactService contactService;

    @Test
    @Transactional
    public void test() { // запуск всех тестов
        this.checkAdmin();
        this.createUser();
        this.inspectUsers();
        this.modifyUser();

        this.createContacts();
        this.inspectContacts();
        this.modifyContact();
        this.killContacts();

        this.killUser();
    }

    // проверка корректности данных администратора
    private void checkAdmin() {
        final User user = this.userService.find(1);

        assertTrue(user != null, "Admin user is found!");
        assertTrue(user.getId() == 1, "Admin identifier is valid!");
        assertTrue(user.getLogin().equals("admin"), "Admin login is valid!");
        assertTrue(user.isValidPassword("admin123"), "Admin password hash is valid!");
        assertTrue(user.getEmail().equals("ivan.adminov@lid.tv"), "Admin email value is valid!");
        assertTrue(user.getLastName().equals("Админов"), "Admin last name value is valid!");
        assertTrue(user.getFirstName().equals("Иван"), "Admin first name value is valid!");
        assertTrue(user.getPatronymic().equals("Иванович"), "Admin patronymic value is valid!");
        assertTrue(user.getPrivileges() == User.PRIVILEGES_ADMIN, "Admin privileges value is valid!");
        assertTrue(user.getGender() == User.GENDER_MALE, "Admin gender value is valid!");
        assertTrue(user.getIsActive() == true, "Admin activity value is valid!");
    }

    // проверка корректности создания нового пользователя
    private void createUser() {
        final User user1 = new User(
            "visitor",
            "visitor123",
            "viktor.visitorov@lid.tv",
            "Визиторов",
            "Виктор",
            "Викторович",
            User.PRIVILEGES_COMMON,
            User.GENDER_MALE,
            true
        );
        this.userService.store(user1);
        this.userId = user1.getId();

        assertTrue(this.userService.count() == 2, "User quantity is valid!");

        final User user2 = this.userService.find(this.userId);
        assertTrue(user2 != null, "Newly created user was successfully found!");
        assertTrue(user2.getId() == this.userId, "User identifier is valid!");
        assertTrue(user2.getLogin().equals("visitor"), "User login is valid!");
        assertTrue(user2.isValidPassword("visitor123"), "User password is valid!");
        assertTrue(user2.getEmail().equals("viktor.visitorov@lid.tv"), "User email value is valid!");
        assertTrue(user2.getLastName().equals("Визиторов"), "User last name value is valid!");
        assertTrue(user2.getFirstName().equals("Виктор"), "User first name value is valid!");
        assertTrue(user2.getPatronymic().equals("Викторович"), "User patronymic value is valid!");
        assertTrue(user2.getPrivileges() == User.PRIVILEGES_COMMON, "User privileges value is valid!");
        assertTrue(user2.getGender() == User.GENDER_MALE, "User gender value is valid!");
        assertTrue(user2.getIsActive() == true, "User activity value is valid!");
    }

    // проверка корректности чтения списка пользователей
    private void inspectUsers() {
        List<User> users;

        try {
            users = this.userService.list(null, UserService.Order.LOGIN_DESC);
        } catch (Exception exc) {
            users = null;
        }
        assertTrue(users != null && users.size() == 2, "Users list is not empty!");

        User u;
        for (int i = 0; i < users.size(); i++) {
            u = users.get(i);
            switch (i) {
                case 0: // visitor
                    assertTrue(
                        u.getId() == this.userId
                            && u.getLogin().equals("visitor")
                            && u.isValidPassword("visitor123")
                            && u.getEmail().equals("viktor.visitorov@lid.tv"),
                        "Visitor contact is valid!"
                    );
                    break;
                case 1: // admin
                    assertTrue(
                        u.getId() == 1
                            && u.getLogin().equals("admin")
                            && u.isValidPassword("admin123")
                            && u.getEmail().equals("ivan.adminov@lid.tv"),
                        "Admin contact is valid!"
                    );
                    break;
            }
        }
    }

    // проверка корректности редактирования заданного пользователя
    private void modifyUser() {
        final User user1 = this.userService.find("visitor");
        assertTrue(user1 != null, "Visitor user was found!");
        assertTrue(user1.getId() == this.userId, "User identifier is valid!");

        user1.setLogin("manager");
        user1.setPassword("manager123");
        user1.setEmail("marina.manzherova@lid.tv");
        user1.setLastName("Манжерова");
        user1.setFirstName("Марина");
        user1.setPatronymic("Матвеевна");
        user1.setPrivileges(User.PRIVILEGES_ADMIN);
        user1.setGender(User.GENDER_FEMALE);
        user1.setIsActive(false);
        this.userService.store(user1);

        final User user2 = this.userService.find(this.userId);
        assertTrue(user2 != null, "Manager user was successfully found!");
        assertTrue(user2.getId() == this.userId, "User id is valid!");
        assertTrue(user2.getLogin().equals("manager"), "User login is valid!");
        assertTrue(user2.isValidPassword("manager123"), "User password is valid!");
        assertTrue(user2.getEmail().equals("marina.manzherova@lid.tv"), "User email value is valid!");
        assertTrue(user2.getLastName().equals("Манжерова"), "User last name value is valid!");
        assertTrue(user2.getFirstName().equals("Марина"), "User first name value is valid!");
        assertTrue(user2.getPatronymic().equals("Матвеевна"), "User patronymic value is valid!");
        assertTrue(user2.getPrivileges() == User.PRIVILEGES_ADMIN, "User privileges value is valid!");
        assertTrue(user2.getGender() == User.GENDER_FEMALE, "User gender value is valid!");
        assertTrue(user2.getIsActive() == false, "User activity value is valid!");
    }

    // проверка корректности создания контактов
    private void createContacts() {
        List<Contact> contacts;

        try {
            contacts = this.contactService.list(this.userId, ContactService.Order.DEFAULT);
        } catch (Exception exc) {
            contacts = null;
        }
        assertTrue(contacts != null && contacts.size() == 0, "Contacts list is empty!");

        final Contact phoneContact = new Contact(this.userId, Contact.TYPE_PHONE, "+7 (000) 123-45-67", true);
        this.contactService.store(phoneContact);
        this.phoneContactId = phoneContact.getId();
        assertTrue(this.phoneContactId > 0, "Phone contact was successfully stored!");

        final Contact emailContact = new Contact(this.userId, Contact.TYPE_EMAIL, "manzherova@example.com", true);
        this.contactService.store(emailContact);
        this.emailContactId = emailContact.getId();
        assertTrue(this.emailContactId > 0, "Email contact was successfully stored!");

        final Contact skypeContact = new Contact(this.userId, Contact.TYPE_SKYPE, "marina.manzherova", true);
        this.contactService.store(skypeContact);
        this.skypeContactId = skypeContact.getId();
        assertTrue(this.skypeContactId > 0, "Skype contact was successfully stored!");

        assertTrue(this.contactService.count(this.userId) == 3, "All contacts were successfully stored!");
    }

    // проверка корректности чтения списка контактов
    private void inspectContacts() {
        List<Contact> contacts;

        try {
            contacts = this.contactService.list(this.userId, ContactService.Order.TYPE_DESC);
        } catch (Exception exc) {
            contacts = null;
        }
        assertTrue(contacts != null && contacts.size() == 3, "Contacts list is not empty!");

        Contact c;
        for (int i = 0; i < contacts.size(); i++) {
            c = contacts.get(i);
            switch (i) {
                case 0: // skype
                    assertTrue(
                        c.getId() == this.skypeContactId
                            && c.getType() == Contact.TYPE_SKYPE
                            && c.getContact().equals("marina.manzherova"),
                        "Skype contact is valid!"
                    );
                    break;
                case 1: // email
                    assertTrue(
                        c.getId() == this.emailContactId
                            && c.getType() == Contact.TYPE_EMAIL
                            && c.getContact().equals("manzherova@example.com"),
                        "Email contact is valid!"
                    );
                    break;
                case 2: // phone
                    assertTrue(
                        c.getId() == this.phoneContactId
                            && c.getType() == Contact.TYPE_PHONE
                            && c.getContact().equals("+7 (000) 123-45-67"),
                        "Phone contact is valid!"
                    );
                    break;
            }
        }
    }

    // проверка корректности редактирования заданного контакта
    private void modifyContact() {
        final Contact contact1 = this.contactService.find(this.emailContactId);

        assertTrue(contact1 != null, "Email contact was successfully found!");
        assertTrue(contact1.getId() == this.emailContactId, "Contact id value is valid!");
        assertTrue(contact1.getUserId() == this.userId, "Contact userId value is valid!");
        assertTrue(contact1.getType() == Contact.TYPE_EMAIL, "Contact type value is valid!");
        assertTrue(contact1.getContact().equals("manzherova@example.com"), "Contact value is valid!");
        assertTrue(contact1.getIsActive() == true, "Contact activity value is valid!");

        contact1.setType(Contact.TYPE_TWITTER);
        contact1.setContact("@manzherova");
        contact1.setIsActive(false);
        this.contactService.store(contact1);

        final Contact contact2 = this.contactService.find(this.emailContactId);
        assertTrue(contact2 != null, "Twitter contact was successfully found!");
        assertTrue(contact2.getId() == this.emailContactId, "Contact id is valid!");
        assertTrue(contact2.getUserId() == this.userId, "Contact userId value is valid!");
        assertTrue(contact2.getType() == Contact.TYPE_TWITTER, "Contact type value is valid!");
        assertTrue(contact2.getContact().equals("@manzherova"), "Contact value is valid!");
        assertTrue(contact2.getIsActive() == false, "Contact activity value is valid!");
    }

    // проверка корректности удаления контактов
    private void killContacts() {
        this.contactService.kill(this.skypeContactId);

        List<Contact> contacts;

        try {
            contacts = this.contactService.list(this.userId, ContactService.Order.TYPE_DESC);
        } catch (Exception exc) {
            contacts = null;
        }
        assertTrue(contacts != null && contacts.size() == 2, "Contacts list is not empty!");

        Contact c;
        for (int i = 0; i < contacts.size(); i++) {
            c = contacts.get(i);
            switch (i) {
                case 0: // twitter
                    assertTrue(
                        c.getId() == this.emailContactId
                            && c.getType() == Contact.TYPE_TWITTER
                            && c.getContact().equals("@manzherova"),
                        "Twitter contact is valid!"
                    );
                    break;
                case 1: // phone
                    assertTrue(
                        c.getId() == this.phoneContactId
                            && c.getType() == Contact.TYPE_PHONE
                            && c.getContact().equals("+7 (000) 123-45-67"),
                        "Phone contact is valid!"
                    );
                    break;
            }
        }

        this.contactService.kill(this.emailContactId);
        this.contactService.kill(this.phoneContactId);
        assertTrue(this.contactService.count(this.userId) == 0, "No more contacts left!");
    }

    // проверка корректности удаления пользователя
    private void killUser() {
        this.userService.kill(this.userId);

        List<User> users;
        try {
            users = this.userService.list(null, UserService.Order.LOGIN_ASC);
        } catch (Exception exc) {
            users = null;
        }
        assertTrue(users != null && users.size() == 1, "Manager user was succesfully deleted!");

        final User user = users.get(0);
        assertTrue(user.getId() == 1 && user.getLogin().equals("admin"), "Only admin user is left!");

        assertTrue(this.userService.count() == 1, "No more users are in the list!");
    }
}
