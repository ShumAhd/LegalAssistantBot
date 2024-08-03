package com.example.telegram_bot.bot;

import com.example.telegram_bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

@Slf4j
@Component
public class LawyerBot extends TelegramLongPollingBot {

  private final BotConfig botConfig;

  @Autowired
  public LawyerBot(BotConfig botConfig) {
    this.botConfig = botConfig;
  }

  @Override
  public String getBotUsername() {
    return botConfig.getUsername();
  }

  @Override
  public String getBotToken() {
    return botConfig.getToken();
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String messageText = update.getMessage().getText();
      long chatId = update.getMessage().getChatId();

      switch (messageText) {
        case "/start":
          startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
          break;
        case "Отправить вопрос":
          requestContact(chatId);
          break;
        case "Отправить новый вопрос":
          sendMessage(chatId, "Пожалуйста, напишите ваш вопрос.");
          break;
        default:
          sendQuestionToLawyers(update.getMessage());
          break;
      }
    } else if (update.hasMessage() && update.getMessage().hasContact()) {
      long chatId = update.getMessage().getChatId();
      String phoneNumber = update.getMessage().getContact().getPhoneNumber();
      sendMessage(chatId, "Спасибо! Юристы свяжутся с вами по номеру: " + phoneNumber);
      sendMessageToLawyers(chatId, phoneNumber);
    }
  }

  private void startCommandReceived(long chatId, String name) {
    String welcomeMessage = "Привет, " + name + "! Добро пожаловать в бот юристов. Пожалуйста, напишите ваш вопрос.";
    sendMessage(chatId, welcomeMessage);
  }

  private void requestContact(long chatId) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Пожалуйста, предоставьте свой номер телефона.");

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow row = new KeyboardRow();
    KeyboardButton button = new KeyboardButton("Поделиться контактом");
    button.setRequestContact(true);
    row.add(button);
    keyboard.add(row);
    keyboardMarkup.setKeyboard(keyboard);

    message.setReplyMarkup(keyboardMarkup);

    try {
      execute(message);
    } catch (TelegramApiException e) {
      log.error("Error occurred: " + e.getMessage());
    }
  }

  private void sendQuestionToLawyers(Message message) {
    String questionText = message.getText();
    long chatId = message.getChatId();
    if (questionText.isEmpty()) {
      sendMessage(chatId, "Пожалуйста, введите текст вашего вопроса.");
      return;
    }

    // Process and send the question to the lawyers' chat
    long lawyersChatId = Long.parseLong(botConfig.getLawyersChatId());
    String userInfo = "Вопрос от: " + message.getChat().getFirstName() + "\n\n" + questionText;

    sendMessage(lawyersChatId, userInfo);
    sendMessage(chatId, "Ваш вопрос отправлен юристам. Они свяжутся с вами в ближайшее время.");
  }

  private void sendMessageToLawyers(long chatId, String phoneNumber) {
    long lawyersChatId = Long.parseLong(botConfig.getLawyersChatId());
    String userInfo = "Номер телефона: " + phoneNumber;

    sendMessage(lawyersChatId, userInfo);
  }

  private void sendMessage(long chatId, String text) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(text);

    try {
      execute(message);
    } catch (TelegramApiException e) {
      log.error("Error occurred: " + e.getMessage());
    }
  }
}
