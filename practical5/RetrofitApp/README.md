# RetrofitApp — практическая работа №5

Отдельное приложение (не входит в сборку Cybersport Tracker).

## Что показывает

* **GET** `https://jsonplaceholder.typicode.com/todos` — 200 задач в RecyclerView.
* **PATCH** `/todos/{id}` — сохранение состояния CheckBox, откат при ошибке.
* Экран ошибки с кнопкой «Повторить», `Snackbar` с результатом запроса.
* **Picasso** — галерея изображений `picsum.photos` с настройкой размера,
  обрезки (`centerCrop`/`centerInside`) и поворота.

## Сборка и запуск

```powershell
$env:JAVA_HOME="D:\Android studio\jbr"
cd practical5\RetrofitApp
.\gradlew.bat :app:assembleDebug
adb install -r app\build\outputs\apk\debug\app-debug.apk
adb shell am start -n ru.mirea.pavlovve.retrofitapp/.MainActivity
```

## Проверка запросов

```powershell
adb logcat | findstr okhttp
```

В логе будут строки `--> GET ...`, `<-- 200 ...` и `--> PATCH ...`.
