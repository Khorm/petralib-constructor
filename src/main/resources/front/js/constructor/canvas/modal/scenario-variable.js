import React, { useState, useEffect, useRef } from 'react'
// import { useSelector, useDispatch } from 'react-redux';
// import { add } from './scenario-variable-slice';
// import { useReduxInitEffect } from './hook';

import 'bootstrap/dist/css/bootstrap.min.css';
import ValueSelector from './selector/value-selector';



export default function ScenarioVariable({currentVariable, inputVariables}){

    // const SIMPLE = 'SIMPLE';
    // const SCRIPT = 'SCRIPT';
    // const SOURCE = 'SOURCE_OUT';

    // const scenarioVariables = useSelector((state) => state.scenarioVariables.list);
    // const dispatch = useDispatch();

    // const [currentVarType, setCurrentVarType] = React.useState(undefined);


    // useReduxInitEffect(scenarioVariables, (initialData) => {
    //     const scenarioVar = scenarioVariables.filter(scVar => scVar.consumerVariableId === currentVariable.id && scVar.blockVariableId === currentVariable.id);
    //     if (!scenarioVar || scenarioVar.length === 0) return;

    //     if (scenarioVar[0].type === SIMPLE){
    //         setCurrentVarType(SIMPLE);
    //     }else if (scenarioVar[0].type === SCRIPT){
    //         setCurrentVarType(SCRIPT);
    //     }else if (scenarioVar[0].type === SOURCE){
    //         setCurrentVarType(SOURCE);
    //     }
    // });


    // function setVat(e){
    //     setCurrentVarType(e.target.value);
    // }

    function createVarName(variable){
        let type;
        if (variable.multiplicity === 'COLLECTION'){
            type = 'Collection<' + variable.variableType.name + '>';
        }else{
            type = variable.variableType.name;
        }
        return (<h4> {type + " : " + variable.name} </h4> );
    }

    return(
        <div>
            {createVarName(currentVariable)}
            <ValueSelector consumerVariable={currentVariable} blockVariableId={currentVariable.id} inputVariables={inputVariables} parentId={0} />
        </div>
    )
}