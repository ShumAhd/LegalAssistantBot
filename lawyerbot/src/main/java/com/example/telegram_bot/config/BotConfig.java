package com.example.telegram_bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BotConfig {
  @Value("${telegram.bot.username}")
  private String username;

  @Value("${telegram.bot.token}")
  private String token;

  @Value("${telegram.lawyers.chatId}")
  private String lawyersChatId;
}
