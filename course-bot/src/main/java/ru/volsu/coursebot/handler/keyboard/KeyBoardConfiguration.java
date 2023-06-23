package ru.volsu.coursebot.handler.keyboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Configuration
public class KeyBoardConfiguration {

    @Bean
    public ReplyKeyboardMarkup mainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Поиск по названию статьи"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Поиск по тэгу"));
        List<KeyboardRow> keyboard = List.of(row1, row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    @Bean
    public ReplyKeyboardMarkup continueKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Продолжить"));
        List<KeyboardRow> keyboard = List.of(row1);

        ReplyKeyboardMarkup continueKeyboard = new ReplyKeyboardMarkup();
        continueKeyboard.setKeyboard(keyboard);
        continueKeyboard.setSelective(true);
        continueKeyboard.setResizeKeyboard(true);
        continueKeyboard.setOneTimeKeyboard(true);

        return continueKeyboard;
    }
}
