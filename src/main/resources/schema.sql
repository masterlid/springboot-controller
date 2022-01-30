-- таблица со списком пользователей
CREATE TABLE IF NOT EXISTS users (
    id         INT NOT NULL AUTO_INCREMENT,                             -- идентификатор
    login      VARCHAR(64) NOT NULL UNIQUE,                             -- логин
    pwd_hash   VARCHAR(64) NOT NULL UNIQUE,                             -- хэш пароля в bcrypt
    email      VARCHAR(256) NOT NULL UNIQUE,                            -- email
    last_name  VARCHAR(256) NOT NULL,                                   -- фамилия
    first_name VARCHAR(256) NOT NULL,                                   -- имя
    patronymic VARCHAR(256) NULL DEFAULT NULL,                          -- отчество
    privileges TINYINT NOT NULL DEFAULT 0,                              -- полномочия
    gender     TINYINT NOT NULL DEFAULT 0,                              -- пол
    is_active  BOOLEAN NOT NULL DEFAULT TRUE,                           -- включён/выключен
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,            -- timestamp создания
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP, -- timestamp изменения
    PRIMARY KEY (id)
);

-- таблица с контактами пользователя
CREATE TABLE IF NOT EXISTS contacts (
    id         INT NOT NULL AUTO_INCREMENT,                             -- идентификатор
    user_id    INT NOT NULL,                                            -- идентификатор пользователя
    type       TINYINT NOT NULL,                                        -- тип контакта
    contact    VARCHAR(256) NOT NULL,                                   -- сам контакт
    is_active  BOOLEAN NOT NULL DEFAULT TRUE,                           -- включён/выключен
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,            -- timestamp создания
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP, -- timestamp изменения
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
