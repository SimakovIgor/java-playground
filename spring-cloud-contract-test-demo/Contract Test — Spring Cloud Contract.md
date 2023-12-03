# Contract Test — Spring Cloud Contract

Микросервисная архитектура, представляющая из себя совокупность независимых, маленьких и взаимосвязанных сервисов, стала предпочтительным выбором для многих компаний. Однако, с этим подходом возникают новые вызовы, особенно в области тестирования.

Именно здесь контрактное тестирование, и особенно подход **Consumer-Driven Contracts** (далее **CDC**), играет ключевую роль. Этот подход переносит фокус с централизованного тестирования на сторону потребителей сервисов, позволяя им определять ожидания и проверять соответствие контрактам, определяющим интерфейсы взаимодействия.

## The evolution of software architecture

<img src="file:///Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-02%20в%2019.49.15.png" title="" alt="Снимок экрана 2023-12-02 в 19.49.15.png" width="456">

Источник: [Керниган и Пайк были правы: делай что-то одно и делай это хорошо / Хабр](https://habr.com/ru/companies/ruvds/articles/777212/)

## Как сейчас?

Использованием WireMock

![Снимок экрана 2023-12-02 в 22.27.12.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-02%20в%2022.27.12.png)

## Что такое CDC?

Контрактное тестирование - это метод проверки системного интерфейса между двумя сторонами: потребителем и поставщиком.

Традиционно проверка интерфейса системы включает в себя интеграционное тестирование. Команда по контролю качества может запустить интеграционное тестирование только после завершения разработки и развёртывания систем в тестовой среде.

![Снимок экрана 2023-12-02 в 20.01.43.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-02%20в%2020.01.43.png)

Источник: [# Contract Test — Spring Cloud Contract vs PACT](https://blog.devgenius.io/contract-test-spring-cloud-contract-vs-pact-420450f20429)

Для того чтобы запускать контрактные тесты раньше, идея заключается в установке заглушки (**stub**) в соответствии с согласованным определением контракта.

Для проверки **consumer side** тесты отправляют запросы к **provider stub**. Формат данных тестового запроса проверяется, и затем генерируется имитированный ответ на основе определения контракта. Этот ответ затем проверяется потребителем.

Для проверки **provider side** выполняется в обратном направлении. Тест код генерирует запрос в соответствии с определением контракта, отправляет его провайдеру и затем проверяет, соответствует ли ответ от провайдера определению контракта.

## Contract Test Framework

Сейчас есть 2 популярных фраемворка — [Spring Cloud Contract (SCC)](https://spring.io/projects/spring-cloud-contract) and [PACT](https://docs.pact.io/).

1. **Язык и экосистема**: Spring Cloud Contract разработан для использования в экосистеме Spring и Java. Он интегрируется с инструментами Spring и позволяет создавать контракты с помощью Groovy или YAML файлов, опираясь на возможности Spring MVC Test. Pact, предоставляет возможности для создания контрактов на различных языках программирования (например, Java, JavaScript, Ruby, Kotlin и другие) и может быть использован в различных экосистемах.

2. **Формат контрактов**: Spring Cloud Contract чаще всего использует формат Groovy DSL или YAML для описания контрактов, которые определяют ожидаемые запросы и ответы между потребителем и поставщиком. Pact использует JSON формат для описания контрактов, который обычно называется "Pact файл".

3. **Тестовые движки и инструменты**: Spring Cloud Contract использует тестовые движки, встроенные в экосистему Spring (например, JUnit или Spock).С другой стороны, Pact имеет свои собственные инструменты для тестирования, такие как Pact Broker, а также поддерживает использование различных тестовых фреймворков для запуска тестов (например, JUnit, Jest, Mocha).

4. **Подход к тестированию**: Spring Cloud Contract генерирует контракт со стороны **Provider** , тогда как PACT - со стороны **Consumer**

## Spring Cloud Contract общая схема

1. **Provider Side** — (1) Разработчик подготавливает определение контракта и базовый класс кода тестирования поставщика. (2) Процесс контрактного тестирования генерирует и запускает тестовый код.

2. **Contract Delivery** — (3) Определение контракта преобразуется в описание заглушки WireMock и упаковывается в JAR-файл. Он публикуется в репозиторий артефактов, такой как Git и Nexus.

3. **Consumer Side** — (4) Разработчик подготавливает тестовый код для потребителя, и (5) контрактное тестирование проверяет потребителя с помощью заглушки WireMock, имитирующей ответ поставщика.

![Снимок экрана 2023-12-02 в 22.36.30.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-02%20в%2022.36.30.png)





## Spring Cloud Contract — погружаемся детальнее

### Архитектура приложения

![Снимок экрана 2023-12-02 в 22.47.35.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-02%20в%2022.47.35.png)

### 



### 



### Тестирование синхронного взаимодействия

<img title="" src="file:///Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-02%20в%2022.59.15.png" alt="Снимок экрана 2023-12-02 в 22.59.15.png" width="300">

#### Provider Side

- Общая схема

<img src="file:///Users/igorsimakov/Desktop/1_9gOi79qWwYphbM-JeT026w.webp" title="" alt="1_9gOi79qWwYphbM-JeT026w.webp" width="1013">

- Подключаем зависимости:

![Снимок экрана 2023-12-03 в 00.17.25.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.17.25.png)

![Снимок экрана 2023-12-03 в 00.17.31.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.17.31.png)

- Описываем контракт:

![Снимок экрана 2023-12-03 в 00.18.06.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.18.06.png)

- Проверяем контракт с провайдером:

![Снимок экрана 2023-12-03 в 00.18.23.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.18.23.png)

- mvn clean install

#### Consuner Side

- Общая схема

<img title="" src="file:///Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-02%20в%2023.55.35.png" alt="Снимок экрана 2023-12-02 в 23.55.35.png" width="755">

- Подключаем зависимости

![Снимок экрана 2023-12-03 в 00.21.08.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.21.08.png)

- Примет теста

![Снимок экрана 2023-12-03 в 00.21.54.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.21.54.png)

- mvn clean install



### 



### Тестирование асинхронного взаимодействия

![Снимок экрана 2023-12-03 в 00.24.13.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.24.13.png)

#### Provider Side

- Подключаем зависимости *(выше)*

- Описываем контракт

![Снимок экрана 2023-12-03 в 00.26.23.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.26.23.png)

- Тестируем контракт на провайдере

![Снимок экрана 2023-12-03 в 00.26.52.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2000.26.52.png)

- mvn clean install

#### Consuner Side

- Подключаем зависимости

- Пример теста
  
  ![Снимок экрана 2023-12-03 в 12.22.24.png](/Users/igorsimakov/Desktop/Снимок%20экрана%202023-12-03%20в%2012.22.24.png)

- mvn clean install



### Полезные ссылки:

1. [GitHub - SimakovIgor/microservices-customer: Customer microservice](https://github.com/SimakovIgor/microservices-customer)

2. [GitHub - SimakovIgor/microservices-fraud: microservices fraud](https://github.com/SimakovIgor/microservices-fraud)

3. [GitHub - SimakovIgor/microservices-notification: microservices-notification](https://github.com/SimakovIgor/microservices-notification)

4. https://blog.devgenius.io/contract-test-spring-cloud-contract-vs-pact-420450f20429

5. [2.&nbsp;Spring Cloud Contract Verifier Introduction](https://cloud.spring.io/spring-cloud-contract/2.1.x/multi/multi__spring_cloud_contract_verifier_introduction)

6. [Spring Cloud Contract (HTTP) - YouTube](https://www.youtube.com/watch?v=Ki54FB2B25U&list=PPSV)

7. https://docs.spring.io/spring-cloud-contract/reference/4.1/project-features-stubrunner/stub-runner-boot.html

8. https://github.com/spring-cloud-samples/spring-cloud-contract-samples/tree/main/producer_rabbit_middleware


