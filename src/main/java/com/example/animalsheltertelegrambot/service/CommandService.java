package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.Contact;
import com.example.animalsheltertelegrambot.models.InfoMessage;
import com.example.animalsheltertelegrambot.models.PhotoFile;
import com.example.animalsheltertelegrambot.repositories.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class CommandService {

    private final Logger logger = LoggerFactory.getLogger(CommandService.class);

    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final InfoMessageRepository messageRepository;
    private final ContactRepository contactRepository;
    private final PhotoFileRepository photoFileRepository;
    private TelegramBot telegramBot;

    public CommandService(ClientRepository clientRepository, AnimalRepository animalRepository, InfoMessageRepository messageRepository, ContactRepository contactRepository, PhotoFileRepository photoFileRepository) {
        this.clientRepository = clientRepository;
        this.animalRepository = animalRepository;
        this.messageRepository = messageRepository;
        this.contactRepository = contactRepository;
        this.photoFileRepository = photoFileRepository;
    }

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    /**
     * Accepts the request and determine what the client wants
     * @see CommandService#sendResponseToCommand
     * @see CommandService#isCommand
     * @see CommandService#isInfoRequest
     * @see CommandService#isMobileNumberValid
     *
     * Finds an informational message in the database by the command received
     * from user which serves as a primary key. If user`s message is not
     * a command, or the command was not found method sends a message
     * stating that requested information was not found.
     * @see CommandService#sendInfoMessage
     * @see CommandService#getNotFoundInfoMessage()
     *
     * sends a callback message or contact saved message
     * @see CommandService#sendCallbackMessage
     * @see CommandService#sendContactSavedMessage
     *
     * creates a new contact and puts it in the database
     * @see CommandService#saveContact
     *
     * sends message and keyboard to the user and performs logging
     * @see CommandService#sendMessage
     */

    public void sendResponseToCommand(Long chatId, String text) {
        sendResponseToCommand(chatId, text, null);
    }

    // accepts the request and determine what the client wants
    public void sendResponseToCommand(Long chatId, String text, InlineKeyboardMarkup keyboardMarkup) {
        logger.info("request processing");
        if (isCommand(text)) {
            if (isInfoRequest(text)) {
                sendInfoMessage(chatId, text, keyboardMarkup);
            } else if (text.equals("/callback")) {
                sendCallbackMessage(chatId, keyboardMarkup);
            }
        } else if (isMobileNumberValid(text)) {
            saveContact(chatId, text);
            sendContactSavedMessage(chatId);
            sendCallbackMessage(chatId, keyboardMarkup);
        } else {
            sendMessage(chatId, "unknown command", "Unknown command!", keyboardMarkup);
        }
    }

    //searches for an informational message in the database and sends it to the user
    public SendResponse sendInfoMessage(Long chatId, String tag, InlineKeyboardMarkup keyboardMarkup) {
        InfoMessage infoMessage = this.messageRepository.
                findById(tag).
                orElse(getNotFoundInfoMessage());
        return sendMessage(chatId, tag, infoMessage.getText(), keyboardMarkup);
    }

    //sends a callback message
    public SendResponse sendCallbackMessage(Long chatId, InlineKeyboardMarkup keyboardMarkup) {
        if (this.contactRepository.findById(chatId).isPresent()) {
            return sendMessage(chatId, "callback", "Запрос принят. Так же, Вы всегда можете прислать новый номер для обратной связи", keyboardMarkup);
        } else {
            return sendMessage(chatId, "contact not found", "Пожалуйста, напишите номер телефона(без отступов и разделяющих знаков) для обратной связи", null);
        }
    }

    //sends contact saved message
    public SendResponse sendContactSavedMessage(Long chatId) {
        return sendMessage(chatId, "contact saved", "The number is saved", null);
    }

    public void sendCallbackQueryResponse(String id) {
        telegramBot.execute(new AnswerCallbackQuery(id));
    }

    //sends message to the user and performs logging
    public SendResponse sendMessage(Long chatId, String name, String text,
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
        return response;
    }

    public void SendPhoto(Long chatId, String caption, String imagePath) {
//        try {
//            File image = ResourceUtils.getFile("classpath:" + imagePath);
            PhotoFile photoFile = photoFileRepository.findById(1).orElseThrow();
            SendPhoto sendPhoto = new SendPhoto(chatId, photoFile.getData());
            sendPhoto.caption(caption);
            telegramBot.execute(sendPhoto);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    //checks whether the incoming text is a command
    public boolean isCommand(String text) {
        return text.startsWith("/");
    }

    //checks whether the incoming text is a request for information
    public boolean isInfoRequest(String tag) {
        return this.messageRepository.findById(tag).isPresent();
    }

    //checks whether the incoming text is a phone number
    public boolean isMobileNumberValid(String number) {
        Pattern p = Pattern.compile("^\\+?[78][-(]?\\d{10}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    //creates a new contact and puts it in the database
    public Contact saveContact(Long chatId, String mobileNumber) {
        return this.contactRepository.save(new Contact(chatId, mobileNumber));
    }

    //returns an information message that was not found for further sending to the user
    public InfoMessage getNotFoundInfoMessage() {
        return new InfoMessage("not found", "Information not found, please try again later");
    }
}
