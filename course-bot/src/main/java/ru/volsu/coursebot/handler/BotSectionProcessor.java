package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.exceptions.CoreException;

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

    public BotApiMethod<?> handle(BotSectionEnum botSectionEnum, Update update) throws BotException, CoreException {
        return messageHandlers.get(botSectionEnum).handle(update);
    }
}
