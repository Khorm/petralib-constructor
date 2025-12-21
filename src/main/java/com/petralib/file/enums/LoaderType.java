package com.petralib.file.enums;

import com.petralib.scenario.enums.ScenarioVariableType;

public enum LoaderType {
    EMPTY_LOADER,
    INPUT_LOADER,
    SCRIPT_LOADER,
    SOURCE_LOADER
    ;

    public static LoaderType fromScenarioVariableType(ScenarioVariableType type){
        switch (type){
            case SIMPLE:
                return INPUT_LOADER;
            case SCRIPT:
                return SCRIPT_LOADER;
            case SOURCE:
                return SOURCE_LOADER;
        }
        throw new RuntimeException("Unknown type: " + type);
    }



}
