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
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';

interface ScenarioVariableProps {
   currentVariable: VariableDto;
   acceptedVariables: VariableDto[];
   scenarioVariable?: ScenarioVariableDto;
   isLocalVariable: boolean;
   removeLocalVariable?: (localVariableId : number) => void;
}

/**
 * currentVariable - переменная блока
 * acceptedVariables - доступные для выбора переменные блока
 * scenarioVariable - переменная сценария относящаяся к этой переменной блока
 */
export default function ScenarioVariable({currentVariable, acceptedVariables,
     scenarioVariable, isLocalVariable, removeLocalVariable }
    :ScenarioVariableProps){

    const [open, setOpen] = React.useState<boolean>(false);

    /**
   * Формирует заголовок переменной с именем, типом и кнопкой удаления.
   * @returns {JSX.Element} Заголовок переменной.
   */
  const createVarName = (): JSX.Element => {
    const typeName = currentVariable.multiplicity === 'COLLECTION'
      ? `Collection<${currentVariable.variableType.name}>`
      : currentVariable.variableType.name;

    return (
      <div className="var-header" onClick={switchOpen}>
        <h4 className="var-type-text">{scenarioVariable?.type || '—'}</h4>
        <h4 className="var-name">{typeName} : {currentVariable.name}</h4>
        {isLocalVariable && removeLocalVariable && (
          <IconButton
            size="small"
            className="delete-button"
            onClick={(e) => {
              e.stopPropagation(); // Не раскрывать панель при клике
              removeLocalVariable(currentVariable.id);
            }}
            sx={{ ml: 'auto', color: 'error.main' }}
          >
            <DeleteIcon fontSize="small" />
          </IconButton>
        )}
      </div>
    );
  };
    
    const switchOpen = () => {
        setOpen(!open);
    }

    return(
        <div>
            {createVarName()}
            
            {open &&
                <div className='var-body'>
                    <ValueSelector
                     consumerVariable={currentVariable}
                     blockVariable={currentVariable}
                     acceptedVariables={acceptedVariables}
                     parentId={0} 
                     scenarioVar={scenarioVariable}
                />
                </div>
            }           
        <Divider sx={{ my: 1 }} />
        </div>
    )
}