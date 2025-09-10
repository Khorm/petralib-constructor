import React, { useState, useEffect } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';

import 'bootstrap/dist/css/bootstrap.min.css';

import { useSelector, useDispatch } from 'react-redux';
import { set, clear } from './scenario-variable-slice';
import { add } from './selector/id/local-id-slice';

import ScenarioVariable from './scenario-variable';

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

export default function ScenarioBlockModal({scenarioBlock, workflow, open, handleClose}){

    const [allVariables, setAllVariables] = React.useState([]);
    const [currentVariables, setCurrentVariables] = React.useState([]);
    const dispatch = useDispatch();
    const scenarioVariables = useSelector((state) =>state.scenarioVariables.list);
    


    useEffect(() => {
        console.log('scenarioBlock', scenarioBlock)
        axios.get('/api/v1/scenario/' + scenarioBlock.id + '/variables',{ params: {
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
                dispatch(add({id:element.variable.id, localId: element.maxLocalId}));
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
        
        axios.post('/api/v1/scenario/' + scenarioBlock.id + '/variables',scenarioVariables,{ params: {
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
                <h2>{scenarioBlock.name}</h2>
                {currentVariables.map((currentVariable, index) => {
                    return(
                        <ScenarioVariable key={index} currentVariable = {currentVariable} inputVariables={allVariables}/>
                    )
                })}
                <Button variant="outlined" onClick={save}>Save</Button>
            </Box>
          </Modal>
    )
}