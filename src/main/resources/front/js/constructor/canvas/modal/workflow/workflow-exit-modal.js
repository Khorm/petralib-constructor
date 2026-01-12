import React, { useState, useEffect } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';

import 'bootstrap/dist/css/bootstrap.min.css';

import { useSelector, useDispatch } from 'react-redux';
import { set, clear } from '../scenario-variable-slice';
import  idGenerator  from '../variable/selectors/id-generator-hook'

import ScenarioVariable from '../variable/scenario-variable';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '50%',
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
  overflow: 'auto',
  maxHeight: '90%'
};

export default function WorkflowExitModal({workflow, open, handleClose}){

    const [allVariables, setAllVariables] = React.useState([]);
    const [currentVariables, setCurrentVariables] = React.useState([]);
    const dispatch = useDispatch();
    const scenarioVariables = useSelector((state) =>state.scenarioVariables.list);


    useEffect(() => {        
        axios.get('/api/v1/scenario/' + workflow + '/variables/exit',{ params: {
            projectId: getProjectId()
        }})
        .then((response) => {
            let variables = [];
            let localVariables = [];
            let scenarioValues = [];
            variables.push(...response.data.inputVariables);           
            console.log("LOADED : ", response.data)  
            response.data.currentVariables.forEach(element => {
                variables.push(element.variable);
                localVariables.push(element.variable);                
                scenarioValues.push(...element.scenarioVariables);                
                idGenerator.setId(element.variable.id, element.maxLocalId)
            });
            setAllVariables(variables);
            setCurrentVariables(localVariables)  
                               
            dispatch(set(scenarioValues));
        }).catch((error) => {
            console.error(error);
            alert(error.message)
       })
    }, [])



    function save() {
        axios.put('/api/v1/scenario/' + workflow + '/variables/exit',scenarioVariables,{ params: {
                projectId: getProjectId()
            }}
            ).then((response) => {
                console.log("OK: ",response);
                dispatch(clear());
                handleClose();
            }).catch((error) => {
                console.error(error);
                alert(error.message)
           })
    }

    function findDefaultLocalId(variableId){
        return scenarioVariables.find(scenarioVar => scenarioVar.blockVariableId === variableId && scenarioVar.parentId === 0)?.localId       
    }

    return(
        <Modal
            open={open}
            onClose={handleClose}
          >
            <Box sx={style}>
                <h2>Exit</h2>
                {currentVariables.map((currentVariable, index) => {
                    return(
                        <ScenarioVariable key={index} currentVariable = {currentVariable} inputVariables={allVariables}
                         defaultLocalId={findDefaultLocalId(currentVariable.id)}/>
                    )
                })}
                <Button variant="outlined" onClick={save}>Save</Button>
            </Box>
          </Modal>
    )
}