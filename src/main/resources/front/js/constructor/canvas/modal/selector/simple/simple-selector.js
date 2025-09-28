import React, { useState, useEffect } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css';

import TypeSelector from '../../type/type-selector';
import { useVariable } from '../hooks/add-variable-hook';


export default function SimpleSelector({ blockVariableId, consumerVariable, inputVariables, localId, parentId }) {

    const { getScenarioVariable, updateSimpleScenarioVariable, updateScenarioVarTypeInheritance, remove } = useVariable();
    const [selectedProducerId, setSelectedProducerId] = useState(scenarioVariable()?.producerId);

    function scenarioVariable() {
        return getScenarioVariable(localId, blockVariableId);
    }

    function handleChange(e) {
        const producerVariableId = +e.target.value;
        setSelectedProducerId(producerVariableId);
        updateSimpleScenarioVariable(consumerVariable.id, producerVariableId, blockVariableId, localId, parentId);
    }

    function setTypeCollection(typeCollection) {
        updateScenarioVarTypeInheritance(localId, typeCollection, blockVariableId)
    }

    function inputVariablesWithoutSelf() {
        return inputVariables.filter(curVar => curVar.id !== consumerVariable.id && curVar.id !== blockVariableId);
    }

    function getDefaultVariable() {
        const cur = getCurrentProducerVariable();
        if (cur) {
            return cur.id + '';
        } else {
            return 'none';
        }
    }

    function getCurrentProducerVariable() {        
        return inputVariables.find(curVar => curVar.id === selectedProducerId);       
    }

    function getProducerVariableName() {
        const curInputVariable = getCurrentProducerVariable();
        if (!curInputVariable) return '';
        return curInputVariable.name;
    }




    return (
        <div className='container'>
            {selectedProducerId !== undefined &&
                <h6>{getProducerVariableName()} </h6>
            }
            {!selectedProducerId &&
                <select defaultValue={getDefaultVariable()} onChange={handleChange}>
                    <option disabled hidden value='none'> -- select a variable -- </option>
                    {inputVariablesWithoutSelf().map((curVariable, index) => {
                        return (
                            <option key={index} value={curVariable.id}>{curVariable.name}</option>
                        )
                    })}
                </select>
            }
            {getCurrentProducerVariable() !== undefined && getCurrentProducerVariable()?.multiplicity !== 'COLLECTION' && scenarioVariable() !== undefined &&
                <TypeSelector baseTypeId={getCurrentProducerVariable().variableType.id}
                    typeArray={scenarioVariable().typeInheritance} setNewTypesArray={setTypeCollection} />
            }
            <button onClick={() => remove(localId, blockVariableId)}>X</button>
        </div>
    )

}