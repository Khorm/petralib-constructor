import React, { useState, useEffect } from 'react'
import axios from 'axios';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';

import 'bootstrap/dist/css/bootstrap.min.css';

import TypeSelector from '../../type/type-selector';
import ValueSelector from '../value-selector';
import { useVariable } from '../hooks/add-variable-hook';




export default function SourceSelector({ blockVariableId, consumerVariable, inputVariables, localId, parentId }) {

    const { getScenarioVariable, updateSourceScenarioVar, updateScenarioVarTypeInheritance,findDefaultLocalId, remove } = useVariable();

    const [selectedSource, setSelectedSource] = useState(undefined);
    const [loadedSources, setLoadedSources] = useState([]);

    useEffect(() => {
       
       axios.get('/api/v1/block/source/acceptedSources',{ params: {
            projectId: getProjectId(),            
        }})
        .then((response) => {
            setLoadedSources(response.data);
            const sourceId  = scenarioVariable().producerId;
            const defaultSource = response.data.find(source=> source.id === sourceId);            
            if (defaultSource !== undefined){
                console.log("DEFAULT SOURCE : ", defaultSource, sourceId);
                setSelectedSource(defaultSource);
            }
        }).catch((error) => {
            console.log(error);
            alert(error)
        })
    }, []);

    function scenarioVariable() {
        return getScenarioVariable(localId, blockVariableId);
    }

    function handleChange(e, source) {   
        console.log("SET SOURCE : " , source);     
        setSelectedSource(source);
        updateSourceScenarioVar(consumerVariable.id, source.id, blockVariableId, localId, parentId);
    }


    function setTypeCollection(typeCollection) {
        updateScenarioVarTypeInheritance(localId, typeCollection, blockVariableId);
    }


    function getProducerSourceOutVariable(){
        return selectedSource.variables.find(sourceVar => sourceVar.pinType === 'OUT')   
        
    }

    function getProducerSourceInVariables(){
        return selectedSource.variables.filter(sourceVar => sourceVar.pinType === 'IN')
    }

    // function loadSources(){
    //     axios.get('/api/v1/block/source/acceptedSources',{ params: {
    //         projectId: getProjectId(),            
    //     }})
    //     .then((response) => {
    //         setLoadedSources(response.data);            
    //     }).catch((error) => {
    //         console.log(error);
    //         alert(error)
    //     })
    // }

                // onOpen={() => {
                //     loadSources();
                // }}
                // 


    return (
        <div className='container'>
            <h6>{selectedSource?.name} </h6>
            {selectedSource === undefined &&
                <Autocomplete
                    disablePortal
                    disableClearable
                    getOptionLabel={(source) => source?.name}
                    value={selectedSource}  
                    inputValue={selectedSource}                  
                    options={loadedSources}
                    onChange={handleChange}
                    renderInput={(params) => <TextField {...params} label="Select source ... " />}
                />
            }
            
            {selectedSource !== undefined && getProducerSourceOutVariable().multiplicity !== 'COLLECTION' && scenarioVariable() !== undefined &&
                <TypeSelector baseTypeId={getProducerSourceOutVariable().variableType.id}
                    typeArray={scenarioVariable().typeInheritance} setNewTypesArray={setTypeCollection} />
            }
            {selectedSource !== undefined && 
                <div>                    
                {getProducerSourceInVariables().map((sourceVar, index)=> {
                    return(
                        <div key={index}> 
                            {sourceVar.name}
                            <ValueSelector consumerVariable={sourceVar} blockVariableId={blockVariableId} inputVariables={inputVariables} 
                            parentId={localId} defaultLocalId={findDefaultLocalId(blockVariableId, localId, sourceVar.id)}/>
                        </div>
                    )
                })}
                </div>
            }
            <button onClick={() => remove(localId, blockVariableId)}>X</button>
        </div>
    )

}