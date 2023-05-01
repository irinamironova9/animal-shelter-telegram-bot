package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.Shelter;
import com.example.animalsheltertelegrambot.repositories.AnimalRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;


@Service
public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final AnimalRepository animalRepository;
    private static TelegramBot telegramBot;

    public MessageSender(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public static void setTelegramBot(TelegramBot telegramBot) {
        MessageSender.telegramBot = telegramBot;
    }

    /**
     * sends a confirmation that we have accepted a push-button response from the user
     *
     * @param id is argument from update.callbackQuery().id()
     */
    public static BaseResponse sendCallbackQueryResponse(String id) {
        return telegramBot.execute(new AnswerCallbackQuery(id));
    }

    /**
     * sends message to the user and performs logging
     *
     * @param chatId         user's chatId
     * @param name           message's name for logger
     * @param text           text for sending
     * @param keyboardMarkup if it is not null, then we send the keyboard to the user
     * @return
     */
    public static void sendMessage(Long chatId, String name, String text,
                                   InlineKeyboardMarkup keyboardMarkup) {
        logger.info("Sending the " + name + " message");

        SendMessage sm = new SendMessage(chatId, text);
        SendResponse response;

        if (keyboardMarkup == null) {
            response = telegramBot.execute(sm);
        } else {
            logger.info("Sending the keyboard");
            response = telegramBot.execute(sm.replyMarkup(keyboardMarkup));
        }
        if (!response.isOk()) {
            logger.error("Could not send the " + name + " message! " +
                    "Error code: {}", response.errorCode());
        }
    }

    public static void sendMessage(Long chatId, String nameOfMessageForLogger, String text) {
        sendMessage(chatId, nameOfMessageForLogger, text, null);
    }

    public static void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, text, null);
    }

    public static void sendMessage(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }

    //Метод отправки фото в чат из директории проекта resources
    public static void sendPhoto(Long chatId, String caption, String imagePath) {
        try {
            File image = ResourceUtils.getFile("classpath:" + imagePath);
            SendPhoto sendPhoto = new SendPhoto(chatId, image);
            sendPhoto.caption(caption);
            telegramBot.execute(sendPhoto);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Метод отправки в чат фото с локального диска по пути, хранящемуся в БД
    public static void sendPhotoDbLinkLocal(Long chatId, String caption, String imagePath) {
        File locationMap = new File(imagePath);
        if (locationMap.isFile()) {
            SendPhoto sendPhoto = new SendPhoto(chatId, new File(imagePath));
            sendPhoto.caption(caption);
            telegramBot.execute(sendPhoto);
        } else {
            sendMessage(chatId, "К сожалению схема проезда к приюту пока не загружена.\n" +
                    "Схема проезда к головному офису приютов:");
            sendPhoto(chatId, "", "images/shelter/shelter_main_location.jpg");
        }
    }

    public static void sendAddress(Long chatId, Shelter shelter) {
        telegramBot.execute(
                new SendMessage(chatId,
                        "Контактные данные приюта " + "'" + shelter.getName() + "'" + "\n" +
                                "Адрес: " + shelter.getAddress() + "\n" +
                                "Телефон: " + shelter.getTelephoneNumber() + "\n" +
                                "Расписание: " + shelter.getTimetable()));
    }

    public static void sendPhoto(SendPhoto sendPhoto) {
        telegramBot.execute(sendPhoto);
    }
}
