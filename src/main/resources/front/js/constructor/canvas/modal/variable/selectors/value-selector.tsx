import React, { useState, useEffect } from 'react'
import { add } from '../../scenario-variable-slice';
import 'bootstrap/dist/css/bootstrap.min.css';


import { useSelector, useDispatch } from 'react-redux';
import SourceSelector from './source/source-selector';
import ValueTypeConstructor from '../value-type-constructor';
import SimpleSelector from './simple/simple-selector';
import { useVariable } from './add-variable-hook';
import { ValueType } from '../value-type';
import { ScenarioVariableDto, VariableDto } from '../../scenario-block-modal';
import idGenerator from './id-generator-hook';
import ScriptSelector from './script/script-selector';

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

    const dispatch = useDispatch();
    const { updateSimpleScenarioVariable, updateSourceScenarioVar, updateScriptScenarioVariable } = useVariable();


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
            case 'SCRIPT':
                scenarioVar = updateScriptScenarioVariable(consumerVariable.id, undefined,  blockVariable.id, undefined, parentId);
                break;
        }
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
            {scenarioVar?.type === 'SCRIPT' &&
                <ScriptSelector blockVariable={blockVariable} consumerVariable={consumerVariable}
            localId={scenarioVar.localId} parentId={parentId} scenarioVariable={scenarioVar}  />
            }

        </div>
    )




}


