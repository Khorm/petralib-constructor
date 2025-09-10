import React, { useState, useEffect , useRef  } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import 'bootstrap/dist/css/bootstrap.min.css';
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

export default function TypeModal({type, open, handleClose}){

    const [name, setName] = React.useState('');
    const [variables, setVariables] = React.useState([]);
    const childrenRefs = useRef([]);


    useEffect(() => {
        if (type.id !== undefined){
            setName(type.name);
            setVariables(type.variables);
            childrenRefs.current = [...type.variables];
        }
    }, [])

    function saveType(){
        let variables = [];
        console.log("SAVE",childrenRefs)
        childrenRefs.current.forEach((childRef) => {
            if (childRef) {
                variables.push(childRef.variable.getVariable());
                console.log("NAME " + childRef.variable.getVariable().name);
            }
        });
        console.log("Saved variables:", variables);

        axios.post('/api/v1/type',{
            id: type.id,
            name: name,
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
              <VariableList incomeVariables={variables} setVariables={setVariables} ref={childrenRefs} pinAccepted={false}/>
              <Button variant="outlined" onClick={saveType}>Save</Button>
            </Box>
          </Modal>
    )
}