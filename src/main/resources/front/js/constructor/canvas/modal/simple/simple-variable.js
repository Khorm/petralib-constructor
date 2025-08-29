import React, { useState, useEffect } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css';
import './simple-variable.sass';


import TypeSelector from '../type/type-selector';
import ScenarioVariable from '../scenario-var-class'

import { useSelector, useDispatch } from 'react-redux';
import { remove, add } from '../scenario-variable-slice';
import { createSelector } from '@reduxjs/toolkit';


export default function SimpleVariable({currentVariable, currentVariables, inputVariables, removeVar}){

    const SIMPLE = 'SIMPLE';

    const dispatch = useDispatch();
    const selectCurrentVariable = createSelector(
            state => state.scenarioVariables.list,
            scenarioVariables => scenarioVariables.filter(variable => variable.consumerVariableId === currentVariable.id).map(data => new ScenarioVariable(data))
    )
    //сценарные переменные только для этого скрипта
    const scenarioVariables = useSelector(selectCurrentVariable);


    function handleChange (e) {
        let newVal = createVariable(+e.target.value);
        dispatch(add(newVal));
    }

    function createVariable(producerId){
        let tst = {
              type: SIMPLE,
              consumerVariableId: currentVariable.id,
              producerVariableId: producerId,
          }
        return new ScenarioVariable(tst);
    }

    function setTypeCollection(typeCollection) {
        let currentVar = scenarioVariable();
        currentVar.typeInheritance = typeCollection;
        dispatch(add(currentVar));
    }

    function removeSimpleVar(){
        const removedValue = scenarioVariable();
        dispatch(remove(removedValue));
        removeVar();
    }

    function scenarioVariable(){
        if (scenarioVariables.length > 0){
            return scenarioVariables[0];
        }
        return undefined;
    }

    function producerVariable(){
        return currentVariables.concat(inputVariables).find(variable => variable.id === scenarioVariable()?.producerVariableId);
    }

    function currentVariablesWithoutSelf(){
        return currentVariables.filter(curVar => curVar.id !== currentVariable.id);
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

    function getDefaultVariable(){
        const cur = producerVariable();
        if (cur){
            return cur.id+'';
        }else{
            return 'none';
        }
    }


    return(
        <div className='container'>
            <h3>Variable - {createVarName(currentVariable)}</h3>
            <button onClick={removeSimpleVar}>X</button>
            <select defaultValue={getDefaultVariable()} onChange={handleChange}>
                <option disabled hidden value='none'> -- select a variable -- </option>
                <optgroup label="Input variables">
                  {inputVariables.map((inpVariable, index) => {
                      return(
                         <option value={inpVariable.id}>{inpVariable.name}</option>
                      )
                  })}
                </optgroup>

                <optgroup label="Current variables">
                  {currentVariablesWithoutSelf().map((curVariable, index) => {
                      return(
                         <option key={index} value={curVariable.id}>{curVariable.name}</option>
                      )
                  })}
                </optgroup>
            </select>
                {scenarioVariable() !== undefined && producerVariable() !== undefined && producerVariable().multiplicity !== 'COLLECTION' &&
                                <TypeSelector baseTypeId={producerVariable().varType.id}
                                             typeArray={scenarioVariable().typeInheritance} setNewTypesArray={setTypeCollection} />
                }
        </div>
    )




}