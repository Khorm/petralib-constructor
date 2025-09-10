import React, { useState, useEffect ,forwardRef } from 'react';
import axios from 'axios';

import IconButton from '@mui/material/IconButton';
import AddIcon from '@mui/icons-material/Add';


import 'bootstrap/dist/css/bootstrap.min.css';
import './variable.sass';

import Variable from './variable';



const VariableList = forwardRef(({incomeVariables, setVariables, pinAccepted = true}, ref) => {

    const [types, setTypes] = React.useState([]);

    useEffect(() => {
        axios.get('/api/v1/type',{ params :{
                projectId: getProjectId()
            }}
            ).then((response) => {
                 setTypes(response.data)
             }).catch((error) => {
                alert(error)
             })
        }, [])




    function addNewVariable(){
        let newVars = [...incomeVariables];
        let newVar = {variable: {name: 'newVar ' + incomeVariables.length}};
        newVars.push(newVar);
        ref.current.push(newVar);
        setVariables(newVars);
        console.log("ADD VAR",ref)
    }

    function removeVariable(index){
        let newVars = [];
        console.log("REMOVE",index)
        for (let i = 0; i<incomeVariables.length; i++){
            console.log("check ",ref.current[i])
            if (i !== index){
                newVars.push(ref.current[i].variable.getVariable())
            }
        }
        ref.current = newVars;
        console.log("END ",ref.current)
        setVariables(newVars);
    }


    return(
        <div className='variable-list-div'>

            <IconButton aria-label="add" onClick={addNewVariable}>
                <AddIcon />
            </IconButton>

                {incomeVariables.map((variable, index) => {
                    return(
                        <Variable key={index} index={index} variable = {variable} 
                        types = {types} removeVar={removeVariable} ref={ref.current[index]} pinAccepted={pinAccepted}/>
                    )
                })}
        </div>
    )
});

export default VariableList;