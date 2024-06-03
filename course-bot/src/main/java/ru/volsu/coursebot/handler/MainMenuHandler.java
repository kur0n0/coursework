package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.volsu.coursebot.dto.HandleResult;
import ru.volsu.coursebot.dto.UserContext;
import ru.volsu.coursebot.enums.BotSectionEnum;

@Component
public class MainMenuHandler implements MessageHandler {

    @Autowired
    @Qualifier("mainMenuKeyboard")
    private ReplyKeyboardMarkup mainMenuKeyboard;

    @Override
    public HandleResult handle(Update update, UserContext userContext) {
        SendMessage.SendMessageBuilder sendMessage = SendMessage
                .builder()
                .chatId(update.getMessage().getChatId().toString())
                .replyMarkup(mainMenuKeyboard)
                .text("Выберите действие");
        return new HandleResult(sendMessage.build(), userContext);
    }

    @Override
    public BotSectionEnum getBotState() {
        return BotSectionEnum.MAIN_MENU;
    }
}
