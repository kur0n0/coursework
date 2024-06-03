package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.dto.HandleResult;
import ru.volsu.coursebot.dto.UserContext;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.exceptions.CoreException;
import ru.volsu.coursebot.service.CacheService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BotSectionProcessor {

    private final Map<BotSectionEnum, MessageHandler> messageHandlers;
    private final CacheService cacheService;

    @Autowired
    public BotSectionProcessor(List<MessageHandler> messageHandlerList, CacheService cacheService) {
        this.messageHandlers = messageHandlerList.stream()
                .collect(Collectors.toMap(MessageHandler::getBotState, Function.identity()));
        this.cacheService = cacheService;
    }

    public BotApiMethod<?> handle(BotSectionEnum botSectionEnum, Update update) throws BotException, CoreException {
        MessageHandler messageHandler = messageHandlers.get(botSectionEnum);

        Long userId = update.getMessage().getFrom().getId();

        UserContext userContext = cacheService.getUserContext(userId);
        if (userContext != null) {
            userContext.setCurrentBotSection(botSectionEnum);
        } else {
            userContext = new UserContext(userId, botSectionEnum);
        }

        HandleResult handleResult = messageHandler.handle(update, userContext);
        cacheService.updateUserContext(handleResult.getUserContext());

        return handleResult.getBotAction();
    }
}
