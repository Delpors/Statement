# Statement Application

Spring Boot приложение для управления расчетно-платежными ведомостями.

## 📋 Требования

- Java 21
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL 15 (автоматически поднимается в Docker)

## 🚀 Быстрый старт

### Запуск с Docker (рекомендуемый способ)

1. Клонируйте репозиторий:
```bash
git clone https://github.com/Delpors/Statement.git
```
2. Соберите приложение:
```bash
mvn clean package
```
3. Запустите с Docker Compose:
```bash
docker-compose up --build
```
4. Приложение будет доступно по адресу: http://localhost:8080

