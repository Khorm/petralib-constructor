# Дальнейшие действия разработчика

## Быстрый старт тестов
- Профиль `test`: H2, сидер пользователя (`r0meo1.ru@gmail.com` / `8K3uLnPVGTtcm5a`) в тестовой конфигурации.
- Подпроект `api-tests` (интеграционные тесты по API-документации):
  - `./gradlew :api-tests:test --tests com.petralib.test.ApiDocAutoTests` — зелёный прогон (auth/projects/services/types/blocks).
- Полный прогон (основной модуль + api-tests):
  - `./gradlew test` — сейчас падает (см. причины и план ниже).

## Текущие проблемы полного прогона
- AuthControllerTest: нет валидации пустых email/password (ожидают 400).
- Block/Type/Service Rest/Service tests: `pageNumber=0` → IllegalArgumentException; NPE при `variables == null` и отсутствии project/service/type; попытки читать/удалять несуществующие сущности.
- ScenarioRestControllerTest: NPE/TransientPropertyValue — нет подготовленных workflow/blocks.
- SmokeTest: статусы 401/403 vs ожидания (нет валидных данных/токена).
- Unit сервисов (BlockService/TypeService): NPE/IllegalArgumentException на пустых данных.

## План, чтобы вывести `./gradlew test` в зелёный
1. Auth: добавить `@NotBlank` в AuthRequestDTO и `@Valid` в AuthController.authenticate.
2. Block/Type:
   - `variables == null` → пустой список в сервисах.
   - `pageNumber` → pageIndex = max(pageNumber-1, 0) в getBlocksByProjectAndName/getTypesPage.
   - Сидер данных (project/service/type/workflow) или graceful-ответы контроллеров/сервисов при отсутствии данных.
3. Service/Scenario:
   - Аналогично: сидер или устойчивость к отсутствующим сущностям (возвращать пустые DTO/200 вместо NPE).
4. SmokeTest: валидный токен/данные (сидер) или корректировка ожиданий статусов.
5. Повторить `./gradlew test`.

## Ветки/PR
- Основная `develop` очищена (revert тестов).
- PR с автотестами в подпроекте: `feature/api-doc-tests-pr` → https://github.com/Khorm/petralib-constructor/pull/6.
