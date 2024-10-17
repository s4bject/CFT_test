# Упрощенная CRM-система

## Описание проекта
Данный проект представляет собой упрощенную CRM-систему для управления информацией о продавцах и их транзакциях. Система поддерживает базовые операции для продавцов и транзакций, а также предоставляет функции аналитики для обработки и анализа данных.

## Основные возможности:
- Управление продавцами: создание, чтение, обновление и удаление данных о продавцах, получение списка всех продавцов.
- Управление транзакциями: создание, чтение, обновление данных о транзакциях, получение списка всех транзакций.
## Аналитика:
- Определение самого продуктивного продавца по сумме транзакций за день, месяц, или год.
- Вывод списка продавцов, сумма всех транзакций которых за указанный период меньше заданного значения.
- Определение наиболее продуктивного периода для каждого продавца, когда было совершено наибольшее количество транзакций.

## Используемые технологии
- Java 21 - Основной язык программирования.
- Spring Boot v3.3.4 - Фреймворк для создания RESTful API.
- PostgreSQL v16.0 - Основная база данных.
- Gradle v8.10.2 - Система управления зависимостями и сборки проекта.
- H2 - in-memory база данных для тестирования.
- JUnit - фреймворк для юнит-тестирования.

## Необходимые зависимости

Проект использует следующие зависимости, которые управляются через Gradle:

- Spring Boot Starter Data JPA (`org.springframework.boot:spring-boot-starter-data-jpa`):
  Используется для работы с базой данных с помощью JPA (Java Persistence API).

- Spring Boot Starter Web (`org.springframework.boot:spring-boot-starter-web`):
  Предоставляет инструменты для создания RESTful API.

- Lombok (`org.projectlombok:lombok`):
  Сокращает объем кода, генерируя геттеры, сеттеры, конструкторы и другие методы с помощью аннотаций.

- Spring Boot DevTools (`org.springframework.boot:spring-boot-devtools`):
  Используется во время разработки для автоматической перезагрузки приложения при изменении кода.

- H2 Database** (`com.h2database:h2`):
  Легковесная база данных, которая работает в режиме in-memory.

- PostgreSQL Driver (`org.postgresql:postgresql`):
  Включает драйвер для подключения к базе данных PostgreSQL.

- Spring Boot Starter Test (`org.springframework.boot:spring-boot-starter-test`):
  Включает набор инструментов для тестирования приложения, в том числе JUnit 5.

- Mockito Core (`org.mockito:mockito-core`) и **Mockito JUnit Jupiter** (`org.mockito:mockito-junit-jupiter`):
  Используются для создания mock-объектов в тестах

- JUnit Platform Launcher (`org.junit.platform:junit-platform-launcher`):
  Предоставляет возможности для запуска JUnit тестов.

- Java Transaction API (`javax.transaction:javax.transaction-api:1.3`):
  Позволяет управлять транзакциями в приложении, что важно для согласованности данных при взаимодействии с базой данных.


## Настройка и запуск проекта
# Создание и настройка конфигурации базы данных
SQL скрипт для созданием базы данных и пользователя для нее
``` sql
CREATE USER myuser WITH PASSWORD 'mypassword';
CREATE DATABASE mydb WITH OWNER myuser;
```
В файле `application.properties` указываем данные для подключения к базе данных.
``` properties
spring.application.name=<Имя приложения>
spring.datasource.url=jdbc:postgresql://localhost:5432/<Название базы данных>
spring.datasource.username=<Имя пользователя>
spring.datasource.password=<Пароль>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
server.port=8081
```
Далее убедитесь что PostgreSQL запущен и настроен. Запустите приложение командой.
``` bash
./gradlew bootRun
```
Для запуска тестов пропишите данную команду.
``` bash
./gradlew test
```
Результаты тестов буду лежать в `build\test-results\test`

## Примеры использования API
- Получение списка продавцов
`GET http://localhost:8081/sellers`

Пример возвращаемых данных
``` json
[
    {
        "id": 4,
        "name": "Artem Artem",
        "contactInfo": "john@example.com",
        "registrationDate": "2024-10-15T12:00:00"
    },
    {
        "id": 1,
        "name": "John 123",
        "contactInfo": "john@example.com",
        "registrationDate": "2024-10-15T12:00:00"
    }
]
```
- Получение продавца по id
`GET http://localhost:8081/sellers/{id продавца}`

Пример возвращаемых данных
``` json
{
  "id": 4,
  "name": "Artem Artem",
  "contactInfo": "john@example.com",
  "registrationDate": "2024-10-15T12:00:00"
}
```
Такой же запрос с методом DElETE удаляет продавца по id.
- Создание продавца
`POST http://localhost:8081/sellers`

И тело запроса
``` json
{
    "name": "Artem Timofeev",
    "contactInfo": "artyon20026@example.com",
    "registrationDate": "2024-10-15T12:00:00"
}
```
Вернет созданного продавца из таблицы в формате `json`

Запрос `PUT http://localhost:8081/sellers/{id продавца}` и с таким же телом запроса, обновит данные продавца.

- Лучший период продавца
Запрос `GET http://localhost:8081/sellers/{id продавца}/best-period` вернет лучший период продавца за каждый срок

Пример данных из ответа
``` json
{
    "bestDay": "2023-10-15",
    "maxDailyTransactions": 2,
    "bestWeekStart": "2023-10-09",
    "bestWeekEnd": "2023-10-15",
    "maxWeeklyTransactions": 3,
    "bestMonthStart": "2023-10-01",
    "bestMonthEnd": "2023-10-31",
    "maxMonthlyTransactions": 3
}
```
- Запросы `GET http://localhost:8081/sellers/top-seller/{period}` и `GET http://localhost:8081/sellers/amount/{amount}/{period}` вернут лучшего продавца за период и список продавцов, у которых сумма всех транзакции за выбранный период
меньше переданного параметра суммы соответственно.

- Получение списка транзакций
`GET http://localhost:8081/transactions`

Пример возвращаемых данных
``` json
[
    {
        "id": 1,
        "seller": {
            "id": 1,
            "name": "John 123",
            "contactInfo": "john@example.com",
            "registrationDate": "2024-10-15T12:00:00"
        },
        "amount": 150.00,
        "paymentType": "CASH",
        "transactionDate": "2023-10-15T14:30:00"
    },
    {
        "id": 2,
        "seller": {
            "id": 1,
            "name": "John 123",
            "contactInfo": "john@example.com",
            "registrationDate": "2024-10-15T12:00:00"
        },
        "amount": 25.00,
        "paymentType": "CASH",
        "transactionDate": "2023-11-15T14:30:00"
    }
]
```
- Получение транзакции по id
`GET http://localhost:8081/transactions/{id транзакции}`

Пример возвращаемых данных
``` json
{
    "id": 1,
    "seller": {
        "id": 1,
        "name": "John 123",
        "contactInfo": "john@example.com",
        "registrationDate": "2024-10-15T12:00:00"
    },
    "amount": 150.00,
    "paymentType": "CASH",
    "transactionDate": "2023-10-15T14:30:00"
}
```

- Получение списка транзакций продавца
`GET http://localhost:8081/transactions/{id продавца}`

Пример возвращаемых данных
``` json
[
    {
        "id": 1,
        "seller": {
            "id": 1,
            "name": "John 123",
            "contactInfo": "john@example.com",
            "registrationDate": "2024-10-15T12:00:00"
        },
        "amount": 150.00,
        "paymentType": "CASH",
        "transactionDate": "2023-10-15T14:30:00"
    },
    {
        "id": 2,
        "seller": {
            "id": 1,
            "name": "John 123",
            "contactInfo": "john@example.com",
            "registrationDate": "2024-10-15T12:00:00"
        },
        "amount": 25.00,
        "paymentType": "CASH",
        "transactionDate": "2023-11-15T14:30:00"
    }
]
```
- Добавление транзакции
`POST http://localhost:8081/transactions`

И тело запроса
``` json
{
  "seller": {
    "id": 4
  },
  "amount": 2400.00,
  "paymentType": "CASH",
  "transactionDate": "2024-07-15T14:30:00"
}

```
Вернет нам добавленную транзакцию из таблицы.
``` json
{
    "id": 16,
    "seller": {
        "id": 4,
        "name": null,
        "contactInfo": null,
        "registrationDate": null
    },
    "amount": 2400.00,
    "paymentType": "CASH",
    "transactionDate": "2024-07-15T14:30:00"
}

```
