import React, { useState, useEffect } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css';

import { ScenarioVariableDto, VariableDto } from '../../../scenario-block-modal';
import { useVariable } from '../add-variable-hook';
import { useDispatch } from 'react-redux';
import { add, remove } from '../../../scenario-variable-slice';
import { VarFunctionType } from '../../enum/variable-function-type';


interface ScriptSelectorProps {
   consumerVariable: VariableDto;
   blockVariable: VariableDto;  
   scenarioVariable: ScenarioVariableDto; 
   functionType: VarFunctionType;  
   availableVariables: VariableDto[];
}

export default function ScriptSelector({blockVariable, consumerVariable, scenarioVariable, functionType, availableVariables }: ScriptSelectorProps) {

    const {  updateScriptScenarioVariable } = useVariable();
    const [open, setOpen] = React.useState<boolean>(undefined);
    const [script, setScript] = React.useState<string>(scenarioVariable.script);
    const dispatch = useDispatch();

    function handleSave() {   
        const newScenarioVar : ScenarioVariableDto = updateScriptScenarioVariable(consumerVariable.id, script, blockVariable.id, functionType);
        handleOpen();
        dispatch(add(newScenarioVar));
    }

    function handleOpen () {
        setOpen(!open);
    }

    function editScript(e : any){
        setScript(e.target.value);
    }

    const createVarName = (variable : VariableDto) => {
        let varName = '(';
        if (variable.multiplicity === 'COLLECTION'){
            varName += '[' + variable.variableType.name + ']';
        } else {
            varName += variable.variableType.name;
        }
        varName += ') ' + variable.name;
        return varName;
    }


    return (
        <div className='container'>
            {!open &&
                <button onClick={handleOpen}>Script...</button>
            }
                        
            {open &&
                <div>
                    <button onClick={handleSave}>Save script...</button>
                    {availableVariables
                    .filter(curVar => curVar.id !== consumerVariable.id && curVar.id !== blockVariable.id)
                    .map((variable,index) => {
                        return (
                            <h3 key = {index}>{createVarName(variable)}</h3>
                        )
                    })}
                    <textarea value={script} style={{resize: 'both'}} name="script" onChange={editScript}></textarea>
                </div>
            }     
            <button onClick={() => dispatch(remove(scenarioVariable))}>X</button>
        </div>
    )

}