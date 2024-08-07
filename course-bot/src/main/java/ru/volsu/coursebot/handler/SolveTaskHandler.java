package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.commons.dto.TaskDTO;
import ru.volsu.coursebot.dto.HandleResult;
import ru.volsu.coursebot.dto.UserContext;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.enums.UserCommandEnum;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.exceptions.CoreException;
import ru.volsu.coursebot.service.CourseCoreService;
import ru.volsu.coursebot.service.MessageService;

import java.util.List;
import java.util.Optional;

@Service
public class SolveTaskHandler implements MessageHandler {

    @Autowired
    private CourseCoreService courseCoreService;

    @Autowired
    private MessageService messageService;

    @Autowired
    @Qualifier(value = "mainMenuKeyboard")
    private ReplyKeyboardMarkup mainMenuKeyboard;

    private static final String ENTER_ANSWER = "Ввести ответ";
    private static final String GET_HINT = "Получить подсказку";

    @Override
    public HandleResult handle(Update update, UserContext userContext) throws BotException, CoreException {
        Message message = update.getMessage();
        User from = message.getFrom();
        String chatId = message.getChatId().toString();

        UserCommandEnum userCommand = Optional.ofNullable(userContext.getLastCommand()).orElse(UserCommandEnum.SHOW_RANDOM_TASK);
        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder()
                .chatId(chatId);
        switch (userCommand) {
            case SHOW_RANDOM_TASK -> {
                Optional<TaskDTO> taskOpt = courseCoreService.getRandomNotSolvedTask(from.getUserName());
                userContext.setLastCommand(UserCommandEnum.WAIT_ACTION);
                if (taskOpt.isPresent()) {
                    TaskDTO task = taskOpt.get();
                    userContext.setTaskDTO(task);

                    sendMessageBuilder
                            .text(task.getQuestion())
                            .parseMode("Markdown")
                            .replyMarkup(getKeyBoard());
                } else {
                    sendMessageBuilder
                            .text("Вы решили все задания!")
                            .parseMode("Markdown")
                            .replyMarkup(mainMenuKeyboard);
                }
            }
            case WAIT_ACTION -> {
                String text = message.getText();
                if (text.equalsIgnoreCase(ENTER_ANSWER)) {
                    userContext.setLastCommand(UserCommandEnum.HANDLE_ANSWER);
                    sendMessageBuilder.text("Введите ответ");
                } else if (text.equalsIgnoreCase(GET_HINT)) {
                    String textToSend = "Подсказка для данного задания отсутствует";
                    ArticleDto articleDto = userContext.getTaskDTO().getHint();
                    if (articleDto != null) {
                        messageService.sendArticleMessage(chatId, List.of(articleDto));
                        textToSend = "К сожалению подсказки нет :(";
                    }
                    userContext.setLastCommand(UserCommandEnum.WAIT_ACTION);
                    sendMessageBuilder.text(textToSend);
                } else {
                    userContext.setLastCommand(null);
                    userContext.setTaskDTO(null);
                    userContext.setCurrentBotSection(BotSectionEnum.MAIN_MENU);

                    sendMessageBuilder
                            .replyMarkup(mainMenuKeyboard)
                            .parseMode("Markdown")
                            .text("Переход в главное меню");
                }
            }
            case HANDLE_ANSWER -> {
                String userAnswer = message.getText();
                TaskDTO taskDTO = userContext.getTaskDTO();
                String correctAnswer = taskDTO.getAnswer();

                Object answer = userAnswer;
                if (taskDTO.getAnswerMapping().equals(TaskDTO.ANSWER_MAPPING_LONG)) {
                    answer = Long.parseLong(userAnswer);
                }

                String text = "Ответ неправильный, попробуйте еще раз!";
                courseCoreService.createTaskHistory(taskDTO.getTaskId(), from.getUserName(), answer.toString());
                if ((taskDTO.getAnswerMapping().equals(TaskDTO.ANSWER_MAPPING_LONG) && answer.equals(Long.parseLong(correctAnswer))) ||
                        (taskDTO.getAnswerMapping().equals(TaskDTO.ANSWER_MAPPING_STRING) && answer.equals(correctAnswer))) {
                    courseCoreService.createSolvedTask(from.getUserName(), taskDTO.getTaskId());
                    text = "Ответ верный!";
                    userContext.setLastCommand(null);
                    userContext.setTaskDTO(null);
                    userContext.setCurrentBotSection(BotSectionEnum.MAIN_MENU);

                    sendMessageBuilder
                            .replyMarkup(mainMenuKeyboard)
                            .parseMode("Markdown")
                            .text(text);
                }

                userContext.setLastCommand(UserCommandEnum.WAIT_ACTION);
                sendMessageBuilder
                        .text(text);

            }
        }

        return new HandleResult(sendMessageBuilder.build(), userContext);
    }

    @Override
    public BotSectionEnum getBotState() {
        return BotSectionEnum.SOLVE_TASK;
    }

    public ReplyKeyboardMarkup getKeyBoard() {
        KeyboardRow mainMenu = new KeyboardRow();
        mainMenu.add(new KeyboardButton("Главное меню"));
        KeyboardRow enterAnswer = new KeyboardRow();
        enterAnswer.add(new KeyboardButton(ENTER_ANSWER));
        KeyboardRow hint = new KeyboardRow();
        hint.add(new KeyboardButton(GET_HINT));
        List<KeyboardRow> keyboard = List.of(mainMenu, enterAnswer, hint);

        ReplyKeyboardMarkup pageKeyboard = new ReplyKeyboardMarkup();
        pageKeyboard.setKeyboard(keyboard);
        pageKeyboard.setSelective(true);
        pageKeyboard.setResizeKeyboard(true);
        pageKeyboard.setOneTimeKeyboard(false);

        return pageKeyboard;
    }
}
