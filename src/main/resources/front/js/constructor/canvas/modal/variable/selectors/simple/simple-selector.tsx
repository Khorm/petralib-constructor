import React, { useState, useEffect } from 'react'
import { add, remove } from '../../../scenario-variable-slice';
import { useSelector, useDispatch } from 'react-redux';

import 'bootstrap/dist/css/bootstrap.min.css';

import TypeSelector, { CTypeFieldDto } from '../../type/type-selector';
import { useVariable } from '../add-variable-hook';
import { ScenarioVariableDto, VariableDto } from '../../../scenario-block-modal';

import './simple-variable.sass';
import { VarFunctionType } from '../../enum/variable-function-type';



interface SimpleSelectorProps {
   consumerVariable: VariableDto;
   blockVariable: VariableDto;
   acceptedVariables: VariableDto[],
   functionType: VarFunctionType  
}

export default function SimpleSelector({ blockVariable, consumerVariable, acceptedVariables, functionType }
    : SimpleSelectorProps
) {

    const dispatch = useDispatch();
    const { updateSimpleScenarioVariable, updateScenarioVarTypeInheritance} = useVariable();

    const [producerVariable, setProducerVariable] = React.useState<VariableDto>(undefined);
    const [scenarioVariable, setScenarioVariable] = React.useState<ScenarioVariableDto>(undefined);
    const scenarioVariables = useSelector((state: any) => state.scenarioVariables.list);


    useEffect(() => {  
        
        const newScenVar : ScenarioVariableDto = scenarioVariables.find((scenVar: ScenarioVariableDto) =>
             scenVar.consumerVariableId === consumerVariable.id && blockVariable.id === scenVar.blockVariableId);
        // console.log("FIND : ", newScenVar, scenarioVariables, )

        // if (newScenVar?.producerId){
            // console.log('set producer ', newScenVar?.producerId)
            setProducerVariable(
                acceptedVariables.find(curVar => curVar.id === newScenVar?.producerId)
            );
        // }
        // if (newScenVar){
            setScenarioVariable(newScenVar);
        // }
        
    }, [scenarioVariables]);

    function handleChange(e: React.ChangeEvent<HTMLSelectElement>) {
        const producerVariableId = +e.target.value;
        
        const newScenarioVar : ScenarioVariableDto = updateSimpleScenarioVariable(consumerVariable.id,
             producerVariableId, blockVariable.id, functionType);
        
        dispatch(add(newScenarioVar));
    }


    function setTypeCollection(typeInheritance: CTypeFieldDto[]) {         
        
        if (!scenarioVariable){
            return
        }
        
        const newScenarioVar : ScenarioVariableDto = updateScenarioVarTypeInheritance(functionType,
             typeInheritance, blockVariable.id)
        // console.log('newScenarioVar', newScenarioVar);
        dispatch(add(newScenarioVar));
    }


    function inputVariablesWithoutSelf() : VariableDto[] {
        return acceptedVariables.filter(curVar => curVar.id !== consumerVariable.id && curVar.id !== blockVariable.id);
    }

    function getDefaultVariable() {
        const cur = producerVariable;
        if (cur) {
            return cur.id + '';
        } else {
            return 'none';
        }
    }

    // function getCurrentProducerVariable() {        
    //     return inputVariables.find(curVar => curVar.id === selectedProducerId);       
    // }

    function getVariableName(curInputVariable :VariableDto) : string {
        // const curInputVariable = producerVariable;
        if (!curInputVariable) return '';
        let resultName : string = '';
        if (curInputVariable.multiplicity === 'COLLECTION') {
            resultName += 'Collection<';
        }else{
            resultName += '(';
        }

        resultName += curInputVariable.variableType.name;
        
        if (curInputVariable.multiplicity === 'COLLECTION') {
            resultName += '>';
        }else{
            resultName += ')';
        }
        resultName += curInputVariable.name;
        return resultName;
    }


    const removeScenarioVar  = () => {
        dispatch(remove(scenarioVariable))
    }


    return (
        <div className='container'>
            {producerVariable !== undefined &&
                <h4>{getVariableName(producerVariable)} </h4>
            }
            {!producerVariable &&
                <select defaultValue={getDefaultVariable()} onChange={handleChange}>
                    <option disabled hidden value='none'> -- select a variable -- </option>
                    {inputVariablesWithoutSelf().map((curVariable, index) => {
                        return (
                            <option key={index} value={curVariable.id}>{getVariableName(curVariable)}</option>
                        )
                    })}
                </select>
            }
            {producerVariable !== undefined && producerVariable?.multiplicity !== 'COLLECTION' && scenarioVariable !== undefined &&
                <div>                    
                    <TypeSelector ownerTypeId={producerVariable.variableType.id}
                        typeArray={scenarioVariable.typeInheritance} setNewTypesArray={setTypeCollection} />
                </div>
            }
            <button onClick={removeScenarioVar}>X</button>
        </div>
    )

}