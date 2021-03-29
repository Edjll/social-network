# social-network

Command to build backend: `mvn clean install`

Command to build docker images: `docker-compose build`

Command to start docker containers: `docker-compose up`

## backend endpoints

<table>
  <tr>
    <th>URL</th>
    <th>Описание</th>
  </tr>
  <tr>
    <td><a href="http://localhost:8085/docs">http://localhost:8085/docs</a></td>
    <td>api-docs</td>
  </tr>
  <tr>
    <td><a href=http://localhost:8085/docs/ui">http://localhost:8085/docs/ui</a></td>
    <td>swagger-ui</td>
  </tr>
</table>

## frontend endpoints

<table>
  <tr>
    <th>URL</th>
    <th>Описание</th>
  </tr>
  <tr>
    <td><a href="http://localhost:3000">http://localhost:3000</a></td>
    <td>Домашная страница</td>
  </tr>
  <tr>
    <td><a href="http://localhost:3000/search">http://localhost:3000/search</a></td>
    <td>Страница поиска пользователей</td>
  </tr>
  <tr>
    <td><a href="http://localhost:3000/user/admin">http://localhost:3000/user/{username}</a></td>
    <td>Профиль пользователя</td>
  </tr>
  <tr>
    <td><a href="http://localhost:3000/user/edit">http://localhost:3000/user/edit</a></td>
    <td>Редактирование профиля пользователя</td>
  </tr>
  <tr>
    <td><a href="http://localhost:3000/user/admin/message">http://localhost:3000/user/{username}/message</a></td>
    <td>Страница диалога авторизованного пользователя с пользователем {username}</td>
  </tr>
</table>

## График выполнения

<table>
  <tr>
    <th>#</th>
    <th>Функционал</th>
    <th>Дата старта</th>
    <th>Дата окончания</th>
    <th>Выполнена</th>
  </tr>
  <tr>
    <td>1</td>
    <td>Авторизация / аутентификация / регистрация</td>
    <td>15.03</td>
    <td>21.03</td>
    <td></td>
  </tr>
  <tr>
    <td>2</td>
    <td>Поиск других пользователей</td>
    <td>22.03</td>
    <td>24.03</td>
    <td></td>
  </tr>
  <tr>
    <td>3</td>
    <td>Обмен сообщениями</td>
    <td>25.03</td>
    <td>30.03</td>
    <td></td>
  </tr>
  <tr>
    <td>4</td>
    <td>Создание / редактирование / удаление постов</td>
    <td>31.03</td>
    <td>04.04</td>
    <td></td>
  </tr>
  <tr>
    <td>5</td>
    <td>Страница пользователя</td>
    <td>05.04</td>
    <td>08.04</td>
    <td></td>
  </tr>
  <tr>
    <td>6</td>
    <td>Админка</td>
    <td>08.04</td>
    <td>15.04</td>
    <td></td>
  </tr>
  <tr>
    <td>7</td>
    <td>Редактирование информации о пользователе</td>
    <td>15.04</td>
    <td>20.04</td>
    <td></td>
  </tr>
  <tr>
    <td>8</td>
    <td>Добавление в друзья / подписывание</td>
    <td>21.04</td>
    <td>26.04</td>
    <td></td>
  </tr>
  <tr>
    <td>9</td>
    <td>Создание / редактирование / удаление группы</td>
    <td>27.04</td>
    <td>01.05</td>
    <td></td>
  </tr>
  <tr>
    <td>10</td>
    <td>Подписка на группы</td>
    <td>02.05</td>
    <td>06.05</td>
    <td></td>
  </tr>
  <tr>
    <td>11</td>
    <td>Лента постов друзей</td>
    <td>06.05</td>
    <td>10.05</td>
    <td></td>
  </tr>
</table>
