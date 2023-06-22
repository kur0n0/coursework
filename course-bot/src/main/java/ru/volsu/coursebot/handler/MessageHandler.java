package ru.volsu.coursebot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.enums.BotSectionEnum;

public interface MessageHandler {
    SendMessage handle(Update update) throws Exception;

    BotSectionEnum getBotState();
}
