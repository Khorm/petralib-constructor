import React, { useState, useEffect } from 'react'
import axios from 'axios';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';

import 'bootstrap/dist/css/bootstrap.min.css';

import TypeSelector, { CTypeFieldDto, CTypeShortDto } from '../../type/type-selector';
import ValueSelector from '../value-selector';
import { useVariable } from '../add-variable-hook';
import { VariableDto, ScenarioVariableDto } from '../../../scenario-block-modal';
import SimpleSelector from '../simple/simple-selector';

import { add, remove } from '../../../scenario-variable-slice';
import { useDispatch, useSelector } from 'react-redux';
import Divider from '@mui/material/Divider';

import './source-selector-type.sass'

interface SourceSelectorProps {
   consumerVariable: VariableDto;
   blockVariable: VariableDto;
   acceptedVariables: VariableDto[],
   parentId: number;
   scenarioVariable: ScenarioVariableDto;
   localId?: number;
}

interface Source {
    id: number;
    name: string;
    description: string;
    variables: VariableDto[];
}


export default function SourceSelector({blockVariable, consumerVariable, acceptedVariables, scenarioVariable, localId, parentId }
    :SourceSelectorProps
) {

    const dispatch = useDispatch();
    const {  updateScenarioVarTypeInheritance, updateSourceScenarioVar} = useVariable();

    const [selectedSource, setSelectedSource] = React.useState<Source>(undefined);
    const [loadedSources, setLoadedSources] = React.useState<Source[]>([]);

    const scenarioVariables = useSelector((state: any) => state.scenarioVariables.list);

    useEffect(() => {
       
       axios.get<Source[]>('/api/v1/block/source/acceptedSources',{ params: {
        // @ts-ignore
            projectId: getProjectId(),            
        }})
        .then((response) => {
            setLoadedSources(response.data);
            const sourceId : number  = scenarioVariable.producerId;
            const defaultSource :Source = response.data.find(source=> source.id === sourceId);            
            if (defaultSource !== undefined){
                console.log("DEFAULT SOURCE : ", defaultSource, sourceId);
                setSelectedSource(defaultSource);
            }
        }).catch((error) => {
            console.log(error);
            alert(error)
        })
    }, []);

    function handleChange(e: React.ChangeEvent<HTMLSelectElement>, source: Source) {   
        console.log("SET SOURCE : " , source);     
        setSelectedSource(source);

        const newScenarioVar : ScenarioVariableDto = updateSourceScenarioVar(consumerVariable.id, source.id, blockVariable.id, localId, parentId);
        dispatch(add(newScenarioVar));
    }


    function setTypeCollection(typeCollection: CTypeFieldDto[]) {
        const newScenarioVar : ScenarioVariableDto = updateScenarioVarTypeInheritance(localId, typeCollection, blockVariable.id)
        dispatch(add(newScenarioVar));
    }


    function getProducerSourceOutVariable(){
        return selectedSource?.variables.find(sourceVar => sourceVar.pinType === 'OUT')   
        
    }

    function getProducerSourceInVariables(){
        return selectedSource?.variables.filter(sourceVar => sourceVar.pinType === 'IN')
    }

    // const getScenarioVarForSourceVar = (sourceVar : VariableDto) : ScenarioVariableDto  => {
    //     return scenarioVariables.find((scenarioVar: ScenarioVariableDto) => {
    //         if (scenarioVar.consumerVariableId === sourceVar.id && scenarioVar.blockVariableId === blockVariable.id) {
    //             return scenarioVar;
    //         }
    //     });             
    // }

    const getSourceName = () : string => {
        let sourceName = '';
        const outVar : VariableDto = getProducerSourceOutVariable();
        if (selectedSource !== undefined){
            if(outVar.multiplicity === 'COLLECTION'){
                sourceName = 'Collection <';
            }
            sourceName+= outVar.variableType.name;
            if(outVar.multiplicity === 'COLLECTION'){
                sourceName+= '>';
            }
            sourceName+= ' : ' + selectedSource.name;
            return sourceName;
        }        
    }

    const getSourceVariableName = (sourceVar : VariableDto) : string => {
        let sourceVarName = '';
        if (sourceVar.multiplicity === 'COLLECTION'){
            sourceVarName += 'Collection <';
        }
        sourceVarName += sourceVar.variableType.name;
        if (sourceVar.multiplicity === 'COLLECTION'){
            sourceVarName += '>';
        }
        sourceVarName += ' : ' + sourceVar.name;
        return sourceVarName;
    }

    return (
        <div>
            <h4>{getSourceName()} </h4>
            {selectedSource === undefined &&
                <Autocomplete
                    disablePortal
                    disableClearable
                    getOptionLabel={(source) => source?.name}
                    value={selectedSource}  
                    inputValue={selectedSource?.name}                  
                    options={loadedSources}
                    onChange={handleChange}
                    style={{width: 'inherit'}}
                    renderInput={(params) => <TextField {...params} label="Select source ... " />}
                />
            }
            
            {selectedSource !== undefined && getProducerSourceOutVariable().multiplicity !== 'COLLECTION' && scenarioVariable !== undefined &&
                <TypeSelector ownerTypeId={getProducerSourceOutVariable().variableType.id}
                    typeArray={scenarioVariable.typeInheritance} setNewTypesArray={setTypeCollection} />
            }
            {selectedSource !== undefined && 
                <div className='source-variables'>                    
                {getProducerSourceInVariables().map((sourceVar, index)=> {
                    return(
                        <div>
                        <div className='source-variable' key={index}> 
                            {getSourceVariableName(sourceVar)}
                            <SimpleSelector consumerVariable={sourceVar} blockVariable={blockVariable}
                            acceptedVariables={acceptedVariables} parentId={localId}  />
                        </div>
                        <Divider />
                        </div>
                    )
                })}
                </div>
            }
            <button onClick={() => remove(scenarioVariable)}>X</button>
        </div>
    )

    // <ValueSelector consumerVariable={sourceVar} blockVariableId={blockVariableId} inputVariables={inputVariables} 
    //                         parentId={localId} defaultLocalId={findDefaultLocalId(blockVariableId, localId, sourceVar.id)}/>

}