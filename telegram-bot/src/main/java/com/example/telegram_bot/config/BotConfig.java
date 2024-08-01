package com.example.telegram_bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

  @Value("${telegram.bot.username}")
  private String botUsername;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Value("${telegram.lawyers.chatId}")
  private String lawyersChatId;

  public String getBotUsername() {
    return botUsername;
  }

  public String getBotToken() {
    return botToken;
  }

  public String getLawyersChatId() {
    return lawyersChatId;
  }
}
