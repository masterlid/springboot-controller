package tv.lid.springboot.users.entities;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="contacts")
public final class Contact {
    public static Byte TYPE_PHONE    = Byte.valueOf((byte) 10);
    public static Byte TYPE_EMAIL    = Byte.valueOf((byte) 20);
    public static Byte TYPE_SKYPE    = Byte.valueOf((byte) 30);
    public static Byte TYPE_FACEBOOK = Byte.valueOf((byte) 40);
    public static Byte TYPE_TWITTER  = Byte.valueOf((byte) 50);
    public static Byte TYPE_YOUTUBE  = Byte.valueOf((byte) 60);
    public static Byte TYPE_VK       = Byte.valueOf((byte) 70);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false, unique = false)
    private Integer userId;

    @Column(name = "type", nullable = false)
    private Byte type;

    @Column(name = "contact", length = 256, nullable = false)
    private String contact;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    // конструктор #1
    public Contact(
        final Integer userId,
        final Byte    type,
        final String  contact,
        final Boolean isActive
    ) {
        this.id        = null;
        this.userId    = userId;
        this.type      = type;
        this.contact   = contact;
        this.isActive  = isActive;
        this.createdAt = new Date();
        this.updatedAt = null;
    }

    // конструктор #2
    public Contact() {}

    // проверка корректности типа контакта
    public static boolean isValid(final Byte type) {
        final Byte[] TYPES = {
            TYPE_PHONE, TYPE_EMAIL, TYPE_SKYPE, TYPE_FACEBOOK, TYPE_TWITTER, TYPE_YOUTUBE, TYPE_VK
        };
        return Arrays.asList(TYPES).indexOf(type) != -1;
    }

    // геттер идентификатора
    public Integer getId() {
        return this.id;
    }

    // геттер идентификатора пользователя
    public Integer getUserId() {
        return this.userId;
    }

    // сеттер идентификатора пользователя
    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    // геттер типа контакта
    public Byte getType() {
        return this.type;
    }

    // сеттер типа контакта
    public void setType(final Byte type) {
        this.type = type;
    }

    // геттер контакта
    public String getContact() {
        return this.contact;
    }

    // сеттер контакта
    public void setContact(final String contact) {
        this.contact = contact;
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

    // сеттер timestamp создания записи в базе данных
    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    // геттер timestamp обновления записи в базе данных
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    // сеттер timestamp обновления записи в базе данных
    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
