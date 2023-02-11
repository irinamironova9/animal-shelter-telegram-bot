package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.ShelterType;
import com.example.animalsheltertelegrambot.models.ShelterUser;
import com.example.animalsheltertelegrambot.models.UserStatus;
import com.example.animalsheltertelegrambot.repositories.UserRepository;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final InfoMessageService infoMessageService;
    private final CallbackService callbackService;
    private final ReportService reportService;
    private final FileService fileService;

    public UserService(UserRepository userRepository, InfoMessageService infoMessageService, CallbackService callbackService, ReportService reportService, FileService fileService) {
        this.userRepository = userRepository;
        this.infoMessageService = infoMessageService;
        this.callbackService = callbackService;
        this.reportService = reportService;
        this.fileService = fileService;
    }

    public void updateHandle(Update update) {
        if (update.message() != null && update.message().text() != null) {
            Long chatId = update.message().chat().id();
            String userMessage = update.message().text();
            String userName = update.message().chat().firstName();
            if (this.userRepository.findById(chatId).isPresent()) {
                if (userRepository.findById(chatId).orElseThrow().getUserStatus() == null) {
                    messageHandle(chatId, "/start");
                } else {
                    messageHandle(chatId, userMessage);
                }
            } else {
                sendFirstGreetings(chatId, userName);
                this.userRepository.save(new ShelterUser(chatId, UserStatus.JUST_USING, ShelterType.DOG_SHELTER, null));
                messageHandle(chatId, "/start");
            };
        } else if (update.callbackQuery() != null) {
            MessageSender.sendCallbackQueryResponse(update.callbackQuery().id());
            Long chatId = update.callbackQuery().message().chat().id();
            String text = update.callbackQuery().data();
            messageHandle(chatId, MenuService.getCommandByButton(text));

        } else if (update.message().photo() != null && update.message().caption() != null) {
            String fileName = update.message().caption();
            Long chatId = update.message().chat().id();
            PhotoSize[] photos = update.message().photo();
            PhotoSize photo = Arrays.stream(photos).max(Comparator.comparing(PhotoSize::fileSize)).orElse(null);
            String fileId = photo.fileId();
            long fileSize = photo.fileSize();
            if (fileName.contains("shelter-")) {
                try {
                    fileService.uploadPhotoShelter(chatId, fileId, fileName, fileSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //determines what the user wants
    private void messageHandle(Long chatId, String userMessage) {
        ShelterUser user = userRepository.findById(chatId).orElseThrow(RuntimeException::new);
        if (userMessage.startsWith("/menu")) {
            switch (userMessage) {
                case "/menuChoiceShelter" -> MenuService.sendChoiceShelterMenu(chatId);
                case "/menuMainShelter" -> MenuService.sendMainShelterMenu(chatId);
                case "/menuAboutShelter" -> MenuService.sendAboutShelterMenu(chatId);
                case "/menuAnimalGuide" -> MenuService.sendAnimalGuideMenu(chatId);
                default -> MessageSender.sendMessage(chatId, "incorrect menu request!");
            }
        } else if (userMessage.equals("/start")){
            MessageSender.sendPhoto(chatId, "", "images/shelter/shelter-cat-or-dog.jpg");
            MenuService.sendChoiceShelterMenu(chatId);
        } else if (userMessage.equals("/catShelter")) {
            user.setShelterType(ShelterType.CAT_SHELTER);
            MessageSender.sendMessage(chatId, "Вы выбрали приют для котов");
            MenuService.sendMainShelterMenu(chatId);
            this.userRepository.save(user);
        } else if (userMessage.equals("/dogShelter")) {
            MessageSender.sendMessage(chatId, "Вы выбрали приют для собак");
            user.setShelterType(ShelterType.DOG_SHELTER);
            MenuService.sendMainShelterMenu(chatId);
            this.userRepository.save(user);
        } else if (infoMessageService.isInfo(userMessage, chatId)) {
            infoMessageService.sendInfoMessage(chatId, userMessage);
        } else if (callbackService.isCallbackRequest(userMessage, chatId)) {
            callbackService.sendCallbackMessage(userMessage, chatId);
        } else if (userMessage.equals("/volunteer")) {
            MessageSender.sendMessage(chatId, "Ок, позову свободного Волонтера");
        } else if (reportService.isReportService(userMessage, chatId)) {
            reportService.sendReportMessage();
        } else {
            MessageSender.sendMessage(chatId, "default", "Не понимаю... попробуй /start");
        }
    }

    private void sendFirstGreetings(Long chatId, String userName) {
        MessageSender.sendPhoto(chatId, "", "images/shelter/shelter_logo.jpg");
        MessageSender.sendMessage(chatId, "first greeting", "Привет! Я бот приюта для животных.\n" +
                "Могу рассказать о приюте для животных, а так же о том, что необходимо сделать, чтобы забрать питомца из приюта.");
    }



}
