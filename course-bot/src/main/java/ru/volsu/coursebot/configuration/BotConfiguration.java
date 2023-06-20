package ru.volsu.coursebot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.volsu.coursebot.Bot;

@Configuration
public class BotConfiguration {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    @Value("${webhook.path}")
    private String webHookPath;

    @Bean
    public SetWebhook buildWebHook() {
        return SetWebhook.builder().url(webHookPath).build();
    }

    @Bean
    public Bot setWebHook(SetWebhook setWebhookInstance) {
        return new Bot(setWebhookInstance, webHookPath, botUsername, botToken);
    }
}