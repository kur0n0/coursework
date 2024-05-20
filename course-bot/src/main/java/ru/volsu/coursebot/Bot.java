package ru.volsu.coursebot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.exceptions.CoreException;
import ru.volsu.coursebot.handler.BotSectionProcessor;
import ru.volsu.coursebot.service.UserCacheService;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Bot extends SpringWebhookBot {

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private BotSectionProcessor botSectionProcessor;

    Logger log = LoggerFactory.getLogger(getClass());

    private String botUsername;
    private String botToken;
    private String webHookPath;

    public Bot(SetWebhook webhook, String webHookPath, String botUsername, String botToken) {
        super(webhook);
        this.botUsername = botUsername;
        this.webHookPath = webHookPath;
        this.botToken = botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();

        BotSectionEnum botSectionEnum = switch (message.getText()) {
            case "Главное меню" -> BotSectionEnum.MAIN_MENU;
            case "Поиск по названию статьи" -> BotSectionEnum.SEARCH_BY_TITLE;
            case "Поиск по предмету статьи" -> BotSectionEnum.SEARCH_BY_TAG;
            case "Полный поиск по строке" -> BotSectionEnum.SEARCH_FULL_TEXT;
            case "Решить случайное задание" -> BotSectionEnum.SOLVE_TASK;
            default -> userCacheService.getBotSectionByUserId(userId);
        };

        userCacheService.setBotSectionForUser(userId, botSectionEnum);
        try {
            return botSectionProcessor.handle(botSectionEnum, update);
        } catch (BotException | CoreException e) {
            log.error("При обработке сообщения: {}, ошибка: {}", botSectionEnum, e.getMessage());
            return SendMessage.builder()
                    .chatId(userId.toString())
                    .text(String.format("Возникла ошибка: %s\n stack trace: %s", e.getMessage(), Arrays.stream(e.getStackTrace())
                            .map(StackTraceElement::toString)
                            .collect(Collectors.joining("\n"))))
                    .build();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }
}
