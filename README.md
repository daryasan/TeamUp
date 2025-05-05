## Сервис users
(Postman-коллекция)[https://.postman.co/workspace/Ordering~ac545bba-63ec-4c3e-a8a8-87192465c0db/collection/36027467-89bbc04e-6fa0-45fb-847e-92ce01a387ac?action=share&creator=36027467] 
# КАК ЗАПУСТИТЬ
## Сервис auth
1. Настройте БД:
   Скачайте pgAdmin
   Откройте и создайте пустую БД с названием teamup_users.
   Откройте меню только что созданной БД (правая кнопка мыши) и нажмите restore:
   Выберите файл teamup_auth.sql и подтвердите.
   Оставьте pdAdmin подключенным.
2. Добавьте файл application.yml по пути users/src/main/resources/application.yml
   Содержание файла можно спросить у меня в лс потому что гит на дает пушить приватный ключ сюда
3. Запустите микросервис из IntelijIdea
