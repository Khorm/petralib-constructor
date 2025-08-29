import React, { useState, useEffect } from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import './source-selected-variable';

import TypeSelector from '../type/type-selector';
import ScenarioVariable from '../scenario-var-class'

import { useSelector, useDispatch } from 'react-redux';
import { remove, add, removeAll } from '../scenario-variable-slice';
import { createSelector } from '@reduxjs/toolkit';



//currentSourceVariable - текущая входящая переменная соурса
//currentVariables - все текущие переменные
//inputVariables - все входящие переменные
//sourceId - текущий выбраный соурс
export default function SourceSelectedVariable({currentSourceVariable, currentVariables, inputVariables, sourceId}){


    const dispatch = useDispatch();
        const selectSourceVariables = createSelector(
                state => state.scenarioVariables.list,
                scenarioVariables => scenarioVariables
                    .filter(variable => variable.sourceId === sourceId && variable.consumerVariableId === currentSourceVariable.id)
                    .map(data => new ScenarioVariable(data))
            )

        //сценарные переменные только для этого соурса
    const scenarioVariables = useSelector(selectSourceVariables);

    const SOURCE_IN = 'SOURCE_IN';



    function addNewTypeArrayToScenarioVariable(newTypeArr){
        let newScenarioSourceVariable = scenarioSourceVariable().getData();
        newScenarioSourceVariable.typeInheritance = newTypeArr;
        console.log("typeInheritance",  newScenarioSourceVariable);
        dispatch(add(new ScenarioVariable(newScenarioSourceVariable)));
    }

    function selectNewSourceVariable(e, sourceVarId) {
        const producerVarId = +e.target.value;
        const tst = {
              type: SOURCE_IN,
              consumerVariableId: sourceVarId,
              producerVariableId: producerVarId,
              sourceId: sourceId,

          }
        const newVal = new ScenarioVariable(tst);

        if (newVal.sourceId === undefined){
            console.error("no source find");
            return;
        }

        console.log("newVal : ", newVal.getData());
        dispatch(add(newVal));
    }

    function scenarioSourceVariable(){
        return scenarioVariables.find(scVar => scVar.consumerVariableId === currentSourceVariable.id)
    }

    function producerValue(){
        return currentVariables.concat(inputVariables).find(variable => variable.id === scenarioSourceVariable()?.producerVariableId);
    }

    function show(){
        const scenarioVar = scenarioSourceVariable();
        const producerVar = producerValue();
        console.log("scenarioSourceVariable", scenarioVar);
        console.log("producerValue", producerValue());

        scenarioVariables.forEach(scVAr => console.log("scenarioVariables ", scVAr.getData()))
        console.log("currentSourceVariable", currentSourceVariable);
        if (scenarioVar === undefined || producerVar === undefined){
            return false;
        }

        return producerValue().multiplicity !== 'COLLECTION';
    }

    function selectDefaultValue(){
        const scenarioVar = scenarioSourceVariable();
        if (!scenarioVar) return 'none';

        const producerVar = currentVariables.concat(inputVariables).find(variable => variable.id === scenarioVar.producerVariableId);
        if (!producerVar) return 'none';

        return producerVar.id+'';
    }



    return(
        <div className='source-selected-container'>
        <select defaultValue={selectDefaultValue()} onChange={(e) => selectNewSourceVariable(e, currentSourceVariable.id)}>
            <option disabled hidden value='none'> - select a producer variable - </option>
            <optgroup label="Input variables">
              {inputVariables.map((inpVariable, index) => {
                  return(
                    <div key={index}>
                     <option value={inpVariable.id}>{inpVariable.name}</option>
                    </div>
                  )
              })}
            </optgroup>

            <optgroup label="Current variables">
              {currentVariables.map((curVariable, index) => {
                  return(
                    <div key={index}>
                     <option value={curVariable.id}>{curVariable.name}</option>
                    </div>
                  )
              })}
            </optgroup>
        </select>

        {show() &&
            <TypeSelector baseTypeId={producerValue().varType.id}
                         typeArray={scenarioSourceVariable().typeInheritance}
                          setNewTypesArray={addNewTypeArrayToScenarioVariable} />
        }
        </div>
    )


}