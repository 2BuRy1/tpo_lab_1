# task_3

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

## Отчет по тестам

### `SpaceEjectionSceneTest`
- `narrativeSceneStartsBeforeEvents`  
  Проверяет корректную инициализацию сцены из `fromNarrative()`: начальные состояния двигателя, воздуха, космоса, стиль и состав экипажа.
- `playOutCallsSceneModulesInOrder`  
  Проверяет оркестрацию (`mock` + `verify`): в `playOut()` вызываются `engine.buzz()`, `airFlow.intensifyToRoar()`, `ejectionEvent.execute()` в нужном порядке.
- `playOutAsNarrativeTextUsesMockedCrewNamesAndTriggersModules`  
  Проверяет, что `playOutAsNarrativeText()` формирует ожидаемый текст и вызывает нужные модули; имена экипажа задаются через моки.
- `constructorRejectsNullAggregateDependencies`  
  Проверяет валидацию конструктора `SpaceEjectionScene`: при `null` в любой зависимости выбрасывается `IllegalArgumentException`.

### `EjectionEventTest`
- `executeMovesAllCrewMembersIntoOpenSpace`  
  Проверяет, что `execute()` переводит всех членов экипажа в `OPEN_SPACE`.
- `getCrewReturnsImmutableSnapshot`  
  Проверяет, что список экипажа нельзя изменить извне (`UnsupportedOperationException`) и состояние события не ломается.
- `constructorValidatesInput`  
  Проверяет негативные сценарии конструктора `EjectionEvent`: `null`/пустой список экипажа, `null` внутри списка, `null` destination, `null` style.

### `DomainValidationTest`
- `crewMemberStartsInsideCraftAndValidatesName`  
  Проверяет, что новый `CrewMember` стартует в `INSIDE_CRAFT`, а некорректные имена (`null`/blank) запрещены.
- `starFieldValidatesValues`  
  Проверяет валидное создание `StarField` и валидацию параметров: число точек должно быть > 0, яркость не должна быть `null`.
- `outerSpaceRequiresStarField`  
  Проверяет запрет создания `OuterSpace` без `StarField`.
