import React, { useState, useEffect  } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import 'bootstrap/dist/css/bootstrap.min.css';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

export default function ServiceModal({service, open, handleClose}){

    const [name, setName] = React.useState('');
    const [path, setPath] = React.useState('');

    useEffect(() => {
            if (service.id !== undefined){
//                axios.get('/api/v1/service/'+service.id)
//                .then((response) => {
//                    console.log(response);
//                    setName(response.data.name);
//                    setPath(response.data.path);
//                }).catch((error) => {
//                  alert(error)
//               })
                setName(service.name);
                setPath(service.path);
            }
        }, [])

    function saveService(){
        axios.post('/api/v1/service',{
            id: service.id,
            name: name,
            path: path,
            projectId: getProjectId()
        }
        ).then((response) => {
             console.log("OK: ",response);
             handleClose();
         }).catch((error) => {
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
              <div><TextField value={path} onChange = {(e) => setPath(e.target.value)} id="outlined-basic" label="Path" variant="outlined" /></div>
              <Button variant="outlined" onClick={saveService}>Save</Button>
            </Box>
          </Modal>
    )
}