# Weather Viewer

## 📌 Description
Weather Viewer — это веб-приложение для просмотра погоды в выбранных пользователем локациях.

Пользователь может искать города, добавлять их на главный экран и удалять.

Данные о погоде получаются через API Open-Meteo.

---

## 🚀 Features

- Регистрация пользователя
- Авторизация (логин/пароль)
- Сессия хранится 2 дня
- Поиск локаций
- Добавление локаций на главный экран
- Удаление локаций
- Защита данных (нельзя удалить чужие локации)
- Отображение текущей погоды

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Thymeleaf
- Hibernate (ORM)
- JDBC
- PostgreSQL
- Flyway (миграции БД)
- Docker
- JUnit + Mockito (тесты)

---

## 🌐 API

Погода берётся из:
- https://open-meteo.com/

---

## ⚙️ How to Run (Local)

1. Запустить Docker (PostgreSQL)
2. Запустить приложение в IntelliJ IDEA
3. Открыть браузер:


---

## 🧪 Tests

В проекте реализованы unit-тесты:

- WeatherService
- Обработка ошибок API
- Проверка JSON parsing
- Проверка бизнес-логики

---

## 📦 Deployment

Приложение готово к деплою на сервер (например, Yandex Cloud).

---

## 👤 Author

Artem Korobenin