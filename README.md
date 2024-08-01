# LawyerBot

LawyerBot - это Telegram-бот, созданный для помощи пользователям в задавании юридических вопросов и получения ответов от юристов. Бот собирает вопросы пользователей и контактную информацию, а затем пересылает их в указанную группу юристов.

## Как работает бот

1. **Приветственное сообщение**: Когда пользователь начинает общение с ботом с помощью команды `/start`, бот отправляет приветственное сообщение с инструкциями.
2. **Отправка вопроса**: Пользователь отправляет свой вопрос. Если вопрос пустой, бот просит пользователя ввести текстовый вопрос.
3. **Контактная информация**: Бот запрашивает номер телефона пользователя для дальнейшей связи. Пользователь может поделиться своим контактом, нажав на кнопку "Поделиться контактом".
4. **Пересылка юристам**: Бот пересылает вопрос пользователя и контактную информацию в указанную группу Telegram юристов.
5. **Подтверждение**: Пользователь получает подтверждение, что его вопрос получен и с ним свяжутся юристы.
6. **Новый вопрос**: Пользователю предоставляется возможность задать новый вопрос.

## Примеры сообщений

Приветственное сообщение:

```
Привет! Я ваш юридический бот. Как я могу помочь вам сегодня?
Пожалуйста, задайте свой вопрос текстом.
```

Ошибка пустого вопроса:

```
Ошибка: Ваш вопрос не содержит текста. Пожалуйста, напишите свой вопрос текстом.
```
Запрос контактной информации:
```
Спасибо за ваш вопрос. Пожалуйста, предоставьте ваш номер телефона для связи.
Поделитесь вашим контактом, нажав на кнопку ниже.
```
Подтверждение:
```
Спасибо за предоставленный номер телефона. Ваш вопрос будет рассмотрен, и юристы свяжутся с вами.
```
Сообщение юристам:
```
Новый вопрос от пользователя @username:
Номер телефона: +123456789
Телеграм ID: 123456789
Вопрос: Как оформить наследство?
```

## Запуск
Добавьте файл конфигурации application.properties в директорию src/main/resources/ с содержимым:
```
telegram.bot.username=YOUR_BOT_USERNAME
telegram.bot.token=YOUR_BOT_API_TOKEN
telegram.lawyers.chatId=YOUR_LAWYERS_GROUP_CHAT_ID
```
