package tv.lid.springboot.users.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tv.lid.springboot.users.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    // возвращает количество контактов с заданным идентификатором
    public long countById(final Integer id);

    // возвращает количество контактов у пользователя с заданным идентификатором
    public long countByUserId(final Integer userId);

    // возвращает количество контактов заданного типа и заданного значения
    @Query("select count(c) from Contact c where c.type = ?1 and c.contact = ?2")
    public long countByTypeAndContact(final Byte type, final String contact);

    // возвращает все контакты заданного пользователя
    public List<Contact> findByUserId(final Integer userId, final Sort sort);

    // активирует/деактивирует заданный контакт
    @Transactional
    @Modifying
    @Query("update Contact c set c.isActive = ?1 where c.id = ?2")
    public void activate(final Boolean isActive, final Integer id);
}
