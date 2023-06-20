package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.enums.BotSectionEnum;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BotSectionProcessor {

    private Map<BotSectionEnum, MessageHandler> messageHandlers;

    @Autowired
    public BotSectionProcessor(List<MessageHandler> messageHandlerList) {
        messageHandlers = messageHandlerList.stream()
                .collect(Collectors.toMap(MessageHandler::getBotState, Function.identity()));
    }

    public SendMessage handle(BotSectionEnum botSectionEnum, Update update) {
        return messageHandlers.get(botSectionEnum).handle(update);
    }
}
