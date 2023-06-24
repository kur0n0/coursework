package ru.volsu.coursebot.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.enums.BotSectionEnum;

public interface MessageHandler {
    BotApiMethod<?> handle(Update update) throws Exception;

    BotSectionEnum getBotState();
}
