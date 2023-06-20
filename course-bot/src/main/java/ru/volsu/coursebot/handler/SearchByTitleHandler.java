package ru.volsu.coursebot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.enums.BotSectionEnum;

@Component
public class SearchByTitleHandler implements MessageHandler {
    @Override
    public SendMessage handle(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text("Реализация в работе")
                .build();
    }

    @Override
    public BotSectionEnum getBotState() {
        return BotSectionEnum.SEARCH_BY_TITLE;
    }
}
