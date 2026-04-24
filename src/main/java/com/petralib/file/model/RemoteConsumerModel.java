package com.petralib.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Модель данных для удалённого потребителя (Remote Consumer) в конструкторе сценариев.
 * Используется при экспорте/импорте модели сервиса для описания внешних зависимостей,
 * где текущий сервис получает данные от другого сервиса (возможно, в другой системе или проекте).
 *
 * <p>Пример использования: текущий блок подключён к выходу блока из удалённого workflow,
 * и эта модель хранит информацию о том, откуда приходят данные.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RemoteConsumerModel {
    /**
     * Уникальный идентификатор удалённого потребителя.
     * Может использоваться для ссылок внутри модели или отслеживания изменений.
     */
    private Long id;

    /**
     * Версия удалённого потребителя (если поддерживается система версий).
     * Используется для контроля совместимости при обновлениях.
     * Пример: "1.0.0", "2.1"
     */
    private String version;

    /**
     * Имя сервиса, которому принадлежит потребитель.
     * Отображается в интерфейсе для идентификации источника.
     * Пример: "Notification Service", "Data Processing API"
     */
    private String serviceName;

    /**
     * Уникальный идентификатор workflow в удалённом сервисе.
     * Определяет, из какого именно рабочего процесса поступают данные.
     */
    private Long workflowId;

    /**
     * Версия удалённого workflow.
     * Обеспечивает точное сопоставление структуры данных при маппинге.
     */
    private String workflowVersion;

    private String consumerName;

    /**
     * Коллекция загружаемых переменных
     */
    private Collection<ValueModel> loadedValues;

    /**
     * Коллекция всех переменных включая загружаемые
     */
    private Collection<ValueDto> contextValues;

    /**
     * Идентификаторы следующих элементов
     */
    private Long nextBlockId;
    private Long previousBlockId;

}
