package ru.volsu.coursebot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.dto.ArticleDto;
import ru.volsu.coursebot.dto.FileDto;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    @Lazy
    private AbsSender bot;

    Logger log = LoggerFactory.getLogger(getClass());

    public void sendArticleMessage(String chatId, List<ArticleDto> articleDtoList) throws BotException {
        for (int i = 0; i < articleDtoList.size(); i++) {
            int finalI = i;
            ArticleDto articleDto = articleDtoList.get(i);

            if (articleDto.getFileDtoList() != null && articleDto.getFileDtoList().size() > 1) {
                List<InputMedia> inputMediaPhotoList = articleDto.getFileDtoList()
                        .stream()
                        .map(file -> {
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(file.getFile()));
                            InputMedia inputMediaPhoto = new InputMediaPhoto();
                            inputMediaPhoto.setMedia(inputStream, file.getTitle());

                            if (finalI == 0) {
                                String text = buildText(articleDto);
                                inputMediaPhoto.setCaption(text);
                            }

                            return inputMediaPhoto;
                        })
                        .toList();

                sendSeveralPhotos(chatId, inputMediaPhotoList);
                sendDefaultMessage(chatId, buildText(articleDto));
            } else if (articleDto.getFileDtoList() != null && articleDto.getFileDtoList().size() == 1) {
                FileDto file = articleDto.getFileDtoList().get(0);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(file.getFile()));
                InputFile inputFile = new InputFile();
                inputFile.setMedia(inputStream, file.getTitle());

                sendOnePhoto(chatId, buildText(articleDto), inputFile);
            } else {
                sendDefaultMessage(chatId, buildText(articleDto));
            }
        }
    }

    public void sendDefaultMessage(String chatId, String text) throws BotException {
        try {
            bot.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке информации о статье: {}", e.getMessage());
            throw new BotException("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }

    public void sendSeveralPhotos(String chatId, List<InputMedia> inputMediaList) throws BotException {
        try {
            bot.execute(SendMediaGroup
                    .builder()
                            .chatId(chatId)
                            .medias(inputMediaList)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке информации о статье: {}", e.getMessage());
            throw new BotException("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }

    public void sendOnePhoto(String chatId, String text, InputFile inputFile) throws BotException {
        try {
            bot.execute(SendPhoto.builder()
                    .photo(inputFile)
                    .chatId(chatId)
                    .caption(text)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке информации о статье: {}", e.getMessage());
            throw new BotException("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }

    private static String buildText(ArticleDto articleDto) {
        return String.format("Название статьи: %s\n" +
                "\nНазвание предмета: %s\n" +
                "\nТекст: %s\n", articleDto.getTitle(), articleDto.getTag(), articleDto.getText());
    }
}
