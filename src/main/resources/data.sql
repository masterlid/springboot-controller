-- добавление администратора
INSERT INTO users (
    login,
    pwd_hash,
    email,
    last_name,
    first_name,
    patronymic,
    privileges,
    gender,
    is_active
) SELECT 'admin', '$2a$08$OQrCVKD2tsV/Xg2jsBqtN.BrjhP./clTgujql5DToSAt5VaRR8vee', 'ivan.adminov@lid.tv', 'Админов', 'Иван', 'Иванович', 100, 5, true
    WHERE NOT EXISTS (
        SELECT login FROM users WHERE login = 'admin'
    );
