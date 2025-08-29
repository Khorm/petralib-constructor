import React, { useState, useEffect } from 'react'
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';
import './source-variable.sass';

import TypeSelector from '../type/type-selector';
import ScenarioVariable from '../scenario-var-class';
import SourceSelectedVariable from './source-selected-variable';

import { useSelector, useDispatch } from 'react-redux';
import { remove, add, removeAll } from '../scenario-variable-slice';
import { createSelector } from '@reduxjs/toolkit';
import { useReduxInitEffect } from '../hook';


//currentVariable - выбранная переменная из входящих переменных блока
//currentVariables - все текущие переменные
//inputVariables - все входящие переменные
export default function SourceVariable({currentVariable, currentVariables, inputVariables, removeVarFunc}){

    const SOURCE_OUT = 'SOURCE_OUT';
    const dispatch = useDispatch();

    const [acceptedSources, setAcceptedSources] = React.useState([]);
    const [selectedSource, setSelectedSource] = React.useState(undefined);

    const selectSourceVariables = createSelector(
            state => state.scenarioVariables.list,
            scenarioVariables => scenarioVariables
                .filter(variable => variable.sourceId === selectedSource?.id || variable.consumerVariableId === currentVariable.id)
                .map(data => new ScenarioVariable(data))
        )

    //сценарные переменные только для этого соурса
    const scenarioVariables = useSelector(selectSourceVariables);

    useReduxInitEffect(scenarioVariables, (initialData) => {
         axios.get('/api/v1/source/acceptedSources',{ params: {
            projectId: getProjectId(),
            multiplicity: currentVariable.multiplicity,
            returnTypeId: currentVariable.varType.id,
        }})
        .then((response) => {
            setAcceptedSources(response.data);
            const scenarioVariable = scenarioVariables.find(scVar => scVar.consumerVariableId === currentVariable.id);
            if (scenarioVariable){
                const choseSource = response.data.find(source => source.id === scenarioVariable.sourceId)
                setSelectedSource(choseSource);
            }
        }).catch((error) => {
            console.log(error);
            alert(error)
        })
    });


    function loadNewSource(sourceId){
        axios.get('/api/v1/source/' + sourceId,
            { params: {
                projectId: getProjectId(),
            }})
            .then((response) => {
                selectNewSource(response.data);
            }).catch((error) => {
                console.error(error);
                alert(error)
            })
    }


    function selectNewSource(source) {
        const outSourceVar = source.variables.find(variable => variable.pinType === 'OUT')
        if (outSourceVar === undefined){
            console.error("no source out variable found");
            return;
        }

        let tst = {
              type: SOURCE_OUT,
              consumerVariableId: currentVariable.id,
              producerVariableId: outSourceVar.id,
              sourceId: source.id
          }
        let newVal = new ScenarioVariable(tst);

        if (newVal.sourceId === undefined){
            console.error("no source found");
            return;
        }
        setSelectedSource(source);
        dispatch(add(newVal));
    }


    function removeScenarioVar(){
        dispatch(removeAll(scenarioVariables));
        removeVarFunc();
    }


    function createVarName(variable){
        let type;
        if (variable.multiplicity === 'COLLECTION'){
            type = 'Collection<' + variable.varType.name + '>';
        }else{
            type = variable.varType.name;
        }
        return (<h4> {type + " " + variable.name} </h4> );
    }

    //возвращает только входящие значения выбранного соурса
    function getOnlyEnterVariables(){
        return selectedSource?.variables.filter(variable => variable.pinType === 'IN');
    }

    function currentVariablesWithoutSelf(){
        return currentVariables.filter(curVar => curVar.id !== currentVariable.id);
    }



    return(
        <div className='source'>
            <div className='header'>
                <h3>Variable: {createVarName(currentVariable)} </h3>
                <h4>Selected source: {selectedSource?.name}</h4>
                <button onClick={removeScenarioVar}>Remove source</button>
            </div>
            {scenarioVariables?.length > 0 &&
                <div>
                    <h6>Source variables: </h6>
                    {getOnlyEnterVariables()?.map((variable, index) => {
                        return (
                        <div key={index} className='variable'>
                            <h4>{createVarName(variable)}</h4>
                            <SourceSelectedVariable currentSourceVariable={variable}
                            currentVariables={currentVariablesWithoutSelf()} inputVariables={inputVariables}
                            sourceId = {selectedSource?.id} />
                        </div>)
                    })}
                </div>
            }


            {selectedSource === undefined &&
                <select defaultValue='none' onChange={(e)=>loadNewSource(+e.target.value)}>
                    <option disabled hidden value='none'> -- select a source -- </option>
                    {acceptedSources.map((source, index) => {
                          return(
                             <option key={index} value={source.id}>{source.name}</option>
                          )
                      })}
                </select>
            }

        </div>
    )


}