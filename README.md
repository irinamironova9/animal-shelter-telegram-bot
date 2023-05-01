<p align="center"> 
<img src="https://i.imgur.com/qZnhE08.png" width="200" alt="Shelter Logo">
</p>

<div id="badges" align="center">

# Telegram бот для приюта животных. Командный учебный проект
</div>

<div id="badges" align="center">

![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
</div>

___
## Описание телеграм - бота
Телеграм-бот для приюта животных, который отвечает на вопросы пользователей о том, что нужно знать и уметь, чтобы забрать животное из приюта.
Телеграмм-бот в течение испытательного срока принимает от новых хозяев ежедневные отчеты о том, как питомцы привыкают к новой обстановке.

Реализована следующая функциональность:
- выбор приюта: для кошек или для собак
- отправка ботом ответов на вопросы пользователей о приюте, правилах усыновления питомцев и т.д. (в зависимости от выбранного приюта)
- кнопки для выбора команд/разделов
- принятие от пользователей отчётов о питомце с фото и текстом
- сохранение фото и данных в БД
- ежедневное автоматическое напоминание об отправке отчёта

___
## Как запустить и протестировать
- Откройте проект в Вашей IDE (например, IntelliJIDEA)
- Укажите путь к Вашей базе данных в файле **[application.properties](src/main/resources/application.properties)**
- В этом же файле укажите токен Telegram бота, на котором хотите запустить приложение
- В файле **[telegram-bot.sql](src/main/resources/liquibase/scripts/telegram-bot.sql)** 
Вы найдёте скрипты, заполняющие БД данными по умолчанию. Так как данный бот рассчитан на работу 
с пользователями, и регистрация питомцев и усыновителей не подразумевается, мы добавили заполнение БД 
данными по умолчанию для удобства запуска и тестирования.
- В соответствии с ТЗ, усыновителя должен зарегистрировать волонтёр. Обычный пользователь отправлять отчёты не может. 
Поэтому, чтобы протестировать функцию отправки отчётов о питомце, на 105 строчке данного файла 
впишите username аккаунта Telegram, с которого собираетесь переписываться с ботом.
- Запустите метод **main** в файле **[AnimalShelterTelegramBotApplication.java](src/main/java/com/example/animalsheltertelegrambot/AnimalShelterTelegramBotApplication.java)**
- Готово!

___
## Техническое задание проекта:
- [ТЗ](https://skyengpublic.notion.site/4509dd17f5f840f1ba6807fe83aa9c15)
- [ТЗ на проект с правками](https://skyengpublic.notion.site/30eb1f5ef036467d8ae0527e7d625718)
___
## Команда разработчиков:
- [Никита Мавродий](https://github.com/nikitamavrodiy)
- [Ирина Миронова](https://github.com/irinamironova9)
- [Альберт Татаринов](https://github.com/letuu)
- [Анастасия Карпова](https://github.com/AnastasiaSergeeva05)
- [Алексей Земский](https://github.com/zemscky)
___
## В проекте используются
  - Java 17
  - Maven
  - Stream API
  - Spring Boot
  - Spring Web
  - Spring Data
  - Spring JPA
  - Hibernate
  - PostgreSQL
  - Liquibase
___
## Демонстрация работы бота:
[Ссылка на обзор бота на Driver Google](https://drive.google.com/file/d/15AbNn0T2qWO4Ol4ypMtHuqE63LQRcmSI/view?usp=share_link)


https://user-images.githubusercontent.com/113126626/218460104-fd49c50d-286d-47b3-86c3-23c801a064dc.mp4


___
