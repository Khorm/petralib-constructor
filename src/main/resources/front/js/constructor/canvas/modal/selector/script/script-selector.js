import React, { useState, useEffect } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css';

import { useVariable } from '../hooks/add-variable-hook';
import ScriptDropdown from './script-dropdown'


export default function ScriptSelector({ blockVariableId, consumerVariable, scenarioScript, localId, parentId }) {

    const { getScenarioVariable, updateScriptScenarioVariable, remove } = useVariable();
    const [open, setOpen] = React.useState(undefined);
    const [script, setScript] = React.useState('');

    useEffect(() => {
        if(scenarioScript !== undefined){
            setScript(scenarioScript);
        }
    }, []);


    // function scenarioVariable() {
    //     return getScenarioVariable(localId, blockVariableId);
    // }

    function handleSave() {
        // const producerVariableId = +e.target.value;
        // setSelectedProducerId(producerVariableId);
        updateScriptScenarioVariable(consumerVariable.id, script, blockVariableId, localId, parentId);
        handleOpen();
        // setDropdownObj(undefined);
    }

    function handleOpen () {
        setOpen(!open);
    }

    function editScript(e){
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
            <button onClick={() => remove(localId, blockVariableId)}>X</button>
        </div>
    )

}