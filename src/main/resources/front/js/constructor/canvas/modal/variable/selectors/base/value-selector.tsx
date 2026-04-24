import React, { useState, useEffect } from 'react'
import { add } from '../../../scenario-variable-slice';
import 'bootstrap/dist/css/bootstrap.min.css';


import { useSelector, useDispatch } from 'react-redux';
import SourceSelector from '../source/source-selector';
import ValueTypeConstructor from './value-type-constructor';
import SimpleSelector from '../simple/simple-selector';
import { useVariable } from '../add-variable-hook';
import { ValueType } from '../../enum/value-type';
import { ScenarioVariableDto, VariableDto } from '../../../scenario-block-modal';
import ScriptSelector from '../script/script-selector';
import { VarFunctionType } from '../../enum/variable-function-type';

interface ValueSelectorProps {
   consumerVariable: VariableDto;
   blockVariable: VariableDto;
   acceptedVariables: VariableDto[],
   parentId: number;
   scenarioVar?: ScenarioVariableDto;
   functionType: VarFunctionType;
   
}

export default function ValueSelector({ consumerVariable, blockVariable, acceptedVariables, scenarioVar = undefined,
    functionType
 }:
    ValueSelectorProps
) {

    const dispatch = useDispatch();
    const { updateSimpleScenarioVariable, updateSourceScenarioVar, updateScriptScenarioVariable } = useVariable();


    function create(createdType: ValueType) {
        let scenarioVar :ScenarioVariableDto;        
        switch (createdType) {
            case 'SIMPLE':
                scenarioVar = updateSimpleScenarioVariable(consumerVariable.id, undefined,  blockVariable.id, 'PARAMETER');
                break;
            case 'SOURCE':
                scenarioVar = updateSourceScenarioVar(consumerVariable.id, undefined,  blockVariable.id, 'FUNCTION');
                break;
            case 'SCRIPT':
                scenarioVar = updateScriptScenarioVariable(consumerVariable.id, undefined,  blockVariable.id, 'FUNCTION');
                break;
        }
        console.log("scenariovar", scenarioVar)
        dispatch(add(scenarioVar));
    }

    return (
        <div>
            {!scenarioVar &&
                <ValueTypeConstructor setSelectedType={create} functionType={functionType}/>
            }
            {scenarioVar?.type === 'SIMPLE' &&
                <SimpleSelector blockVariable={blockVariable} consumerVariable={consumerVariable} 
                acceptedVariables={acceptedVariables} functionType='PARAMETER' />
            }
            {scenarioVar?.type === 'SOURCE' &&
                <SourceSelector blockVariable={blockVariable} consumerVariable={consumerVariable} 
                acceptedVariables={acceptedVariables} scenarioVariable={scenarioVar} functionType='FUNCTION'/>
            }
            {scenarioVar?.type === 'SCRIPT' &&
                <ScriptSelector blockVariable={blockVariable} consumerVariable={consumerVariable}
                scenarioVariable={scenarioVar}  functionType='FUNCTION' availableVariables={acceptedVariables}/>
            }

        </div>
    )




}


