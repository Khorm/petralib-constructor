import React, { useState, useEffect, useRef } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import 'bootstrap/dist/css/bootstrap.min.css';
import Autocomplete from '@mui/material/Autocomplete';

import VariableList from './variable/variable-list';

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
};

export default function BlockModal({block, open, handleClose, url}){

    const [name, setName] = React.useState('');
    const [variables, setVariables] = React.useState([]);
    const [services, setServices] = React.useState([]);
    const [service, setService] = React.useState({});
    const variableRefs = useRef([]);

    useEffect(() => {
            if (block.id !== undefined){
                setName(block.name);
                setVariables(block.variables);
                variableRefs.current = [...block.variables];
                setService(block.service);
            }
            axios.get('/api/v1/service',{ params: {
                projectId: getProjectId()
            }})
            .then((response) => {
                setServices(response.data)
            }).catch((error) => {
              alert(error)
           })
        }, [])

    function saveBlock(){
        let variables = [];
        variableRefs.current.forEach((childRef) => {
            if (childRef) {
                variables.push(childRef.variable.getVariable());
                console.log("NAME " + childRef.variable.getVariable().name);
            }
        });
        console.log("Saved variables:", variables);

        axios.post(url,{
            id: block.id,
            name: name,
            service: service,
            variables: variables
        },{
         params :{
            projectId: getProjectId()
        }}
        ).then((response) => {
             console.log("OK: ",response);
             handleClose();
         }).catch((error) => {
            console.log(error);
            alert(error)
         })
    }

    return(
        <Modal
            open={open}
            onClose={handleClose}
          >
            <Box sx={style}>
                <div><TextField value={name} onChange = {(e) => setName(e.target.value)} id="outlined-basic" label="Name" variant="outlined" /></div>
                <div style={{width: '30%'}}>
                    <Autocomplete
                        fullWidth
                        getOptionLabel={(option) => {if (option !== undefined && option.name !== undefined) return option.name; else return ''}}
                        value={service}
                        options={services}
                        onChange={(event, newVar) => setService(newVar) }
                      renderInput={(params) => <TextField {...params} label="Service" />}
                    />
                </div>
                <VariableList incomeVariables={variables} setVariables={setVariables} ref={variableRefs} />
                <Button variant="outlined" onClick={saveBlock}>Save</Button>
            </Box>
          </Modal>
    )
}