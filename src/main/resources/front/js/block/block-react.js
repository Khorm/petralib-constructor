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
import { Button } from '@mui/material';
import Alert from '@mui/material/Alert';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';

import VariablesList from './variables-list';



export default function Root() {

    const [name, setName] = useState('')
    const [nameValid, setNameValid] = useState(false)

    const [description, setDescription] = useState('')
    const [variables, setVariables] = useState([]);

    const [service, setService] = useState([])
    const [serviceValid, setServiceValid] = useState(null)
    const [services, setServices] = useState([]);

    const [alerts, setAlerts] = useState([])

   useEffect(() => {
        axios.get('/api/v1/service',
           { params :{
               projectId: getProjectId()
           }})
        .then((response) => {
           console.log(response.data)
           setServices(response.data);
        })
    }, [])

     useEffect(() => {

            if (!getBlockId()) return;

            axios.get('/api/v1/block/'+ getBlockId(),
                { params :{
                    projectId: getProjectId()
                }})
            .then((response) => {
                console.log(response.data)
                setName(response.data.name);
                setNameValid(true);
                setDescription(response.data.description);
                setVariables(response.data.variableList);
                for (let i = 0; i < services.length; i++){
                    if (services[i].id === response.data.serviceId){
                        setService(services[i]);
                        setServiceValid(true);
                        break;
                    }
                }

            })
        }, [services])


    function onNameChangeValidate(e){
        const text = e.target.value;
        setName(text);
        if (text.length == 0 || text.length > 100){
            setNameValid(false);
        }else{
            setNameValid(true);
        }
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


    function deleteBlock(){
        axios.delete('/api/v1/block/'+ getBlockId(),
        { params :{
            projectId: getProjectId()
        }})
        .then((response) => {
            openConstructor();
            console.log(response.data)
        })
    }

    function onServiceChangeValue(e){
        for (let i = 0; i < services.length; i++){
            if (services[i].id === e.target.value){
                console.log(services[i])
                setService(services[i]);
                setServiceValid(true);
                break;
            }
        }
    }


    function saveBlock(){
        console.log(service);
        axios.post('/api/v1/block', {
            id: getBlockId(),
            name: name,
            projectId: getProjectId(),
            description: description,
            variableList: variables,
            serviceId: service.id
        },{ params :{
          projectId: getProjectId(),
          blockType: getBlockType()
        }})
        .then((response) => {
            console.log("OK: ",response);
            setAlerts([])
            openConstructor();
        })
        .catch(function (error) {
            console.log("ERR: ",error.response);
            if (Array.isArray(error.response.data)){
                console.log("VALIDATION ERROR: ",error.response.data);
                setAlerts(error.response.data)
            }
        });
    }

    function openConstructor(){
        window.location.href = '/projects/' + getProjectId() + '/constructor';
    }

    return (
        <div>
            <h2> Block params </h2>
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
            <div>
            <FormControl style={{width:'100%'}}>
                <InputLabel id="service-select-label">Service</InputLabel>
                <Select
                        fullWidth
                        labelId="select-label"
                        id="service-select"
                        label="Service"
                        value={service.id || ''}
                        onChange={(e) => onServiceChangeValue(e)}
                        error={!serviceValid}
                  >
                  {services.map((service, index) => {
                      return(
                          <MenuItem value={service.id} key={index}>{service.name}</MenuItem>
                      )
                  })}
                </Select>
            </FormControl>
            </div>

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

            <Button onClick={saveBlock}>Save</Button>
            <Button onClick={openConstructor}>Back</Button>
            {getBlockId() !== null && <Button onClick={deleteBlock}>Delete</Button> }

        </div>
    );
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<Root />);