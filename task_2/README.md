# task_2: B+ дерево и модульное тестирование трасс алгоритма

Ключевой класс: `org.example.bplustree.BPlusTree`.

Ограничение задания: максимум ключей в узле = `7`.

## Что реализовано

- Вставка (`insert`)
- Поиск (`contains`)
- Обход ключей в порядке возрастания (`keysInOrder`)
- Трассировка характерных точек вставки (`insertWithTrace`, `insertAllWithTrace`)

## Характерные точки алгоритма

В трассу попадают точки:

- `START_INSERT`
- `ROOT_IS_LEAF`
- `DESCEND_INTERNAL`
- `LEAF_FOUND`
- `LEAF_INSERT`
- `DUPLICATE_KEY`
- `LEAF_OVERFLOW`
- `SPLIT_LEAF`
- `INSERT_IN_PARENT`
- `INTERNAL_OVERFLOW`
- `SPLIT_INTERNAL`
- `NEW_ROOT`
- `END_INSERT`

## Наборы исходных данных и эталон

Тесты в `task_2/src/test/java/org/example/bplustree/BPlusTreeTest.java` сравнивают фактическую трассу с эталонной для сценариев:

1. Набор без разбиений: `[10, 20, 30]`
2. Набор с разбиением листа и созданием нового корня: `[10, 20, 30, 40, 50, 60, 70, 80]`
3. Набор, где при очередной вставке происходит переполнение внутреннего узла, его split и новый корень.
4. Набор с дубликатом ключа.

Запуск:

```bash
cd task_2
./gradlew test
```
