import React, { useState, useEffect  } from 'react'
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

import TextField from '@mui/material/TextField';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import FormLabel from '@mui/material/FormLabel';
import RadioGroup from '@mui/material/RadioGroup';

import VariablesList from './variables-list';
import { Button } from '@mui/material';
import Alert from '@mui/material/Alert';


export default function Root() {

    const [name, setName] = useState('')
    const [nameValid, setNameValid] = useState(false)

    const [description, setDescription] = useState('')
    const [variables, setVariables] = useState([]);

    const [alerts, setAlerts] = useState([])

   useEffect(() => {
        if (!getWorkflowId()) return;

        axios.get('/api/v1/workflow/'+ getWorkflowId(),
        { params :{
            projectId: getProjectId()
        }})
            .then((response) => {
                console.log(response.data)
                setName(response.data.name);
                setDescription(response.data.description);
                setVariables(response.data.variableList);
                setNameValid(true);
            })
    }, [])


    function onNameChangeValidate(e){
        const text = e.target.value;
        setName(text);
        if (text.length == 0 || text.length > 100){
            setNameValid(false);
        }else{
            setNameValid(true);
        }
    }

    function handleInputSignalChange(e, id){
        let newSignals = []
        for (let i = 0; i < signals.length; i++) {
            if (signals[i].id === id){
                signals[i].isInput = !signals[i].isInput;
            }
            newSignals.push(signals[i])
        }
        setSignals(newSignals);
    }

    function addVariable(){
        let newVariables = [];
        for (let i = 0; i < variables.length; i++) {
            newVariables.push(variables[i]);
        }
        newVariables.push({});
        setVariables(newVariables);
    }

    function removeVariable(number){
        let newVariables = [];
        for (let i = 0; i < variables.length; i++) {
            if (i !== number){
                newVariables.push(variables[i]);
            }
        }
        setVariables(newVariables);
    }

    function editVariable(variable, number){
        let newVariables = [];
        for (let i = 0; i < variables.length; i++) {
            if (i !== number){
                newVariables.push(variables[i]);
            }else{
                newVariables.push(variable);
            }
        }
        setVariables(newVariables);
    }


    function deleteWorkflow(){
        axios.delete('/api/v1/workflow/'+ getWorkflowId(),
        { params :{
            projectId: getProjectId()
        }})
        .then((response) => {
            console.log(response.data)
        })
    }

    function saveWorkflow(){
        axios.post('/api/v1/workflow', {
            id: getWorkflowId(),
            name: name,
            projectId: getProjectId(),
            description: description,
            variableList: variables
        },{ params :{
          projectId: getProjectId()
        }})
        .then((response) => {
            console.log("OK: ",response);
            setAlerts([])
        })
        .catch(function (error) {
            console.log("ERR: ",error.response);
            if (Array.isArray(error.response.data)){
                console.log("VALIDATION ERROR: ",error.response.data);
                setAlerts(error.response.data)
            }
        });
    }

    return (
        <div>
            <h2> Workflow params </h2>
            {alerts.map((alert, index) => {
                return(
                    <Alert key={index} severity="error">{alert}</Alert>
                )
            })}

            <TextField
                fullWidth
                error={!nameValid}
                id="name-basic" label="Name" variant="outlined"
                value={name || ''}
                onChange={(e) => onNameChangeValidate(e)} />

            <TextField
                fullWidth
                id="standard-multiline-flexible"
                label="Description"
                multiline
                variant="standard"
                value={description || ''}
                onChange={(e) => setDescription(e.target.value)}
                 />

            <VariablesList variables={variables} addVariable={addVariable}
                                    removeVariable={removeVariable} editVariable={editVariable}/>

            <Button onClick={saveWorkflow}>Save</Button>
            <Button>Back</Button>
            {getWorkflowId() !== null && <Button onClick={deleteWorkflow}>Delete</Button> }

        </div>
    );
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<Root />);