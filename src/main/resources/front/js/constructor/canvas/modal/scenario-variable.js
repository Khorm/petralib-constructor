import React, { useState, useEffect, useRef } from 'react'
// import { useSelector, useDispatch } from 'react-redux';
// import { add } from './scenario-variable-slice';
// import { useReduxInitEffect } from './hook';

import 'bootstrap/dist/css/bootstrap.min.css';
import ValueSelector from './selector/value-selector';



export default function ScenarioVariable({currentVariable, inputVariables, defaultLocalId}){

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
            <ValueSelector consumerVariable={currentVariable} blockVariableId={currentVariable.id} inputVariables={inputVariables} parentId={0} 
            defaultLocalId={defaultLocalId}/>
        </div>
    )
}