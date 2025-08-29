import React, { useState, useEffect } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';

import 'bootstrap/dist/css/bootstrap.min.css';

import { useSelector, useDispatch } from 'react-redux';
import { set, clear } from '../scenario-variable-slice';

import WorkflowVariable from './workflow-variable';

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

    const [inputVariables, setInputVariables] = React.useState([]);
    const [currentVariables, setCurrentVariables] = React.useState([]);
    const dispatch = useDispatch();
    const scenarioVariables = useSelector((state) => state.scenarioVariables.list);


    useEffect(() => {        
        axios.get('/api/v1/scenario/' + workflow + '/variables/exit',{ params: {
            projectId: getProjectId()
        }})
        .then((response) => {
            setInputVariables(response.data.inputVariables);
            setCurrentVariables(response.data.currentVariables);
            console.log("LOADED : ", response.data.scenarioVariables)
            dispatch(set(response.data.scenarioVariables));
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

    return(
        <Modal
            open={open}
            onClose={handleClose}
          >
            <Box sx={style}>
                <h2>{workflow.name}</h2>
                {currentVariables.map((currentVariable, index) => {
                    return(
                        <WorkflowVariable key={index} currentVariable = {currentVariable}  inputVariables={inputVariables}
                        currentVariables={currentVariables}/>
                    )
                })}
                <Button variant="outlined" onClick={save}>Save</Button>
            </Box>
          </Modal>
    )
}