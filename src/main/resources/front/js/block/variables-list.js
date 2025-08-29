import React, { useState, useEffect  } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css'
import axios from 'axios'

import { Button } from '@mui/material';
import { Grid } from '@mui/material';
import { Item } from '@mui/material';

import Variable from './variable';

export default function VariablesList({variables, addVariable, removeVariable, editVariable}) {


    return (
        <div>
            <h2> Variables </h2>
             {variables.map((variable, index) => {
                return(
                    <Variable variable={variable} number={index} editVariable={editVariable} key={index}
                    removeVariable={removeVariable}/>
                )
            })}
            <Button onClick={addVariable}>Add</Button>
        </div>
    )
}