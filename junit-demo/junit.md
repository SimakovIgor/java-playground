#### Определение UNIT тестирования и его важность:

UNIT-тестирование - это процесс проверки отдельных компонентов или модулей программы с целью убедиться в их корректности  
и соответствии спецификации. Эти компоненты могут быть функциями, методами, классами или даже модулями.  

UNIT-тестирование важно по нескольким причинам:  

1. Помогает обнаружить ошибки на ранних стадиях разработки, что уменьшает затраты на исправление проблем в будущем.  
2. Обеспечивает документацию: тесты описывают ожидаемое поведение компонентов, что облегчает понимание их использования.  
3. Способствует улучшению дизайна: для того чтобы компоненты были тестируемыми, они должны быть хорошо структурированы и  
   связаны.  
4. Позволяет проводить рефакторинг кода с уверенностью в его надежности.  

#### Краткое введение в инструменты JUnit, Mockito и AssertJ:

**JUnit**: JUnit - это фреймворк для тестирования Java-приложений. Он предоставляет аннотации и классы для определения и  
запуска тестов, а также проверки ожидаемых результатов. JUnit облегчает создание и выполнение тестовых сценариев.  

**Mockito**: Mockito - библиотека для создания моков (фиктивных объектов) в тестах. Она позволяет настроить поведение моков  
и проверить взаимодействие тестируемого кода с ними.  

**AssertJ**: AssertJ - это библиотека для создания утверждений в тестах. Она предоставляет более выразительные методы для  
проверки ожидаемых результатов, что делает тесты более читаемыми и понятными.  

**JaCoCo**: JaCoCo (Java Code Coverage) - инструмент для измерения покрытия кода тестами. Он анализирует выполнение тестов и  
предоставляет отчеты о покрытии, позволяя определить, какие части кода были протестированы, а какие нет. JaCoCo помогает  
локализовать недостаточно протестированные участки кода и улучшает качество тестирования.

#### Анализируем покрытие кода тестами jacoco

**Введение в JaCoCo**

JaCoCo, или Java Code Coverage, представляет собой инструмент для измерения покрытия кода при выполнении тестов.

**Как JaCoCo помогает улучшить качество кода?**

JaCoCo предоставляет информацию о том, какие участки кода были выполнены при запуске тестов, а какие остались непокрытыми

**Типы покрытия**

- **Линейное покрытие (Line coverage)**: Этот тип покрытия оценивает, сколько строк кода было выполнено при запуске тестов. Он сообщает о том, сколько процентов строк кода были пройдены тестами.

- **Ветвевое покрытие (Branch coverage)**: Ветвевое покрытие измеряет, сколько процентов ветвей в коде были выполнены тестами. Ветви представляют различные варианты выполнения кода, такие как условные операторы if-else.

- **Покрытие инструкций (Instruction coverage)**: Этот тип покрытия отслеживает, сколько инструкций в байт-коде Java были выполнены тестами. Он оценивает точность выполнения каждой инструкции в коде.

- **Покрытие методов (Method coverage)**: Данный тип покрытия измеряет, сколько методов были вызваны и выполнены при запуске тестов.

- **Покрытие классов (Class coverage)**: Покрытие классов сообщает о том, сколько классов были выполнены при запуске тестов. Это позволяет оценить, насколько разнообразно тестируется весь класс.

**Настройка минимального порога**

**Исключение из покрытия**

#### Основы JUnit: аннотации, структура тестового класса:

**@Test**: Обозначает метод как тестовый.  
**@AfterEach**: Метод, помеченный этой аннотацией, будет выполнен перед каждым тестовым методом.  
**@BeforeEach**: Метод, помеченный этой аннотацией, будет выполнен после каждого тестового метода.  
**@BeforeAll**: Метод, помеченный этой аннотацией, будет выполнен перед всеми тестами в классе (статический метод).  
**@AfterAll**: Метод, помеченный этой аннотацией, будет выполнен после всех тестов в классе (статический метод).

#### Основы Mockito

1. **Введение**
   
   1. Описание Mockito как библиотеки для создания мок-объектов в юнит-тестах.
   2. Зачем нужны моки и какие проблемы они решают.

2. **Основные возможности Mockito**
   
   - Создание мок-объектов с помощью `Mockito.mock()`.
   - Создание spy объектов с помощью `Mockito.spy()`
   - Определение поведения мок-объектов с помощью `when()` и `thenReturn()`.
   - Определение поведения spy объектов с помощью `doReturn(...).when(service).method(args)`
   - Проверка вызовов методов с помощью `verify(), verifyNoMoreInteractions()`.
   - Спецификация вызовов с использованием `VerificationMode: times(), atLeastOnce(), atMostOnes(), atLeast(). atMost(), never()`

3. **ArgumentMatcher**
   
   - **Argument Matchers** - это механизм, предоставляемый библиотекой Mockito, который позволяет вам более гибко и точно определять ожидаемые аргументы при вызове методов мок-объектов. Это особенно полезно, когда тестируемый метод ожидает специфические значения аргументов, и вы хотите, чтобы ваш тест был более гибким и устойчивым к изменениям.
   - Использование `any()`, `eq()` и других методов для работы с аргументами.
   - Другие матчеры, такие как `isNull()`, `notNull()`, `startsWith()`, `endsWith()`, `contains()`, позволяют более точно специфицировать ожидаемые аргументы.

4. **ArgumentCaptor**
   
   1. Это инструмент в библиотеке Mockito, который позволяет захватывать и сохранять аргументы, переданные в вызовы методов моков
   
   2. Особенно полезно, когда вы хотите проверить значения аргументов, переданных в метод, или когда аргументы представляют собой сложные объекты

5. **Обработка исключений**
   
   - `assertThatThrownBy`
   - Использование `doThrow()` и `doReturn()` при работе с void-методами.

#### Коллекции и AssertJ

1. Использование  `assertThat() и isEqualTo()`

2. Сравнение объектов по свойствам`usingRecursiveComparison(), ignoringFields(), .withStrictTypeChecking()`

3. Проверка исключений `assertThatThrownBy(), isInstanceOf(), hasMessageContaining()`
