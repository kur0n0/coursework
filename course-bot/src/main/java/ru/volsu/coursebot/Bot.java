package ru.volsu.coursebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.handler.BotSectionProcessor;
import ru.volsu.coursebot.service.UserCacheService;

public class Bot extends SpringWebhookBot {

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private BotSectionProcessor botSectionProcessor;

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
            case "Поиск по тэгу" -> BotSectionEnum.SEARCH_BY_TAG;
            default -> userCacheService.getBotSectionByUserId(userId);
        };

        userCacheService.setBotSectionForUser(userId, botSectionEnum);
        try {
            return botSectionProcessor.handle(botSectionEnum, update);
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(userId.toString())
                    .text("Возникла ошибка: " + e.getMessage())
                    .build();// todo Сделать свои экспешены и хэндлеры для него, отправлять сообщение в боте с описанием ошибки
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
