package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.ShelterType;
import com.example.animalsheltertelegrambot.models.ShelterUser;
import com.example.animalsheltertelegrambot.models.UserStatus;
import com.example.animalsheltertelegrambot.repositories.ShelterUserRepository;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final ShelterUserRepository shelterUserRepository;
    private final InfoMessageService infoMessageService;
    private final CallbackService callbackService;
    private final ReportService reportService;

    public UserService(ShelterUserRepository shelterUserRepository, InfoMessageService infoMessageService, CallbackService callbackService, ReportService reportService) {
        this.shelterUserRepository = shelterUserRepository;
        this.infoMessageService = infoMessageService;
        this.callbackService = callbackService;
        this.reportService = reportService;
    }

    public void updateHandle(Update update) {
        if (update.message() != null && update.message().text() != null) {
//            String username = update.message().from().username();
            Long chatId = update.message().chat().id();
            String userMessage = update.message().text();
            String userName = update.message().chat().firstName();
            if (this.shelterUserRepository.findById(chatId).isPresent()) {
                if (shelterUserRepository.findById(chatId).orElseThrow().getUserStatus() == null) {
                    messageHandler(chatId, "/start");
                } else {
                    messageHandler(chatId, userMessage);
                }
            } else {
                sendFirstGreetings(chatId, userName);
                this.shelterUserRepository.save(new ShelterUser(chatId, UserStatus.JUST_USING, ShelterType.DOG_SHELTER, null));
                messageHandler(chatId, "/start");
            };
        } else if (update.callbackQuery() != null) {
            MessageSender.sendCallbackQueryResponse(update.callbackQuery().id());
            Long chatId = update.callbackQuery().message().chat().id();
            String text = update.callbackQuery().data();
            messageHandler(chatId, MenuService.getCommandByButton(text));
        }
    }



    //determines what the user wants
    private void messageHandler(Long chatId, String userMessage) {
        ShelterUser user = shelterUserRepository.findById(chatId).orElseThrow(RuntimeException::new);
        if (userMessage.startsWith("/menu")) {
            switch (userMessage) {
                case "/menuChoiceShelter" -> MenuService.sendChoiceShelterMenu(chatId);
                case "/menuMainShelter" -> MenuService.sendMainShelterMenu(chatId);
                case "/menuAboutShelter" -> MenuService.sendAboutShelterMenu(chatId);
                case "/menuAnimalGuide" -> MenuService.sendAnimalGuideMenu(chatId);
                default -> MessageSender.sendMessage(chatId, "incorrect menu request!");
            }
        } else if (userMessage.equals("/start")){
            MenuService.sendChoiceShelterMenu(chatId);
        } else if (userMessage.equals("/catShelter")) {
            user.setShelterType(ShelterType.CAT_SHELTER);
            MessageSender.sendMessage(chatId, "Вы выбрали приют для котов");
            MenuService.sendMainShelterMenu(chatId);
            this.shelterUserRepository.save(user);
        } else if (userMessage.equals("/dogShelter")) {
            MessageSender.sendMessage(chatId, "Вы выбрали приют для собак");
            user.setShelterType(ShelterType.DOG_SHELTER);
            MenuService.sendMainShelterMenu(chatId);
            this.shelterUserRepository.save(user);
        } else if (infoMessageService.isInfo(userMessage, chatId)) {
            infoMessageService.sendInfoMessage(chatId, userMessage);
        } else if (callbackService.isCallbackRequest(userMessage, chatId)) {
            callbackService.sendCallbackMessage(userMessage, chatId);
        } else if (userMessage.equals("/volunteer")) {
            MessageSender.sendMessage(chatId, "Ок, позову свободного Волонтера");
        } else if (reportService.isSendReportCommand(userMessage, chatId)) {
            reportService.sendReportFirstStep(chatId);
        } else {
            MessageSender.sendMessage(chatId, "default", "Не понимаю... попробуй /start");
        }
    }

    private void sendFirstGreetings(Long chatId, String userName) {
        MessageSender.sendMessage(chatId, "first greeting", "Привет! Я бот приюта для животных.\n" +
                "Могу рассказать о приюте для животных, а так же о том, что необходимо сделать, чтобы забрать питомца из приюта.");
    }



}
