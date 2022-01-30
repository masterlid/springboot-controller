package tv.lid.springboot.users.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tv.lid.springboot.users.entities.Contact;
import tv.lid.springboot.users.repositories.ContactRepository;

@Service
public final class ContactService {
    public static enum Order {
        DEFAULT,
        TYPE_ASC,
        TYPE_DESC,
        CONTACT_ASC,
        CONTACT_DESC
    }

    @Autowired
    private ContactRepository contactRepo;

    // возвращает количество контактов у пользователя с заданным идентификатором
    public long count(final Integer userId) {
        return this.contactRepo.countByUserId(userId);
    }

    // возвращает количество контактов заданного типа и заданного значения
    public long countByTypeAndContact(final Byte type, final String contact) {
        return this.contactRepo.countByTypeAndContact(type, contact);
    }

    // возвращает список контактов заданного пользователя
    public List<Contact> list(
        final Integer userId,
        final ContactService.Order order
    ) throws Exception {
        Sort sort;

        switch (order) {
            case TYPE_ASC:
                sort = Sort.by("type").ascending();
                break;
            case TYPE_DESC:
                sort = Sort.by("type").descending();
                break;
            case CONTACT_ASC:
                sort = Sort.by("contact").ascending();
                break;
            case CONTACT_DESC:
                sort = Sort.by("contact").descending();
                break;
            case DEFAULT:
            default:
                sort = Sort.by("type").ascending();
        }
        return this.contactRepo.findByUserId(userId, sort);
    }

    // проверяет существование контакта по заданному идентификатору
    public boolean exists(final Integer id) {
        final long count = this.contactRepo.countById(id);
        return count != 0;
    }

    // возвращает контакт по заданному идентификатору
    public Contact find(final Integer id) {
        final Optional<Contact> optContact = this.contactRepo.findById(id);
        return optContact.isPresent() ? optContact.get() : null;
    }

    // сохраняет информацию о заданном контакте
    public void store(final Contact contact) {
        this.contactRepo.saveAndFlush(contact);
    }

    // удаляет контакт по заданному идентификатору
    public void kill(final Integer id) {
        this.contactRepo.deleteById(id);
    }

    // активирует/деактивирует заданный контакт
    public void activate(final Boolean isActive, final Integer id) {
        this.contactRepo.activate(isActive, id);
    }
}
