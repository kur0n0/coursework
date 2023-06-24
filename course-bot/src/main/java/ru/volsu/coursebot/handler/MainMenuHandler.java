package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.volsu.coursebot.enums.BotSectionEnum;

@Component
public class MainMenuHandler implements MessageHandler {

    @Autowired
    @Qualifier("mainMenuKeyboard")
    private ReplyKeyboardMarkup mainMenuKeyboard;

    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(message.getChatId().toString())
                .replyMarkup(mainMenuKeyboard)
                .text("Выберите действие")
                .build();


        return sendMessage;
    }

    @Override
    public BotSectionEnum getBotState() {
        return BotSectionEnum.MAIN_MENU;
    }
}
