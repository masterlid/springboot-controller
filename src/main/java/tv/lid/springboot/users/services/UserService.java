package tv.lid.springboot.users.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tv.lid.springboot.users.entities.User;
import tv.lid.springboot.users.repositories.UserRepository;

@Service
public final class UserService {
    public static int DEFAULT_PAGE  = 0;  // номер страницы по умолчанию
    public static int DEFAULT_COUNT = 20; // количество пользователей на страницу по умолчанию

    public static enum Order {
        DEFAULT,
        LOGIN_ASC,
        LOGIN_DESC,
        EMAIL_ASC,
        EMAIL_DESC,
        LAST_NAME_ASC,
        LAST_NAME_DESC,
        CREATED_AT_ASC,
        CREATED_AT_DESC
    }

    @Autowired
    private UserRepository userRepo;

    // возвращает общее количество всех пользователей
    public long count() {
        return this.userRepo.count();
    }

    // возвращает количество пользователей с заданным логином
    public long countByLogin(final String login) {
        return this.userRepo.countByLogin(login);
    }

    // возвращает количество пользователей с заданным email
    public long countByEmail(final String email) {
        return this.userRepo.countByEmail(email);
    }

    // возвращает список пользователей по заданным параметрам
    public List<User> list(
        final String seek,  // подстрока для поиска (м.б. null)
        final int    page,  // номер страницы
        final int    count, // кол-во записей на одной странице
        final UserService.Order order
    ) throws Exception {
        Sort sort;

        switch (order) {
            case LOGIN_ASC:
                sort = Sort.by("login").ascending();
                break;
            case LOGIN_DESC:
                sort = Sort.by("login").descending();
                break;
            case EMAIL_ASC:
                sort = Sort.by("email").ascending();
                break;
            case EMAIL_DESC:
                sort = Sort.by("email").descending();
                break;
            case LAST_NAME_ASC:
                sort = Sort.by("lastName").ascending();
                break;
            case LAST_NAME_DESC:
                sort = Sort.by("lastName").descending();
                break;
            case CREATED_AT_ASC:
                sort = Sort.by("createdAt").ascending();
                break;
            case CREATED_AT_DESC:
                sort = Sort.by("createdAt").descending();
                break;
            case DEFAULT:
            default:
                sort = Sort.by("login").ascending();
        }
        // return this.userRepo.findAll(PageRequest.of(page, count, sort)).getContent();
        return (
            seek == null
                ? this.userRepo.findAll(PageRequest.of(page, count, sort)).getContent()
                : this.userRepo.seekBySubstring(seek, PageRequest.of(page, count, sort)).getContent()
        );
    }

    // возвращает список пользователей на первой странице с заданной сортировкой
    public List<User> list(final String seek, final UserService.Order order) throws Exception {
        return this.list(seek, UserService.DEFAULT_PAGE, UserService.DEFAULT_COUNT, order);
    }

    // проверяет существование пользователя по заданному идентификатору
    public boolean exists(final Integer id) {
        final long count = this.userRepo.countById(id);
        return count != 0;
    }

    // возвращает пользователя по заданному идентификатору
    public User find(final Integer id) {
        final Optional<User> optUser = this.userRepo.findById(id);
        return optUser.isPresent() ? optUser.get() : null;
    }

    // возвращает пользователя по заданному логину
    public User find(final String login) {
        return this.userRepo.findFirstByLogin(login);
    }

    // возвращает пользователя по заданному логину или email
    public User findByLoginOrEmail(final String loginOrEmail) {
        User user = this.userRepo.findFirstByLogin(loginOrEmail); // пытаемся найти пользователя по логину
        if (user == null) { // если не удалось, то ...
            user = this.userRepo.findFirstByEmail(loginOrEmail); // ... пытаемся найти пользователя по email
        }
        return user;
    }

    // сохраняет информацию о заданном пользователе
    public void store(final User user) {
        this.userRepo.saveAndFlush(user);
    }

    // удаляет пользователя по заданному идентификатору
    public void kill(final Integer id) {
        this.userRepo.deleteById(id);
    }

    // активирует/деактивирует заданного пользователя
    public void activate(final Boolean isActive, final Integer id) {
        this.userRepo.activate(isActive, id);
    }
}
