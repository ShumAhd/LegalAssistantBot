package com.example.telegram_bot;

import com.example.telegram_bot.bot.LawyerBot;
import com.example.telegram_bot.config.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class LawyerBotApplication {
	public static void main(String[] args) {
		SpringApplication.run(LawyerBotApplication.class, args);
	}

	@Bean
	public LawyerBot lawyerBot(BotConfig botConfig) {
		TelegramBotsApi botsApi;
		try {
			botsApi = new TelegramBotsApi(DefaultBotSession.class);
			LawyerBot bot = new LawyerBot(botConfig);
			botsApi.registerBot(bot);
			return bot;
		} catch (TelegramApiException e) {
			e.printStackTrace();
			return null;
		}
	}
}
