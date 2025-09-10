import React, { useState, useEffect } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css';

import TypeSelector from '../type/type-selector';
import ScenarioVariable from '../scenario-var-class'

import { useSelector, useDispatch } from 'react-redux';
import { remove, add } from '../scenario-variable-slice';
import { createSelector } from '@reduxjs/toolkit';
import { useReduxInitEffect } from '../hook';


export default function ScriptVariable({currentVariable, removeVar}){

    const SCRIPT = 'SCRIPT';
    const dispatch = useDispatch();
    const selectCurrentVariable = createSelector(
            state => state.scenarioVariables.list,
            scenarioVariables => scenarioVariables.filter(variable => variable.consumerVariableId === currentVariable.id).map(data => new ScenarioVariable(data))
        )

    //��������� ���������� ������ ��� ����� �������
    const scenarioVariables = useSelector(selectCurrentVariable);

    useReduxInitEffect(scenarioVariables, (initialData) => {
       if(scenarioVariable() === undefined){
           const newVar = createVariable();
           dispatch(add(newVar));
       }
    });


    function removeSimpleVar(){
        dispatch(remove(scenarioVariable()));
        removeVar();
    }

    function changeScript(e){
        const script = e.target.value;
        let newSceneVar = scenarioVariable();
        if (!newSceneVar){
            newSceneVar = createVariable();
        }
        newSceneVar.script = script;
        dispatch(add(newSceneVar));
    }


    function createVariable(){
        let tst = {
              type: SCRIPT,
              consumerVariableId: currentVariable.id,
          }
        return new ScenarioVariable(tst);
    }

    function scenarioVariable(){
        if (scenarioVariables.length > 0){
            return scenarioVariables[0];
        }
        return undefined;
    }

    function createVarName(variable){
        let type;
        if (variable.multiplicity === 'COLLECTION'){
            type = 'Collection<' + variable.fieldType.name + '>';
        }else{
            type = variable.fieldType.name;
        }
        return (<h4> {type + " : " + variable.name} </h4> );
    }

    return(
        <div>
            <h3>Variable - {createVarName(currentVariable)}</h3>
            <button onClick={removeSimpleVar}>X</button>
            <textarea style={{width: '100%'}} onChange={changeScript} value={scenarioVariable()?.script} />
        </div>
    )


}