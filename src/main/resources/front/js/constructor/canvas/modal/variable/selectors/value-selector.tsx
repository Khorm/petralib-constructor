import React, { useState, useEffect } from 'react'
import { add } from '../../scenario-variable-slice';
import 'bootstrap/dist/css/bootstrap.min.css';


import { useSelector, useDispatch } from 'react-redux';
import SourceSelector from './source/source-selector';
import ValueTypeConstructor from '../value-type-constructor';
import SimpleSelector from './simple/simple-selector';
import ScriptSelector from '../../selector/script/script-selector';
import { useVariable } from './add-variable-hook';
import { ValueType } from '../value-type';
import { ScenarioVariableDto, VariableDto } from '../../scenario-block-modal';
import idGenerator from './id-generator-hook';

interface ValueSelectorProps {
   consumerVariable: VariableDto;
   blockVariable: VariableDto;
   acceptedVariables: VariableDto[],
   parentId: number;
   scenarioVar?: ScenarioVariableDto;
}

export default function ValueSelector({ consumerVariable, blockVariable, acceptedVariables, parentId, scenarioVar = undefined }:
    ValueSelectorProps
) {

    // const scenarioVariablesList = useSelector(
    //     (state: any) => state.scenarioVariables.list
    // ) as ScenarioVariableDto[];
    const dispatch = useDispatch();
    const { updateSimpleScenarioVariable, updateSourceScenarioVar } = useVariable();
    // const [localId, setLocalId] = useState(defaultLocalId);

    // useEffect(() => {
    //     console.log("SET DEFAULT ID : ", defaultLocalId);
    //     setLocalId(defaultLocalId);
    // }, []);

    // useEffect(() => {
    //     updateValue();
    // }, [localId]);

    // useEffect(() => {
    //     updateValue();
    // }, [scenarioVariablesList]);

    // function updateValue() {
    //     console.log("UPDATE SCENARIO VARIABLES : ", scenarioVariablesList, localId, blockVariable.id);
    //     let find = false;
    //     for (let i = 0; i < scenarioVariablesList.length; i++) {
    //         if (scenarioVariablesList[i].blockVariableId ===  blockVariable.id && scenarioVariablesList[i].localId === localId) {
    //             if (curScenarioVariable === undefined) {
    //                 setCurScenarioVariable(scenarioVariablesList[i]);
    //             }
    //             find = true;
    //             break;
    //         }
    //     }
    //     if (!find) {
    //         setCurScenarioVariable(undefined);
    //         setLocalId(undefined);
    //     }

    // }


    function create(createdType: ValueType) {
        let scenarioVar :ScenarioVariableDto;
        const localId = idGenerator.generateId(blockVariable.id);
        switch (createdType) {
            case 'SIMPLE':
                scenarioVar = updateSimpleScenarioVariable(consumerVariable.id, undefined,  blockVariable.id, parentId, localId);
                break;
            case 'SOURCE':
                scenarioVar = updateSourceScenarioVar(consumerVariable.id, undefined,  blockVariable.id, localId, parentId);
                break;
            // case 'SCRIPT':
            //     localId = updateScriptScenarioVariable(consumerVariable.id, undefined,  blockVariable.id, undefined, parentId);
            //     break;
        }
        // console.log("SET LOCAL ID : ", scenarioVariablesList, localId, blockVariableId);
        // setLocalId(localId);
        dispatch(add(scenarioVar));
    }

    return (
        <div>
            {!scenarioVar &&
                <ValueTypeConstructor setSelectedType={create} />
            }
            {scenarioVar?.type === 'SIMPLE' &&
                <SimpleSelector blockVariable={blockVariable} consumerVariable={consumerVariable} acceptedVariables={acceptedVariables}
             parentId={scenarioVar.parentId}  />
            }
            {scenarioVar?.type === 'SOURCE' &&
                <SourceSelector blockVariable={blockVariable} consumerVariable={consumerVariable} acceptedVariables={acceptedVariables}
            localId={scenarioVar.localId} parentId={parentId} scenarioVariable={scenarioVar} />
            }

        </div>
    )


            // {curScenarioVariable?.type === 'SCRIPT' &&
            //     <ScriptSelector blockVariableId={blockVariable.id} consumerVariable={consumerVariable} scenarioScript={curScenarioVariable.script}
            //         localId={localId} parentId={parentId} />
            // }

}


