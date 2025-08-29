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


export default function Root() {

    const [name, setName] = useState('')
    const [nameValid, setNameValid] = useState(false)

    const [description, setDescription] = useState('')

    const [path, setPath] = useState('')
    const [pathValid, setPathValid] = useState(false)

    const [alerts, setAlerts] = useState([])

   useEffect(() => {
        if (!getServiceId()) return;

        axios.get('/api/v1/service/'+ getServiceId(),
        { params :{
            projectId: getProjectId()
        }})
            .then((response) => {
                console.log(response.data)
                setName(response.data.name);
                setDescription(response.data.description);
                setPath(response.data.path)
                setNameValid(true);
                setPathValid(true);
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

    function onPathChangeValidate(e){
        const text = e.target.value;
        setPath(text);
        if (text.length == 0 || text.length > 100){
            setPathValid(false);
        }else{
            setPathValid(true);
        }
    }

    function deleteService(){
        axios.delete('/api/v1/service/'+ getServiceId(),
        { params :{
            projectId: getProjectId()
        }})
        .then((response) => {
            console.log(response.data)
            openConstructor();
        })
    }

    function saveService(){
        axios.post('/api/v1/service', {
            id: getServiceId(),
            name: name,
            projectId: getProjectId(),
            description: description,
            path: path
        },{ params :{
          projectId: getProjectId()
        }})
        .then((response) => {
            setAlerts([])
            openConstructor();
        })
        .catch(function (error) {
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
            <h2> Service params </h2>
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
                error={!pathValid}
                id="path-basic" label="Path" variant="outlined"
                value={path || ''}
                onChange={(e) => onPathChangeValidate(e)} />

            <TextField
                fullWidth
                id="standard-multiline-flexible"
                label="Description"
                multiline
                variant="standard"
                value={description || ''}
                onChange={(e) => setDescription(e.target.value)}
                 />

            <Button onClick={saveService}>Save</Button>
            <Button onClick={openConstructor}>Back</Button>
            {getServiceId() !== null && <Button onClick={deleteService}>Delete</Button> }

        </div>
    );
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<Root />);