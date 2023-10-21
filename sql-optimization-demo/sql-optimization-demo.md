## Что такое индексы

- Средство увеличения производительности БД

- Минусы
  
  - Замедление записи в таблицы (Реляционные базы 80% чтения к 20% записи) [What databases should be used in applications which are read heavy, write heavy, and both? - Quora](https://www.quora.com/What-databases-should-be-used-in-applications-which-are-read-heavy-write-heavy-and-both)
  
  - Дополнительные объемы дискового пространств (В среднем индексы занимают половину размера таблицы)
  
  - Индексы имеют свойство распухать, особенно на часто изменяемых данных
    
    - Изменение данных не изменяет строчку - новая версия строки, если устаревшие версии не нужны, то vacuum чистит место и туда прилетают новые данные. Таблицы - хорошо, а в индексах остаются пустые места (bloat). Периодически индексы приходится пересоздавать
  
  - Не все индексы полезны

```sql
SELECT  
 datname,  
 tup_fetched AS reads,  
 tup_inserted + tup_updated + tup_deleted AS writes,  
 (ROUND((tup_inserted + tup_updated + tup_deleted)::numeric / NULLIF(tup_fetched, 0), 5)) * 100 AS ratio  
FROM pg_stat_database;
```

## Где хранятся данные индексов

[PostgreSQL : Документация: 9.6: 11.11. Сканирование только индекса : Компания Postgres Professional](https://postgrespro.ru/docs/postgresql/9.6/indexes-index-only-scans)

Все индексы в PostgreSQL являются *вторичными*, что значит, что каждый индекс хранится вне области основных данных таблицы (которая в терминологии PostgreSQL называется *кучей* таблицы)

В PostgreSQL данные хранятся в файлах, которые называются "relation files" или просто "relfiles". Каждая таблица (и её индексы) хранятся в отдельном файле

```sql
/your_data_directory/base/your_database_oid/
    - 1234 (relation file for table with OID 1234)
        - 1234.dat (actual data)
        - 1234_fsm (free space map)
        - 1234_vm (visibility map)
        - 1234_idx1.idx (index file for the first index on table OID 1234)
        - 1234_idx2.idx (index file for the second index on table OID 1234)
        - ...
    - ...
```

 **1. Relation Files (relfiles):**

- Эти файлы содержат данные таблицы
- Они обычно имеют расширение ".dat"
- Каждый relfile представляет собой набор блоков данных, размер которых зависит от конфигурации вашей базы данных

**2. Free Space Map (FSM):**

- Это структура, которая отслеживает свободное пространство внутри relation files
- Она помогает PostgreSQL понимать, где можно разместить новые строки.

**3. Visibility Map:**

- Эта структура помогает определить, какие строки могут быть видны для транзакций, а какие нет. Это важно для поддержания уровня изоляции транзакций

**4. Index Files:**

- Файлы индексов хранят структуры данных, которые ускоряют поиск в таблице

## Индексы в PostgreSQL

```sql
CREATE [ UNIQUE ] INDEX [ CONCURRENTLY ] [ [ IF NOT EXISTS ] name ] ON [ ONLY ] table_name [ USING method ]
    ( { column_name | ( expression ) } [ COLLATE collation ] [ opclass [ ( opclass_parameter = value [, ... ] ) ] ] [ ASC | DESC ] [ NULLS { FIRST | LAST } ] [, ...] )
    [ INCLUDE ( column_name [, ...] ) ]
    [ NULLS [ NOT ] DISTINCT ]
    [ WITH ( storage_parameter [= value] [, ... ] ) ]
    [ TABLESPACE tablespace_name ]
    [ WHERE predicate ]
```

## Создание индекса, с чего начать?

- Ориентируемся на продуктивное окружение
  
  - Тестовое не соответствует реальности
  
  - Обладать статистикой нагрузки на БД от запросов
    
    - Плохой запрос - медленный запрос? Не всегда, часто - это куча быстрых не оптимизированных запросов 
    
    - **pg_stat_statements** 
      
      - предоставляет возможность отслеживать статистику планирования и выполнения сервером всех операторов SQL
      
      - Обезличенные запросы
  
  - Иметь примеры запросов с параметрами
    
    - Для понимания входящих запросов 
    
    - Для проверки что не сделали хуже

- Уметь читать статистику распределения данных (планировщик, executor)

- Планировщик опирается на данные по представлению pg_stats
  
  - это системная таблица, которая содержит статистическую информацию о данных в таблицах и индексах. Она используется оптимизатором запросов для принятия решений о том, каким образом выполнить запрос с максимальной эффективностью
  
  - **n_distinct** - Количество уникальных значений
  
  - **correlation** - Упорядоченность значений (корреляция между значениями столбца и значениями других столбцов)
  
  - **null_frac** - Объемы null
  
  - **most_common_vals** и **most_common_freq** - Частые значения

## Типы индексов

1. **btree** [B-tree / Хабр](https://habr.com/ru/articles/114154/)
   
   1. Наиболее распространенный тип индексов
   
   2. Алгоритмы работы и модель хранения улучшается
   
   3. Покрывает 90% задач
   
   4. Легко создать ориентируясь на статистику

2. **hash**
   
   1. btree работает быстрее чем hash
   
   2. Раньше были ограничения на дисках, занимает меньше времени
   
   3. Обслуживает операцию равенства, а btree операцию сравнения (Позволяет делать сортировку по индексу, по hash сортировки нет) и равенства 

3. **gist**
   
   1. Полезен для гео данных

```sql
-- Pull the number of rows, indexes, and some info about those indexes
SELECT
    pg_class.relname,
    pg_size_pretty(pg_class.reltuples::bigint)            AS rows_in_bytes,
    pg_class.reltuples                                    AS num_rows,
    COUNT(*)                                              AS total_indexes,
    COUNT(*) FILTER ( WHERE indisunique)                  AS unique_indexes,
    COUNT(*) FILTER ( WHERE indnatts = 1 )                AS single_column_indexes,
    COUNT(*) FILTER ( WHERE indnatts IS DISTINCT FROM 1 ) AS multi_column_indexes
FROM
    pg_namespace
        LEFT JOIN pg_class ON pg_namespace.oid = pg_class.relnamespace
        LEFT JOIN pg_index ON pg_class.oid = pg_index.indrelid
WHERE
        pg_namespace.nspname = 'partners' AND
        pg_class.relkind = 'r'
GROUP BY pg_class.relname, pg_class.reltuples
ORDER BY pg_class.reltuples DESC;
```

```sql
-- Статистика испольования индексов в разрезе схем и таблиц
with tables_without_indexes as (
    select
        relname::text as table_name,
        coalesce(seq_scan, 0) - coalesce(idx_scan, 0) as too_much_seq,
        pg_relation_size(relname::regclass) as table_size,
        coalesce(seq_scan, 0) as seq_scan,
        coalesce(idx_scan, 0) as idx_scan
    from pg_stat_all_tables
    where
            pg_relation_size(schemaname || '.' || relname::regclass) > 5::integer * 8192 and /*skip small tables*/
            relname not in ('databasechangelog')
)
select
       schemaname,
       relname,
       seq_scan,
       idx_scan
from pg_stat_all_tables
where (seq_scan + idx_scan) > 100::integer and -- table in use
        seq_scan > 0 and -- too much sequential scans
        schemaname not in ('pg_catalog', 'pg_toast')
order by schemaname, seq_scan desc;
```

```sql
-- Дубликаты индексов
SELECT pg_size_pretty(sum(pg_relation_size(idx))::bigint) as size,
       (array_agg(idx))[1] as idx1, (array_agg(idx))[2] as idx2,
       (array_agg(idx))[3] as idx3, (array_agg(idx))[4] as idx4
FROM (
         SELECT indexrelid::regclass as idx, (indrelid::text ||E'\n'|| indclass::text ||E'\n'|| indkey::text ||E'\n'||
                                              coalesce(indexprs::text,'')||E'\n' || coalesce(indpred::text,'')) as key
         FROM pg_index) sub
GROUP BY key HAVING count(*)>1
ORDER BY sum(pg_relation_size(idx)) DESC;
```

```sql
-- Статистика по использование и размеру индексов
SELECT
    t.schemaname,
    t.tablename,
    c.reltuples::bigint                            AS num_rows,
    pg_size_pretty(pg_relation_size(c.oid))        AS table_size,
    psai.indexrelname                              AS index_name,
    pg_size_pretty(pg_relation_size(i.indexrelid)) AS index_size,
    CASE WHEN i.indisunique THEN 'Y' ELSE 'N' END  AS "unique",
    psai.idx_scan                                  AS number_of_scans,
    psai.idx_tup_read                              AS tuples_read,
    psai.idx_tup_fetch                             AS tuples_fetched
FROM
    pg_tables t
        LEFT JOIN pg_class c ON t.tablename = c.relname
        LEFT JOIN pg_index i ON c.oid = i.indrelid
        LEFT JOIN pg_stat_all_indexes psai ON i.indexrelid = psai.indexrelid
WHERE
        t.schemaname NOT IN ('pg_catalog', 'information_schema') and
        c.relname != 'flyway_schema_history'
ORDER BY schemaname, index_size;
```

## Источники:

1. https://postgrespro.com/blog/pgsql/5969296

2. [Postgres Pro Standard : Документация: 9.5: 14.1. Использование EXPLAIN : Компания Postgres Professional](https://postgrespro.ru/docs/postgrespro/9.5/using-explain) 

3. [Андрей Сальников — Индексы в PostgreSQL. Как понять, что создавать - YouTube](https://www.youtube.com/watch?v=ju9F8OvnL4E&t=1604s)

4. [Index Maintenance - PostgreSQL wiki](https://wiki.postgresql.org/wiki/Index_Maintenance)

5. [Индексы в PostgreSQL — 1 / Хабр](https://habr.com/ru/companies/postgrespro/articles/326096/)
