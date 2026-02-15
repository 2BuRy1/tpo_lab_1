# task_3: заготовка

В проекте подготовлена минимальная структура:

- `src/main/java`
- `src/test/java`

Добавьте сюда алгоритм и модульные тесты для третьего задания.

Запуск:

```bash
cd task_3
./gradlew test
./gradlew run
```

Примечание: в `build.gradle` используется Java toolchain 17.  
Если на машине основной JDK новее и Gradle не стартует, выполните запуск так:

```bash
cd task_3
JAVA_HOME=$(/usr/libexec/java_home -v 17) ./gradlew test
```
