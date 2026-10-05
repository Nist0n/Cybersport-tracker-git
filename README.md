# Киберспортивный трекер (Cybersport Tracker)

Android-приложение для просмотра киберспортивных турниров, команд и матчей.
Проект по дисциплине «Разработка мобильных приложений»:

- **Практика №1** — чистая архитектура: use case, слои presentation / domain / data;
- **Практика №2** — модульный проект: модули `domain` и `data`, раздельные модели,
  Firebase Auth, Room, SharedPreferences, NetworkApi (см. раздел 3).

- Пакет приложения: `ru.mirea.pavlovve.cybersporttracker`
- Язык: **Java** (Empty Views Activity), Gradle Kotlin DSL, AGP 9.1.1

---

## 1. Модули проекта (Практика №2, п. 1.2)

| Модуль | Тип | Назначение |
|---|---|---|
| `:app` | Android Application | presentation: Activity, экраны, сборка зависимостей (`AppContainer`) |
| `:domain` | **Java Library** | бизнес-логика: Entities, интерфейсы репозиториев, Use Case. Без Android и внешних библиотек |
| `:data` | **Android Library** | доступ к данным: NetworkApi (JSON), Room, SharedPreferences, Firebase Auth, DTO/мапперы |

Зависимости (Dependency Rule): `:app → :domain`, `:app → :data`, `:data → :domain`.
Domain ни от кого не зависит; модули обеспечивают жёсткое разделение слоёв —
код одного модуля невозможно подключить к другому по ошибке.

```
settings.gradle.kts → include(":app"), include(":domain"), include(":data")
```

## 2. Структура исходников

```
domain/src/main/java/ru/mirea/pavlovve/cybersporttracker/domain/
├── models/        Team, Tournament, Match, User, Prediction   ← Entities
├── repository/    интерфейсы (контракты)
└── usecase/       Login, Register, GetTeams, GetTeamById, GetTournaments,
                   GetMatches, GetUserProfile, AddTeamToFavorites,
                   SubscribeToTournament, MakePrediction, GetPredictionHistory,
                   RecognizeLogo

data/src/main/java/ru/mirea/pavlovve/cybersporttracker/data/
├── network/       NetworkApi — работа с сетью с замоканными JSON-данными (Gson)
├── local/         Room: AppDatabase, entity/ (3 таблицы), dao/ (3 DAO)
├── storage/       независимый блок ClientStorage:
│   ├── model/ClientInfo.java     ← собственная модель блока (не доменная!)
│   ├── ClientStorage.java        ← интерфейс блока (в data, не в domain)
│   ├── SharedPreferencesClientStorage.java
├── auth/          AuthDataSource: FirebaseAuthDataSource (FB Auth),
│                  MockAuthDataSource (тестовые данные), CompositeAuthDataSource
├── dto/           JSON-модели внешнего API
├── mapper/        DTO/Entity/ClientInfo ↔ доменные Entities
├── repository/    8 реализаций (роутят данные между источниками)
└── source/        TfliteLogoDataSource (заглушка модели logo_classifier.tflite)

app/src/main/java/ru/mirea/pavlovve/cybersporttracker/
├── MainActivity.java              ← View
└── presentation/
    ├── LoginActivity.java         ← экран авторизации (Firebase Auth)
    └── AppContainer.java          ← сборка зависимостей (ручной DI)
```

## 3. Контрольное задание практики №2

| № | Требование | Реализация |
|---|---|---|
| 1 | Прототип приложения в Figma | делаете в Figma (экраны — см. раздел 6) |
| 2 | Модули `data` и `domain`, перенос кода | модули `:data` и `:domain`, весь код слоёв перенесён |
| 3 | Новая Activity — авторизация через **Firebase Auth**, логика распределена по трём модулям | `LoginActivity` (app) → `LoginUseCase`/`RegisterUseCase` + `UserRepository` (domain) → `FirebaseAuthDataSource` (data). Вход в фоновом потоке (`ExecutorService` + `Tasks.await`) |
| 4 | В репозитории три способа обработки данных | **SharedPreferences** — `SharedPreferencesClientStorage` (информация о клиенте/сессия); **Room** — `AppDatabase` + 3 DAO (избранное, прогнозы, подписки); **NetworkApi** — `network/NetworkApi.java` с замоканными JSON-данными (Gson → DTO) |

### Раздельные модели данных (Практика №2, п. 1.1)

- **UI-модели** — строки форматируются в `AppContainer.format*()`;
- **domain** — Entity (`Team`, `User`, …) без зависимостей;
- **data** — DTO (`TeamDto`), строки БД (`FavoriteTeamEntity`, `PredictionEntity`,
  `SubscriptionEntity`) и модель блока `storage/model/ClientInfo`;
- конвертация — мапперы (`TeamMapper`, `FavoriteTeamEntityMapper`, `ClientInfoMapper`, …);
- блок `storage` не импортирует классы domain — его можно перенести в другой проект.

### Поток данных (пример: вход)

```
LoginActivity (app)  --фоновый поток-->  LoginUseCase (domain)
      → UserRepository.login() (domain: интерфейс)
         → UserRepositoryImpl (data)
            → AuthDataSource.signIn()  → Firebase Auth | тестовые данные
            → ClientStorage.save(ClientInfo)  → SharedPreferences
      ← User (Entity) → UI
```

## 4. Настройка Firebase (задание 3)

1. https://console.firebase.google.com → **Add project**.
2. В проекте: **Add app → Android**:
   - package name: `ru.mirea.pavlovve.cybersporttracker`;
   - SHA-1 (для отладки): `gradlew signingReport`.
3. Скачать **google-services.json** и положить в каталог `app/`
   (рядом с `build.gradle.kts`).
4. Синхронизировать Gradle — плагин `google-services` подключится автоматически
   (в `app/build.gradle.kts` применение условное: `if (file("google-services.json").exists())`).
5. В Firebase: **Authentication → Sign-in method → Email/Password → Enable**,
   затем **Users → Add user** (например `admin@test.ru` / `123456`).
6. Входить на экране авторизации этим e-mail и паролем.

**До настройки Firebase** проект собирается и работает: `CompositeAuthDataSource`
переключается на `MockAuthDataSource` с тестовыми данными **admin / 1234**
(и `user` / `user`), на экране появляется подсказка.

## 5. Запуск и проверка

1. Android Studio → открыть проект (Gradle JDK = bundled JBR), выполнить Gradle sync.
2. Запуск на устройстве/эмуляторе (API 26+): стартовый экран — **авторизация**,
   после входа открывается `MainActivity`.
3. Сборка: `gradlew assembleDebug` (собираются модули `domain`, `data`, `app`).

> В исходных кодах комментарии намеренно не используются — всё описание
> собрано в этом файле.

## 6. Экраны для прототипа в Figma (задание 1)

1. **Авторизация** — e-mail, пароль, кнопки «Войти» / «Создать аккаунт», статус.
2. **Список команд** — карточки с логотипом, тегом, страной, рейтингом, поиск.
3. **Страница команды** — логотип, состав, рейтинг, кнопка «В избранное».
4. **Список турниров** — название, игра, призовые, дата, кнопка «Подписаться».
5. **Расписание матчей** — пары команд, счёт/время, статус (LIVE/PLAN/FT).
6. **Личный профиль** — аватар, e-mail, избранное, подписки, выход.
7. **Сканер логотипа** — выбор фото, результат распознавания TFLite.
8. **История прогнозов** — список прогнозов, статусы (PENDING/WIN/LOSS).

## 7. Куда расширяться

- `NetworkApi` → реальный Retrofit-клиент (JSON и DTO уже описаны);
- Room → добавить миграции и `exportSchema`, перенести запросы в фоновый поток
  (сейчас `allowMainThreadQueries()` — допустимо только для демо);
- `TfliteLogoDataSource` → реальный `Interpreter` с моделью `.tflite`;
- экраны → Fragments + ViewModel (MVVM) в модуле `app`; DI → Hilt.
