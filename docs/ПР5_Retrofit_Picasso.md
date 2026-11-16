# Практическая работа №5. Retrofit и Picasso

## 1. Цель

Заменить заглушку сетевого слоя в приложении **Cybersport Tracker** на реальные
HTTP-запросы через **Retrofit**, научиться обрабатывать сетевые ошибки и
загружать изображения по URL через **Picasso**. Отдельно, вне проекта трекера,
сделать приложение **RetrofitApp** с задачами JSONPlaceholder (GET + PATCH) и
галереей изображений с настраиваемыми параметрами отображения.

## 2. REST, HTTP и JSON — минимум теории

REST — стиль построения API, где каждая сущность доступна по URL, а действие
задаётся HTTP-методом:

| Метод  | Назначение                    | Пример в работе                          |
|--------|-------------------------------|------------------------------------------|
| GET    | получить данные               | `GET /todos`, `GET getTeams`             |
| POST   | создать ресурс                | (не используется)                        |
| PATCH  | частично изменить ресурс      | `PATCH /todos/1` c `{"completed": true}` |
| DELETE | удалить ресурс                | (не используется)                        |

Сервер отвечает JSON-документом и кодом состояния: `200 OK` — успех,
`4xx` — ошибка запроса, `5xx` — ошибка сервера. В Android сеть нельзя трогать
в главном потоке, поэтому запросы выполняются асинхронно.

## 3. Retrofit: как устроен сетевой слой

### 3.1 Зависимости

Версии вынесены в Version Catalog `gradle/libs.versions.toml`:

```toml
retrofit = "2.11.0"
okhttp = "4.12.0"
picasso = "2.8"

retrofit      = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
converter-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
picasso       = { group = "com.squareup.picasso", name = "picasso", version.ref = "picasso" }
```

Подключены в `data/build.gradle.kts` (retrofit, converter-gson, okhttp-logging)
и `app/build.gradle.kts` (picasso).

### 3.2 Описание API интерфейсом

Retrofit превращает интерфейс с аннотациями в реализацию HTTP-клиента.
В трекере используется официальное API LoL Esports
(`https://esports-api.lolesports.com/persisted/gw/`):

```java
public interface LolEsportsApi {

    @GET("getTeams")
    Call<LolTeamsResponse> getTeams(@Query("hl") String language);

    @GET("getLeagues")
    Call<LolLeaguesResponse> getLeagues(@Query("hl") String language);

    @GET("getSchedule")
    Call<LolScheduleResponse> getSchedule(@Query("hl") String language);
}
```

Разметка аннотаций:

* `@GET("getTeams")` — метод и относительный путь; полный URL собирается из
  `baseUrl`;
* `@Query("hl")` — параметр строки запроса (`?hl=en-US`);
* `@Path("id")` — подстановка в `{id}` внутри пути;
* `@Body` — тело запроса, сериализуется конвертером (в RetrofitApp это
  `UpdateTodoBody`);
* `@Header` / `Interceptor` — заголовки (ключ API у LoL Esports передаётся
  общим перехватчиком).

В учебном приложении интерфейс проще:

```java
public interface TodoApi {
    @GET("todos")
    Call<List<TodoDto>> getTodos();

    @PATCH("todos/{id}")
    Call<TodoDto> updateTodo(@Path("id") int id, @Body UpdateTodoBody body);
}
```

### 3.3 Сборка клиента

`data/network/ApiClient.java` (трекер) и
`practical5/RetrofitApp/.../data/ApiClient.java` (практика) собирают клиента по
одной схеме — синглтон:

```java
OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                .header("x-api-key", ApiConfig.API_KEY)
                .header("Accept", "application/json")
                .build()))
        .addInterceptor(new HttpLoggingInterceptor().setLevel(Level.BASIC))
        .build();

Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
```

* **OkHttp** — транспорт: соединения, таймауты, перехватчики, логирование;
* **Retrofit** — разбор аннотаций и вызовы;
* **GsonConverterFactory** — JSON ↔ Java-объекты.

`HttpLoggingInterceptor` на уровне `BASIC` печатает метод, URL, код и время
ответа — этим удобно проверять запросы в `adb logcat`.

### 3.4 Синхронные и асинхронные вызовы

* `call.execute()` — блокирующий вызов. В трекере он выполняется внутри уже
  существующих фоновых `Executor` репозиториев, чтобы не менять архитектуру;
* `call.enqueue(callback)` — асинхронный вызов, колбэк приходит в главный поток.
  Используется в `RetrofitApp` (`MainActivity.loadTodos`,
  `MainActivity.onCompletedChanged`).

```java
api.getTodos().enqueue(new Callback<List<TodoDto>>() {
    @Override
    public void onResponse(Call<List<TodoDto>> call, Response<List<TodoDto>> response) {
        if (response.isSuccessful() && response.body() != null) {
            adapter.submitList(response.body());
        } else {
            showError("Сервер вернул код " + response.code());
        }
    }

    @Override
    public void onFailure(Call<List<TodoDto>> call, Throwable t) {
        showError(ApiClient.describe(t));
    }
});
```

## 4. DTO и маппинг в доменные модели

JSON не совпадает с моделями приложения, поэтому сетевой слой разделён на
**DTO** и **domain**-модели:

* DTO — `data/dto/LolTeamDto.java`, `LolLeagueDto.java`, `LolMatchDto.java`,
  `LolScheduleResponse.java` и т. д. Поля повторяют JSON, Gson заполняет их по
  имени;
* мапперы — `TeamMapper`, `MatchMapper`, `TournamentMapper` превращают DTO в
  `Team`, `Match`, `Tournament` из модуля `domain`;
* мапперы нормализуют данные: `http://` → `https://` для логотипов, отбрасывают
  команды без имени, формируют стабильные числовые `id` из строковых.

Пример:

```java
public Team map(LolTeamDto dto) {
    return new Team(
            stableId(dto.getId()),
            dto.getName(),
            dto.getCode(),
            dto.getHomeLeague() != null ? dto.getHomeLeague().getName() : "",
            normalizeUrl(dto.getImage()),
            regionOf(dto),
            0,
            playersOf(dto));
}
```

В практическом приложении DTO один — `TodoDto` (`userId`, `id`, `title`,
`completed`), маппинг не нужен: поля совпадают с JSON.

## 5. Обработка ошибок

Сетевые ошибки делятся на три группы:

1. **Транспортные** — нет сети, таймаут, обрыв (`IOException`,
   `UnknownHostException`, `SocketTimeoutException`). В трекере они
   заворачиваются в `NetworkException` с понятным текстом; в RetrofitApp —
   метод `ApiClient.describe(Throwable)`.
2. **HTTP-ошибки** — сервер ответил, но код не `2xx` (`response.isSuccessful()`
   == false). Показываем код.
3. **Ошибки разбора** — неожиданный JSON; Gson бросит исключение, его ловит
   слой репозитория.

UI реагирует на ошибку состоянием:

* список команд — экран ошибки «Нет соединения с сервером» и кнопка
  **«Повторить»** (`TeamsViewModel`/`TeamsUiState`);
* карточка команды — `Snackbar` с действием повтора
  (`TeamDetailsViewModel`);
* в RetrofitApp — блок ошибки вместо списка, кнопка «Повторить», а при
  неудачном PATCH CheckBox возвращается в исходное положение и появляется
  `Snackbar`.

Также обрабатывается «пустой» успешный ответ: `response.body() == null`
считается ошибкой, чтобы не показывать пустой экран без объяснения.

## 6. Кэширование

* **Retrofit-уровень.** `data/network/NetworkApi.java` держит загруженные
  команды, лиги и расписание в памяти. Список команд — около 1,5 МБ и 1587
  записей, поэтому повторные открытия экранов не вызывают повторную загрузку.
* **Picasso.** Хранит изображения в памяти и на диске, повторный показ логотипа
  не требует сети.
* **OkHttp** может кэшировать ответы по HTTP-заголовкам; в проекте кэш диска не
  включён, потому что данные API меняются, а объём небольшой.

## 7. Picasso

Picasso загружает изображение по URL асинхронно, кэширует его и сам отменяет
запрос при переиспользовании `ImageView` в RecyclerView.

Логотип команды (`TeamsAdapter.bindLogo`):

```java
Picasso.get()
        .load(team.getLogoUrl())
        .placeholder(R.drawable.logo_team_placeholder)
        .error(R.drawable.logo_team_placeholder)
        .resize(96, 96)
        .onlyScaleDown()
        .centerInside()
        .into(logoView);
```

Ключевые методы:

| Метод             | Что делает                                         |
|-------------------|----------------------------------------------------|
| `load(url)`       | источник: URL, файл, ресурс                        |
| `placeholder(res)`| картинка до загрузки                               |
| `error(res)`      | картинка при ошибке                                |
| `resize(w, h)`    | декодирование сразу в нужном размере (экономия RAM)|
| `centerCrop()`    | заполнить область с обрезкой краёв                 |
| `centerInside()`  | вписать целиком без обрезки                        |
| `rotate(deg)`     | поворот результата                                 |
| `cancelRequest(v)`| отменить загрузку для конкретного `ImageView`      |

Если URL пустой, запрос не создаётся — сразу ставится заглушка. Битые ссылки
(в API встречаются логотипы на отключённом CDN) закрываются `error(...)`.

## 8. Отдельное приложение RetrofitApp

Расположено в `practical5/RetrofitApp` и не входит в сборку трекера
(`settings.gradle.kts` трекера его не подключает). Это самостоятельный
Gradle-проект.

Что реализовано:

* `GET https://jsonplaceholder.typicode.com/todos` — 200 задач в RecyclerView
  (`TodosAdapter`, `ListAdapter` + `DiffUtil`);
* `PATCH /todos/{id}` при переключении CheckBox — отправляется только
  `{"completed": true|false}`, пока идёт запрос CheckBox заблокирован,
  при ошибке состояние откатывается;
* индикатор загрузки, экран ошибки с кнопкой «Повторить», `Snackbar` с
  результатом;
* горизонтальная галерея из 8 изображений `https://picsum.photos/seed/.../300/300`
  через Picasso;
* настройка отображения: размер (SeekBar 70–190 dp), обрезка по центру
  (`centerCrop`/`centerInside`), поворот (0–345° с шагом 15°), кнопка загрузки
  новых изображений;
* `HttpLoggingInterceptor` для проверки запросов в logcat.

## 9. Архитектура проекта трекера

Сетевой слой занял место заглушки, не меняя остальных слоёв:

```
UI (Activity/Adapter)
   ↑ LiveData
ViewModel (TeamsViewModel, TeamDetailsViewModel)
   ↑ use cases
domain (TeamRepository, GetTeamsUseCase, модели Team/Match/Tournament)
   ↑ реализация интерфейсов
data (TeamRepositoryImpl → NetworkApi → Retrofit → LolEsportsApi)
```

* `NetworkApi` — фасад: кэш, вызовы Retrofit, преобразование ошибок;
* репозитории вызывают его в фоновом executor и отдают доменные модели;
* ViewModel формирует `UiState` (`Loading`, `Content`, `Error`);
* Activity/Adapter только отображают состояние и вызывают действия
  (повтор, открытие карточки, избранное).

Авторизация осталась учебной: `MockUserStore` (`admin@test.ru` / `123456`)
больше не связан с сетевым слоем.

## 10. Запуск и проверка

Сборка трекера:

```powershell
$env:JAVA_HOME="D:\Android studio\jbr"
cd D:\Cybersport-tracker-git\Cybersport-tracker-git
.\gradlew.bat :app:assembleDebug
```

Сборка практики:

```powershell
$env:JAVA_HOME="D:\Android studio\jbr"
cd D:\Cybersport-tracker-git\Cybersport-tracker-git\practical5\RetrofitApp
.\gradlew.bat :app:assembleDebug
```

Установка и запуск:

```powershell
adb install -r app\build\outputs\apk\debug\app-debug.apk
adb shell am start -n ru.mirea.pavlovve.cybersporttracker/.presentation.LoginActivity
adb shell am start -n ru.mirea.pavlovve.retrofitapp/.MainActivity
```

Проверка запросов:

```powershell
adb logcat | findstr okhttp
```

Ожидаемые строки: `--> GET ...`, `<-- 200 ...`, `--> PATCH ...`.

Проверка ошибок: отключить Wi-Fi/мобильные данные, перезапустить приложение —
появится экран «Нет соединения с сервером»; включить сеть и нажать
«Повторить» — данные загрузятся снова.

## 11. Результаты

* Список команд загружается с реального API: 1587 команд, логотипы, регионы и
  лиги.
* Карточка команды показывает состав и матчи из сети, логотип грузится Picasso.
* Ошибки сети не приводят к падению: показывается состояние с повтором.
* В RetrofitApp задачи загружаются и сохраняются PATCH-запросом, изображения
  отображаются с настраиваемым размером, обрезкой и поворотом.

Скриншоты:

![Список команд](screens/p5_teams_list.png)

![Карточка команды](screens/p5_team_details.png)

![Экран ошибки сети](screens/p5_network_error.png)

![RetrofitApp](screens/p5_retrofit_app.png)

## 12. Изменённые файлы

| Файл | Назначение |
|------|------------|
| `gradle/libs.versions.toml`, `data/build.gradle.kts`, `app/build.gradle.kts` | зависимости Retrofit/OkHttp/Picasso |
| `data/.../network/ApiClient.java`, `ApiConfig.java`, `LolEsportsApi.java` | Retrofit-клиент и описание API |
| `data/.../network/NetworkApi.java`, `NetworkException.java` | фасад, кэш, ошибки |
| `data/.../dto/Lol*.java` | DTO ответов API |
| `data/.../mapper/*.java` | DTO → domain |
| `data/.../repository/*.java` | доступ к данным |
| `app/.../teams/*`, `app/.../teamdetails/*` | состояния ошибок, Picasso, повторы |
| `app/.../common/RegionNames.java` | локализация регионов |
| `practical5/RetrofitApp/**` | отдельное приложение практики |
