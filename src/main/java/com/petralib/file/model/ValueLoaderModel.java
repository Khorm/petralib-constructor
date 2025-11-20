package com.petralib.file.model;



import com.petralib.file.enums.LoaderType;
import com.petralib.ctype.enums.Multiplicity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ValueLoaderModel {
    
    public ValueLoaderModel() {
        // Default constructor
    }

    //айди переменной сценария
    private Long scenarioVariableId;

    //айди переменной в дереве
    private Long localId;

    //имя переменнок
    private String name;
    private String multiplicity;


    //тип лоадера
    private String loaderType;

    //зависимые переменные
    private Collection<ValueLoaderModel> children;

    //айди родителя переменной блока
    private Long parent;

    //переменные блока, которые необходимо загрузить до загрузки текущего значения
    private Collection<Long> requiredBlockVariables;

    //входящая переменная
    private Long producerVariableId;

    private Long consumerVariableId;

    //строка извлечения для сложного объекта
    private String extractionString;

    //скрипт
    private String script;

//    //айди соурса
//    private Long sourceId;

    //версия соурса
    private String sourceVersion;
    private String sourceName;
    private String sourceServiceName;

    //переменные соурса
//    private Collection<ValueLoaderModel> sourceInputVariableModels;


    public LoaderType getLoaderType() {
        return LoaderType.valueOf(loaderType);
    }

    public Multiplicity getMultiplicity() {
        return Multiplicity.valueOf(multiplicity);
    }

}
