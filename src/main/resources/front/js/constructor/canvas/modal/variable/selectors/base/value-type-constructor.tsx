import React, { useState, useEffect } from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';

import {ValueType} from '../../enum/value-type';
import { VarFunctionType } from '../../enum/variable-function-type';


interface ValueTypeConstructorProps {
  /**
   * Обратный вызов, срабатывающий при изменении выбранного типа значения.
   * @param type - Выбранный тип значения.
   */
  setSelectedType: (type: ValueType) => void;
  functionType: VarFunctionType;
}
export default function ValueTypeConstructor({setSelectedType, functionType} :
     ValueTypeConstructorProps){ 


    const [selectedValue, setSelectedValue] = useState('none');


    function set(e: React.ChangeEvent<HTMLSelectElement>){
        const selectedResult = e.target.value as 'SIMPLE' | 'SCRIPT' | 'SOURCE';
        setSelectedValue(selectedResult);
        setSelectedType(selectedResult);
    }


    return(
        <div>
            <select defaultValue="none" name="value-select" id="value-select" value={selectedValue} onChange={set}>
                <option disabled hidden value='none'> -- select a variable type -- </option>
                <option value={'SIMPLE'}>Simple</option>
                {functionType === 'FUNCTION' || functionType === 'CONSUMER' && 
                    <option value={'SOURCE'}>Source</option>
                }
                {functionType === 'FUNCTION' || functionType === 'CONSUMER' &&
                    <option value={'SCRIPT'}>Script</option>
                }
                
            </select>
        </div>
    )

}