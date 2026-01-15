import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import ValueTypeConstructor from './value-type-constructor';

import { useVariable } from './selectors/add-variable-hook';

import 'bootstrap/dist/css/bootstrap.min.css';
import { VariableDto,ScenarioVariableDto } from '../scenario-block-modal';

import {ValueType} from './value-type';
import ValueSelector from './selectors/value-selector';

import './sass/scenario-variable.sass';
import Divider from '@mui/material/Divider';

interface ScenarioVariableProps {
   currentVariable: VariableDto;
   acceptedVariables: VariableDto[];
   scenarioVariable?: ScenarioVariableDto;
}

/**
 * currentVariable - переменная блока
 * acceptedVariables - доступные для выбора переменные блока
 * scenarioVariable - переменная сценария относящаяся к этой переменной блока
 */
export default function ScenarioVariable({currentVariable, acceptedVariables, scenarioVariable}
    :ScenarioVariableProps){

    const [open, setOpen] = React.useState<boolean>(false);

    function createVarName(variable: VariableDto){
        let type;
        if (variable.multiplicity === 'COLLECTION'){
            type = 'Collection<' + variable.variableType.name + '>';
        }else{
            type = variable.variableType.name;
        }
        return (<div className='var-header' onClick={switchOpen}> <h4 className='var-type-text'>{scenarioVariable?.type} </h4>
         <h4 className='var-name'> {type + " : " + variable.name} </h4></div> );
    }   
    
    const switchOpen = () => {
        setOpen(!open);
    }

    return(
        <div>
            {createVarName(currentVariable)}
            
            {open &&
                <div className='var-body'>
                    <ValueSelector consumerVariable={currentVariable} blockVariable={currentVariable} acceptedVariables={acceptedVariables} parentId={0} 
                    scenarioVar={scenarioVariable}/>
                </div>
            }           
             
        </div>
    )
}