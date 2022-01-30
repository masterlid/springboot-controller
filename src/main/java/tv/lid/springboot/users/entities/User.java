package tv.lid.springboot.users.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name="users")
public final class User {
    public static Byte PRIVILEGES_ADMIN  = Byte.valueOf((byte) 100);
    public static Byte PRIVILEGES_COMMON = Byte.valueOf((byte) 0);

    public static Byte GENDER_MALE       = Byte.valueOf((byte)  5);
    public static Byte GENDER_FEMALE     = Byte.valueOf((byte) -5);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login", length = 64, nullable = false, unique = true)
    private String login;

    @Column(name = "pwd_hash", length = 64, nullable = false, unique = true)
    private String pwdHash;

    @Column(name = "email", length = 256, nullable = false, unique = true)
    private String email;

    @Column(name = "last_name", length = 256, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 256, nullable = false)
    private String firstName;

    @Column(name = "patronymic", length = 256, nullable = true)
    private String patronymic;

    @Column(name = "privileges", nullable = false)
    private Byte privileges;

    @Column(name = "gender", nullable = false)
    private Byte gender;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    // конструктор #1
    public User(
        final String  login,
        final String  password,
        final String  email,
        final String  lastName,
        final String  firstName,
        final String  patronymic,
        final Byte    privileges,
        final Byte    gender,
        final Boolean isActive
    ) {
        this.id         = null;
        this.login      = login;
        this.pwdHash    = BCrypt.hashpw(password, BCrypt.gensalt());
        this.email      = email;
        this.lastName   = lastName;
        this.firstName  = firstName;
        this.patronymic = patronymic;
        this.privileges = privileges;
        this.gender     = gender;
        this.isActive   = isActive;
        this.createdAt  = new Date();
        this.updatedAt  = null;
    }

    // конструктор #2
    public User() {}

    // геттер идентификатора
    public Integer getId() {
        return this.id;
    }

    // геттер логина
    public String getLogin() {
        return this.login;
    }

    // сеттер логина
    public void setLogin(final String login) {
        this.login = login;
    }

    // геттер хэша пароля
    public String getPwdHash() {
        return this.pwdHash;
    }

    // сеттер пароля
    public void setPassword(final String password) {
        this.pwdHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // проверка соответствия пароля
    public boolean isValidPassword(final String password) {
        return BCrypt.checkpw(password, this.pwdHash);
    }

    // геттер email
    public String getEmail() {
        return this.email;
    }

    // сеттер email
    public void setEmail(final String email) {
        this.email = email;
    }

    // геттер имени
    public String getFirstName() {
        return this.firstName;
    }

    // сеттер имени
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    // геттер фамилии
    public String getLastName() {
        return this.lastName;
    }

    // сеттер фамилии
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    // геттер отчества
    public String getPatronymic() {
        return this.patronymic;
    }

    // сеттер отчества
    public void setPatronymic(final String patronymic) {
        this.patronymic = patronymic;
    }

    // геттер привилегий
    public Byte getPrivileges() {
        return this.privileges;
    }

    // сеттер привилегий
    public void setPrivileges(final Byte privileges) {
        this.privileges = privileges;
    }

    // геттер пола
    public Byte getGender() {
        return this.gender;
    }

    // геттер пола в текстовом виде
    public String getGenderText() {
        return this.gender == User.GENDER_MALE ? "муж" : (this.gender == User.GENDER_FEMALE ? "жен" : "-");
    }

    // сеттер пола
    public void setGender(final Byte gender) {
        this.gender = gender;
    }

    // геттер активности
    public Boolean getIsActive() {
        return this.isActive;
    }

    // сеттер активности
    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    // геттер timestamp создания записи в базе данных
    public Date getCreatedAt() {
        return this.createdAt;
    }

    // геттер timestamp создания записи в базе данных, возвращающий строку
    public String getCreatedAtText() {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(this.createdAt);
    }

    // сеттер timestamp создания записи в базе данных
    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    // геттер timestamp обновления записи в базе данных
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    // геттер timestamp обновления записи в базе данных, возвращающий строку
    public String getUpdatedAtText() {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(this.updatedAt);
    }

    // сеттер timestamp обновления записи в базе данных
    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
