import React, { useState, useEffect } from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';

import { SIMPLE, SCRIPT, SOURCE} from './value-type';




export default function ValueTypeConstructor({setSelectedType}){ 
    const [selectedValue, setSelectedValue] = useState('none');


    function set(e){
        const selectedResult = e.target.value;
        setSelectedValue(selectedResult);
        setSelectedType(selectedResult);
    }

    return(
        <div>
            <select defaultValue="none" name="value-select" id="value-select" value={selectedValue} onChange={set}>
                <option disabled hidden value='none'> -- select a variable type -- </option>
                <option value={SIMPLE}>Simple</option>
                <option value={SOURCE}>Source</option>
                <option value={SCRIPT}>Script</option>
            </select>
        </div>
    )

}