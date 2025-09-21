import React, { } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import { useReduxInitEffect } from '../hook';

import 'bootstrap/dist/css/bootstrap.min.css';

import SimpleVariable from '../simple/simple-variable';



export default function WorkflowVariable({ currentVariable, inputVariables, currentVariables }) {

    const SIMPLE = 'SIMPLE';

    const scenarioVariables = useSelector((state) => state.scenarioVariables.list);

    const [currentVarType, setCurrentVarType] = React.useState(undefined);


    useReduxInitEffect(scenarioVariables, (initialData) => {
        const scenarioVar = scenarioVariables.filter(scVar => scVar.consumerVariableId === currentVariable.id);
        if (!scenarioVar || scenarioVar.length === 0) return;
        setCurrentVarType(SIMPLE);
    });


    function setVat(e) {
        setCurrentVarType(e.target.value);
    }

    function createVarName(variable) {
        let type;
        if (variable.multiplicity === 'COLLECTION') {
            type = 'Collection<' + variable.fieldType.name + '>';
        } else {
            type = variable.fieldType.name;
        }
        return (<h4> {type + " : " + variable.name} </h4>);
    }

    

    return (
        <div>
            {currentVarType === SIMPLE &&
                <SimpleVariable currentVariable={currentVariable}
                    currentVariables={currentVariables} inputVariables={inputVariables} removeVar={() => setCurrentVarType(undefined)} />
            }

            {currentVarType === undefined &&
                <div>
                    <h4>{createVarName(currentVariable)}</h4>
                    <select defaultValue="none" name="value-select" id="value-select" onChange={setVat}>
                        <option disabled hidden value='none'> -- select a variable type -- </option>
                        <option value={SIMPLE}>Simple</option>              
                    </select>
                </div>
            }
        </div>
    )
}