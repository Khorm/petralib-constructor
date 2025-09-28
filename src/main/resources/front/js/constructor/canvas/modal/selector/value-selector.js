import React, { useState, useEffect } from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';


import { useSelector, useDispatch } from 'react-redux';
import SourceSelector from './source/source-selector';
import { SIMPLE, SCRIPT, SOURCE } from './value-type';
import ValueTypeConstructor from './value-type-selector';
import SimpleSelector from './simple/simple-selector';
import ScriptSelector from './script/script-selector';
import { useVariable } from './hooks/add-variable-hook';



export default function ValueSelector({ consumerVariable, blockVariableId, inputVariables, parentId, defaultLocalId = undefined }) {

    const scenarioVariablesList = useSelector(state => state.scenarioVariables.list);
   
    const { updateSimpleScenarioVariable, updateSourceScenarioVar, updateScriptScenarioVariable } = useVariable();
    const [localId, setLocalId] = useState(defaultLocalId);
    const [curScenarioVariable, setCurScenarioVariable] = useState(undefined);

    // useEffect(() => {
    //     console.log("SET DEFAULT ID : ", defaultLocalId);
    //     setLocalId(defaultLocalId);
    // }, []);

    useEffect(() => {
        updateValue();
    }, [localId]);

    useEffect(() => {
        updateValue();
    }, [scenarioVariablesList]);

    function updateValue() {
        console.log("UPDATE SCENARIO VARIABLES : ", scenarioVariablesList, localId, blockVariableId);
        let find = false;
        for (let i = 0; i < scenarioVariablesList.length; i++) {
            if (scenarioVariablesList[i].blockVariableId === blockVariableId && scenarioVariablesList[i].localId === localId) {
                if (curScenarioVariable === undefined) {
                    setCurScenarioVariable(scenarioVariablesList[i]);
                }
                find = true;
                break;
            }
        }
        if (!find) {
            setCurScenarioVariable(undefined);
            setLocalId(undefined);
        }

    }


    function create(createdType) {
        let localId;
        switch (createdType) {
            case SIMPLE:
                localId = updateSimpleScenarioVariable(consumerVariable.id, undefined, blockVariableId, undefined, parentId);
                break;
            case SOURCE:
                localId = updateSourceScenarioVar(consumerVariable.id, undefined, blockVariableId, undefined, parentId);
                break;
            case SCRIPT:
                localId = updateScriptScenarioVariable(consumerVariable.id, undefined, blockVariableId, undefined, parentId);
                break;
        }
        // console.log("SET LOCAL ID : ", scenarioVariablesList, localId, blockVariableId);
        setLocalId(localId);
    }

    return (
        <div>
            {!curScenarioVariable &&
                <ValueTypeConstructor setSelectedType={create} />
            }
            {curScenarioVariable?.type === SIMPLE &&
                <SimpleSelector blockVariableId={blockVariableId} consumerVariable={consumerVariable} inputVariables={inputVariables}
                    localId={localId} parentId={parentId} />
            }
            {curScenarioVariable?.type === SOURCE &&
                <SourceSelector blockVariableId={blockVariableId} consumerVariable={consumerVariable} inputVariables={inputVariables}
                    localId={localId} parentId={parentId} />
            }
            {curScenarioVariable?.type === SCRIPT &&
                <ScriptSelector blockVariableId={blockVariableId} consumerVariable={consumerVariable} scenarioScript={curScenarioVariable.script}
                    localId={localId} parentId={parentId} />
            }
        </div>
    )

}