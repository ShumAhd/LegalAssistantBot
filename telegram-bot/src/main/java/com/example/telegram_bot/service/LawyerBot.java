package com.example.telegram_bot.service;

import com.example.telegram_bot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LawyerBot extends TelegramLongPollingBot {

  private final BotConfig botConfig;

  @Autowired
  public LawyerBot(BotConfig botConfig) {
    this.botConfig = botConfig;
  }

  @Override
  public String getBotUsername() {
    return botConfig.getBotUsername();
  }

  @Override
  public String getBotToken() {
    return botConfig.getBotToken();
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      Message message = update.getMessage();
      if (message.hasText()) {
        handleIncomingMessage(message);
      } else if (message.hasContact()) {
        handleIncomingContact(message);
      }
    }
  }

  private void handleIncomingMessage(Message message) {
    String chatId = message.getChatId().toString();
    String receivedText = message.getText();

    SendMessage response = new SendMessage();
    response.setChatId(chatId);

    if ("/start".equals(receivedText)) {
      response.setText("Привет! Я ваш юридический бот. Как я могу помочь вам сегодня?\n\nПожалуйста, задайте свой вопрос текстом.");
    } else if (receivedText.trim().isEmpty()) {
      response.setText("Ошибка: Ваш вопрос не содержит текста. Пожалуйста, напишите свой вопрос текстом.");
    } else {
      response.setText("Спасибо за ваш вопрос. Пожалуйста, предоставьте ваш номер телефона для связи.\n\n" +
          "Поделитесь вашим контактом, нажав на кнопку ниже.");
      response.setReplyMarkup(getContactKeyboard());
    }

    try {
      execute(response);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private void handleIncomingContact(Message message) {
    String chatId = message.getChatId().toString();
    String contact = message.getContact().getPhoneNumber();
    String userName = message.getFrom().getUserName();
    String telegramId = message.getFrom().getId().toString();
    String question = message.getText();

    SendMessage response = new SendMessage();
    response.setChatId(chatId);
    response.setText("Спасибо за предоставленный номер телефона. Ваш вопрос будет рассмотрен, и юристы свяжутся с вами.");

    // Отправка сообщения в группу юристов
    SendMessage lawyersMessage = new SendMessage();
    lawyersMessage.setChatId(botConfig.getLawyersChatId());
    lawyersMessage.setText("Новый вопрос от пользователя @" + userName + ":\n" +
        "Номер телефона: " + contact + "\n" +
        "Телеграм ID: " + telegramId + "\n" +
        "Вопрос: " + question);

    try {
      execute(response);
      execute(lawyersMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private ReplyKeyboardMarkup getContactKeyboard() {
    KeyboardButton contactButton = new KeyboardButton("Поделиться контактом");
    contactButton.setRequestContact(true);

    KeyboardRow row = new KeyboardRow();
    row.add(contactButton);

    List<KeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(row);

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setKeyboard(keyboard);
    replyKeyboardMarkup.setOneTimeKeyboard(true);
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }
}