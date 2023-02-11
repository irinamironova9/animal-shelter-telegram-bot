package com.example.animalsheltertelegrambot.service;

import com.example.animalsheltertelegrambot.models.ShelterUser;
import com.example.animalsheltertelegrambot.models.UserStatus;
import com.example.animalsheltertelegrambot.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CallbackService {
    private final UserRepository userRepository;

    public CallbackService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isMobileNumberValid(String number) {
        Pattern p = Pattern.compile("^\\+?[78][-(]?\\d{10}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    public void sendCallbackMessage(String userMessage, Long chatId) {
        ShelterUser user = this.userRepository.findById(chatId).orElseThrow(RuntimeException::new);
        if (user.getUserStatus().equals(UserStatus.SENDING_PHONE)) {
            if (isMobileNumberValid(userMessage)) {
                user.setPhoneNumber(userMessage);
                user.setUserStatus(UserStatus.JUST_USING);
                MessageSender.sendMessage(chatId, "callback number saved", "Записал номер телефона. Запрос на обратный звонок принят.");
            } else {
                user.setUserStatus(UserStatus.SENDING_PHONE_ANSWER);
                MessageSender.sendMessage(chatId, "callback number saved", "Неверный формат номера телефона, попробуете еще раз? (Выберите ответ: ДА или НЕТ)",
                        MenuService.createMenuButtons(MenuService.YES, MenuService.NO));
            }
        } else if (user.getUserStatus().equals(UserStatus.SENDING_PHONE_ANSWER)) {
            if (userMessage.equalsIgnoreCase("ДА")) {
                user.setUserStatus(UserStatus.SENDING_PHONE);
                MessageSender.sendMessage(chatId, "Введите номер телефона для обратной связи");
            } else {
                user.setUserStatus(UserStatus.JUST_USING);
                MessageSender.sendMessage(chatId, "Ок, понял, что Вы не хотите вводить номер еще раз.");
            }
        } else if (user.getUserStatus().equals(UserStatus.JUST_USING)) {
            if (user.getPhoneNumber() != null) {
                user.setUserStatus(UserStatus.SENDING_PHONE_IS_ACTUAL_ANSWER);
                MessageSender.sendMessage(chatId, "callbackService number is actual ","Ваш номер " + user.getPhoneNumber() + " - еще актуален? (Напишите ответ: ДА или НЕТ)", MenuService.createMenuButtons(MenuService.YES, MenuService.NO));
            } else {
                user.setUserStatus(UserStatus.SENDING_PHONE);
                MessageSender.sendMessage(chatId, "phone request", "Пожалуйста, напишите номер телефона(без отступов и разделяющих знаков) для обратной связи");
            }
        } else if (user.getUserStatus().equals(UserStatus.SENDING_PHONE_IS_ACTUAL_ANSWER)) {
            if (userMessage.equalsIgnoreCase("ДА")) {
                user.setUserStatus(UserStatus.JUST_USING);
                MessageSender.sendMessage(chatId, "ok", "Ок, тогда свяжемся по нему с Вами в ближайшее время");
            } else if (userMessage.equalsIgnoreCase("НЕТ")) {
                user.setUserStatus(UserStatus.SENDING_PHONE);
                MessageSender.sendMessage(chatId, "phone request", "Пожалуйста, напишите новый номер телефона для связи");
            } else {
                user.setUserStatus(UserStatus.JUST_USING);
                MessageSender.sendMessage(chatId, "Не получил конкретного ответа, раз обратный звонок Вас более не интересует, можете спросить меня о чем-либо еще");
                MenuService.sendMainShelterMenu(chatId);
            }
        }
        userRepository.save(user);
    }

    public boolean isCallbackRequest(String userMessage, Long chatId) {
        ShelterUser user = userRepository.findById(chatId).orElseThrow(RuntimeException::new);
        return userMessage.equals("/callback")
                || user.getUserStatus().equals(UserStatus.SENDING_PHONE)
                || user.getUserStatus().equals(UserStatus.SENDING_PHONE_ANSWER)
                || user.getUserStatus().equals(UserStatus.SENDING_PHONE_IS_ACTUAL_ANSWER);
    }
}
