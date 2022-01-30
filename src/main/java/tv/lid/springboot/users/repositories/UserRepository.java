package tv.lid.springboot.users.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import tv.lid.springboot.users.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // возвращает количество пользователей с заданным идентификатором
    public long countById(final Integer id);

    // возвращает количество пользователей с заданным логином
    public long countByLogin(final String login);

    // возвращает количество пользователей с заданным email
    public long countByEmail(final String email);

    // возвращает всех пользователей
    public List<User> findAll();

    // возвращает всех пользователей по заданным параметрам сортировки
    public Page<User> findAll(final Pageable pageable);

    // возвращает пользователя по заданному логину
    public User findFirstByLogin(final String login);

    // возвращает пользователя по заданному email
    public User findFirstByEmail(final String email);

    // ищет пользователей по заданной подстроке
    @Query(
        "select u from User u where " +
        "u.login like %:seek% or " +
        "u.email like %:seek% or " +
        "u.lastName like %:seek% or " +
        "u.firstName like %:seek% or " +
        "u.patronymic like %:seek%"
    )
    public Page<User> seekBySubstring(@Param("seek") final String seek, final Pageable pageable);

    // активирует/деактивирует пользователя
    @Transactional
    @Modifying
    @Query("update User u set u.isActive = ?1 where u.id = ?2")
    public void activate(final Boolean isActive, final Integer id);
}
