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
   functionType: VarFunctionType  
   
}

export default function ScriptSelector({blockVariable, consumerVariable, scenarioVariable, functionType }: ScriptSelectorProps) {

    const {  updateScriptScenarioVariable } = useVariable();
    const [open, setOpen] = React.useState<boolean>(undefined);
    const [script, setScript] = React.useState<string>(scenarioVariable.script);
    const dispatch = useDispatch();

    function handleSave() {   
        updateScriptScenarioVariable(consumerVariable.id, script, blockVariable.id, functionType);
        handleOpen();
    }

    function handleOpen () {
        setOpen(!open);
    }

    function editScript(e : any){
        setScript(e.target.value);
    }


    return (
        <div className='container'>
            {!open &&
                <button onClick={handleOpen}>Script...</button>
            }
                        
            {open &&
                <div>
                    <button onClick={handleSave}>Save script...</button>
                    <textarea value={script} style={{resize: 'both'}} name="script" onChange={editScript}></textarea>
                </div>
            }     
            <button onClick={() => dispatch(remove(scenarioVariable))}>X</button>
        </div>
    )

}