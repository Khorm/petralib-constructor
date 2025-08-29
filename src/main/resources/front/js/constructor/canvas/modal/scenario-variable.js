import React, { useState, useEffect, useRef } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import { add } from './scenario-variable-slice';
import { useReduxInitEffect } from './hook';

import 'bootstrap/dist/css/bootstrap.min.css';

import SimpleVariable from './simple/simple-variable';
import SourceVariable from './source/source-variable';
import ScriptVariable from './script/script-variable';



export default function ScenarioVariable({currentVariable, inputVariables, currentVariables}){

    const SIMPLE = 'SIMPLE';
    const SCRIPT = 'SCRIPT';
    const SOURCE = 'SOURCE_OUT';

    const scenarioVariables = useSelector((state) => state.scenarioVariables.list);
    const dispatch = useDispatch();

    const [currentVarType, setCurrentVarType] = React.useState(undefined);


    useReduxInitEffect(scenarioVariables, (initialData) => {
        const scenarioVar = scenarioVariables.filter(scVar => scVar.consumerVariableId === currentVariable.id);
        if (!scenarioVar || scenarioVar.length === 0) return;

        if (scenarioVar[0].type === SIMPLE){
            setCurrentVarType(SIMPLE);
        }else if (scenarioVar[0].type === SCRIPT){
            setCurrentVarType(SCRIPT);
        }else if (scenarioVar[0].type === SOURCE){
            setCurrentVarType(SOURCE);
        }
    });


    function setVat(e){
        setCurrentVarType(e.target.value);
    }

    function createVarName(variable){
        let type;
        if (variable.multiplicity === 'COLLECTION'){
            type = 'Collection<' + variable.varType.name + '>';
        }else{
            type = variable.varType.name;
        }
        return (<h4> {type + " : " + variable.name} </h4> );
    }

    return(
        <div>
            {currentVarType === SIMPLE &&
                <SimpleVariable currentVariable={currentVariable}
                 currentVariables = {currentVariables} inputVariables={inputVariables} removeVar={() => setCurrentVarType(undefined)}/>
            }

            {currentVarType === SOURCE &&
                <SourceVariable currentVariable={currentVariable}
                 currentVariables = {currentVariables} inputVariables={inputVariables} removeVarFunc={() => setCurrentVarType(undefined)} />
            }

            {currentVarType === SCRIPT &&
                <ScriptVariable currentVariable={currentVariable}  removeVar={() => setCurrentVarType(undefined)} />
            }

            {currentVarType === undefined &&
                    <div>
                    <h4>{createVarName(currentVariable)}</h4>
                    <select defaultValue="none" name="value-select" id="value-select"  onChange={setVat}>
                        <option disabled hidden value='none'> -- select a variable type -- </option>
                        <option value={SIMPLE}>Simple</option>
                        <option value={SOURCE}>Source</option>
                        <option value={SCRIPT}>Script</option>
                    </select>
                    </div>
            }
        </div>
    )
}