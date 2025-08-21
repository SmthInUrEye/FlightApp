# Flight Management Service

**Flight Management Service** — это Spring Boot приложение для управления перелётами.  
Позволяет создавать, обновлять, удалять и фильтровать перелёты через REST API

---

## Основные возможности

- CRUD операции с перелётами (создание, чтение, обновление, удаление)
- Фильтрация перелётов по разным критериям (перелёты с длительным временем на земле, перелёты с датой отправления в прошлом и т.д.)
- Автоматическая миграция базы данных с помощью Liquibase
- Поддержка PostgreSQL

---

## Технологии

- Java 17
- Spring Boot
- Spring Web (REST API)
- Spring Data JPA
- Liquibase (миграции БД)
- PostgreSQL
- Maven

---

## Запуск проекта

1. Клонировать репозиторий:
    ```bash
    git clone https://github.com/SmthInUrEye/flightApp.git
    cd flightApp
    ```

2. Настроить подключение к базе данных в `application.properties` или `application.yml`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/flightdb
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

3. Запустить приложение:
    ```bash
    mvn spring-boot:run
    ```

4. Приложение автоматически применит миграции Liquibase и будет готово к работе.

---

## REST API

Пример основных эндпоинтов:

| Метод  | URL                   | Описание                    |
|--------|-----------------------|-----------------------------|
| GET    | `/api/flights/{id}`   | Получить перелёт по ID      |
| POST   | `/api/flights`        | Создать новый перелёт       |
| PUT    | `/api/flights/{id}`   | Обновить существующий перелёт |
| DELETE | `/api/flights/{id}`   | Удалить перелёт             |
| GET    | `/api/flights`        | Получить все перелёты       |

---

### Формат даты и времени

В API используется формат даты и времени:

**Пример:**

```json
{
  "segments": [
    {
      "departureDate": "21.08.2025 09:10",
      "arrivalDate": "21.08.2025 11:10"
    }
  ]
}
```
### Реализована поддержка Swagger UI для наглядного тестирования API 
<img width="1151" height="710" alt="image" src="https://github.com/user-attachments/assets/823e8ae3-613d-46df-88b4-d5187c1b03ca" />

