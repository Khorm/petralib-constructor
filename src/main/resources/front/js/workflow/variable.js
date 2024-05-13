import React, { useState, useEffect  } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css'

import axios from 'axios'

import { Button } from '@mui/material';
import { Grid } from '@mui/material';
import { Item } from '@mui/material';
import TextField from '@mui/material/TextField';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';


export default function Variable({variable, number, editVariable, removeVariable}) {

    const [nameValid, setNameValid] = useState(true)
    const [typeValid, setTypeValid] = useState(true)
    const [multiplicityValid, setMultiplicityValid] = useState(true)


    function onNameChangeValidate(e){
        const text = e.target.value;
        let cloneVariable = Object.assign({}, variable);
        cloneVariable.name = text;
        console.log('editVariable', editVariable);
        editVariable(cloneVariable,number);
    }

    useEffect(() => {
            // Этот блок кода будет выполнен после каждого изменения пропс myProp
            console.log('Пропс myProp изменился:', variable);
            if (!variable.name){
                setNameValid(false);
            }else if (variable.name.length > 100){
                setNameValid(false);
            }else{
                setNameValid(true);
            }

            if (!variable.type){
                setTypeValid(false);
            }else if ( variable.type.length > 100){
                setTypeValid(false);
            }
            else{
                setTypeValid(true);
            }

            if (!variable.multiplicity){
                setMultiplicityValid(false);
            }else{
                setMultiplicityValid(true);
            }
    }, [variable]); // Указываем зависимость myProp, чтобы useEffect срабатывал при изменении этой пропс

    function onTypeChangeValidate(e){
        const text = e.target.value;
        let cloneVariable = Object.assign({}, variable);
        cloneVariable.type = text;
        editVariable(cloneVariable,number);
    }

    function onDescrChange(e){
        const text = e.target.value;
        let cloneVariable = Object.assign({}, variable);
        cloneVariable.description = text;
        editVariable(cloneVariable,number);
    }

    function onMultiplicityChangeValue(e){
        const text = e.target.value;
        let cloneVariable = Object.assign({}, variable);
        cloneVariable.multiplicity = text;
        editVariable(cloneVariable,number);
    }

    return (
        <div>
            <TextField
                        error={!nameValid}
                        id="name-basic" label="Name" variant="outlined"
                        value={variable.name || ''}
                        onChange={(e) => onNameChangeValidate(e)} />

            <TextField
                        error={!typeValid}
                        id="type-basic" label="Type" variant="outlined"
                        value={variable.type || ''}
                        onChange={(e) => onTypeChangeValidate(e)} />
            <FormControl>
                <InputLabel id="demo-simple-select-label">Multiplicity</InputLabel>
                <Select
                        labelId="multiplicity-select-label"
                        id="multiplicity-select"
                        label="Multiplicity"
                        value={variable.multiplicity || ''}
                        onChange={(e) => onMultiplicityChangeValue(e)}
                        error={!multiplicityValid}
                  >
                    <MenuItem value={'SINGLE'}>Single</MenuItem>
                    <MenuItem value={'COLLECTION'}>Collection</MenuItem>
                </Select>
            </FormControl>
            <TextField
                        id="standard-multiline-flexible"
                        label="Description"
                        multiline
                        variant="standard"
                        value={variable.description || ''}
                        onChange={(e) => onDescrChange(e)}
                         />
            <Button onClick={() => removeVariable(number)}>Remove</Button>
        </div>
    )
}