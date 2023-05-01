-- liquibase formatted sql

-- changeset irina:1

create table shelter
(
    number           text primary key,
    name             text,
    address          text,
    telephone_number text,
    timetable        text,
    shelter_type     int
);

create table location_map
(
    number     text primary key,
    file_path  text,
    file_size  bigint,
    shelter_number text references shelter (number)
);

-- create table info_message
-- (
--     tag  text primary key,
--     text text
-- );

create table cat_info_message
(
    tag  text primary key,
    text text
);

create table dog_info_message
(
    tag  text primary key,
    text text
);

create table shelter_user
(
    chat_id      bigint primary key,
    user_status  int,
    shelter_type int,
    phone_number text,
    username     text
);

create table adopter
(
    id                bigserial primary key,
    chat_id           bigint,
    username          text,
    name              text,
    address           text,
    age               int,
    telephone_number  text,
    current_report_id bigint
);

create table animal
(
    id                     bigserial primary key,
    name                   text,
    color                  text,
    species                text,
    age                    int,
    unique_characteristics text,
    special_needs          text,
    adopter_id             bigint references adopter (id),
    shelter_id             text references shelter (number),
    probation_period_id    bigint
);

create table probation_period
(
    id                              bigserial primary key,
    ends                            date,
    was_successful                  boolean,
    volunteers_comment              text,
    need_to_send_volunteers_comment boolean,
    animal_id                       bigint references animal (id),
    adopter_id                      bigint references adopter (id)
);

create table report
(
    id                  bigserial primary key,
    probation_period_id bigint references probation_period (id),
    date                date,
    entry               text,
    photo_id            text
);

-- -- changeset nmavro:2
--
-- INSERT INTO info_message
--     (tag, text)
-- VALUES ('/start', 'Привет! Я бот приюта для животных. Выбери раздел из списка:'),
--        ('/aboutshelter',
--         'Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.' ||
--         'Для того, чтобы спасти жизнь, достаточно совсем немного...' ||
--         'Мы постарались создать сообщество для помощи людям, которые задумываются о том, чтобы забрать собаку или кошку домой.' ||
--         'Наш телеграмм-бот может ответить на вопросы людей о том, что нужно знать и уметь, чтобы забрать животное из приюта.'),
--        ('/dogmenu', 'Здесь должно быть dogmenu, но пока что его нету'),
--        ('/sendreportmenu', 'Здесь должно быть sendreportmenu, но пока что его нету'),
--        ('/volunteer', 'Хорошо, позову Волонтера (или нет)'),
--        ('/addressandschedule', 'Адрес и часы работы головного офиса приютов: ' || '
--     ' || 'г. Астана, ул. Прохладная, д. 1. Часы работы: 10:00 - 20:00'),
--        ('/safety', 'техника безопасности на территории приюта'),
--        ('/description', 'Выберите раздел:');

-- changeset albert:3
INSERT INTO shelter (number, name, address, telephone_number, timetable, shelter_type)
VALUES ('1', 'Котики', 'г. Астана, ул. Зеленая, д. 1', '+7-999-99-99', 'Пн-Пт с 8-00 до 20-00', 0),
       ('2', 'Собачки', 'г. Астана, ул. Лесная, д. 2', '+7-888-88-88', 'Пн-Пт с 8-00 до 20-00', 1);

-- -- changeset zemscky:6
-- INSERT INTO info_message (tag, text)
-- VALUES ('/documents',
--         'При себе из документов обязательно иметь только паспорт. Документ обязательно фотографируют и вносят данные в базу данных. ' ||
--         'Так как все собаки в приюте чипированы, необходимо внести в международную базу изменения о том, что собственник собаки сменился. ' ||
--         'В самом учреждении также практикуют подписание договора, это внутренний документ, который остается в приюте. ' ||
--         'Кроме того, составляется акт убытия — эти данные передают в Минсельхоз. Поэтому любое появление или передача животного в приюте фиксируется. ' ||
--         'Так же вам предложат пройти что-то вроде собеседования, а в ходе общения уже вынесут решение, доверить вам животное или нет. ' ||
--         'Для дополнительной консультации можете обратиться к волонтеру: /volunteer'::varchar(255)),
--        ('/advice',
--         '1) Готовьтесь к тому, что собака не сразу привыкнет к новой обстановке. Дайте ей время, чтобы она смогла обследовать все углы и познакомиться со всеми членами семьи.' ||
--         'В первый час лучше не слишком настойчиво к ней приставать. ' ||
--         '2) Уже в первый день можно дать животному кличку и начать ее использовать. Собачки быстро привыкают к своему имени. ' ||
--         '3) Чтобы пес привык к вам, в первые дни стоит проводить с ним больше времени. По этой причине лучше брать щенка на выходных.'::varchar(255)),
--        ('/refusal',
--         '1) Животные НЕ ПЕРЕДАЮТСЯ лицам, пришедшим в состоянии алкогольного или наркотического опьянения.' ||
--         '2) Животные НЕ ПЕРЕДАЮТСЯ гражданам, проживающим в арендуемом жилье. ' ||
--         '3) Сотрудники приюта МОГУТ ОТКАЗАТЬ в передаче животного лицу, ранее утратившему животное, при этом, причины утраты животного признаны сотрудниками приюта не уважительными. ' ||
--         'Данный перечень не является полным. Сотрудники приюта оставляют за собой ПРАВО ОТКАЗАТЬ в передаче животного и по иным причинам. ' ||
--         'Всем желающим взять животное из приюта надо помнить о том, что передача животного из приюта новому владельцу – это НЕ ОБЯЗАННОСТЬ, а ПРАВО приюта.'::varchar(255));

-- -- changeset nmavro:7
-- INSERT INTO dog_info_message (tag, text)
-- VALUES ('/start', 'Привет! Я бот приюта для животных. Выбери раздел из списка:');

-- changeset nmavro:8
INSERT INTO dog_info_message (tag, text)
VALUES ('/dogmenu', 'Здесь должно быть dogmenu, но пока что его нету');

-- changeset nmavro:9
INSERT INTO dog_info_message (tag, text)
VALUES ('/sendreportmenu', 'Здесь должно быть sendreportmenu, но пока что его нету');

-- changeset nmavro:10
INSERT INTO dog_info_message (tag, text)
VALUES ('/volunteer', 'Хорошо, позову Волонтера (или нет)');

-- changeset nmavro:11
INSERT INTO dog_info_message (tag, text)
VALUES ('/addressandschedule', 'Адрес и часы работы головного офиса приютов: ' || '
' || 'г. Астана, ул. Прохладная, д. 1. Часы работы: 10:00 - 20:00');

-- changeset nmavro:12
INSERT INTO dog_info_message (tag, text)
VALUES ('/safety', 'техника безопасности на территории приюта');

-- changeset nmavro:13
INSERT INTO dog_info_message (tag, text)
VALUES ('/description', 'Выберите раздел:');

-- changeset nmavro:14
INSERT INTO dog_info_message (tag, text)
VALUES ('/passRegistration', 'У охраны собак такой номер +64684684');

-- changeset nmavro:15
INSERT INTO dog_info_message (tag, text)
VALUES ('/aboutshelter',
        'Мы приют для собак Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.Для того, чтобы спасти жизнь, достаточно совсем немного...');

-- changeset nmavro:16
INSERT INTO cat_info_message (tag, text)
VALUES ('/catmenu', 'Здесь должно быть catmenu, но пока что его нету');

-- changeset nmavro:17
INSERT INTO cat_info_message (tag, text)
VALUES ('/sendreportmenu', 'Здесь должно быть sendreportmenu, но пока что его нету');

-- changeset nmavro:18
INSERT INTO cat_info_message (tag, text)
VALUES ('/volunteer', 'Хорошо, позову Волонтера (или нет)');

-- changeset nmavro:19
INSERT INTO cat_info_message (tag, text)
VALUES ('/addressandschedule', 'Адрес и часы работы головного офиса приютов: ' || '
' || 'г. Астана, ул. Прохладная, д. 1. Часы работы: 10:00 - 20:00');

-- changeset nmavro:20
INSERT INTO cat_info_message (tag, text)
VALUES ('/safety', 'техника безопасности на территории приюта');

-- changeset nmavro:21
INSERT INTO cat_info_message (tag, text)
VALUES ('/description', 'Выберите раздел:');

-- changeset nmavro:22
INSERT INTO cat_info_message (tag, text)
VALUES ('/passRegistration', 'У охраны собак такой номер +64684684');

-- changeset nmavro:23
INSERT INTO cat_info_message (tag, text)
VALUES ('/aboutshelter',
        'Мы приют для кошек. Все мы знаем, что нашим маленьким друзьям очень часто приходится нелегко, и только мы можем им помочь.Для того, чтобы спасти жизнь, достаточно совсем немного...');

-- changeset albert:24
INSERT INTO location_map (number, file_path, file_size, shelter_number)
VALUES ('1', '\shelters\shelter-map-1.jpg', 280887, '1'),
       ('2', '\shelters\shelter-map-2.jpg', 82963, '2');