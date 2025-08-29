import React from 'react';
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

export default function ProjectModal({project, saveProj, deleteProj, open, handleClose}){

    const [name, setName] = React.useState(project.name);

    function saveProject(){
        let newProj = {
            id: project.id,
            name: name
        }
        saveProj(newProj);
        handleClose();
    }

    function deleteProject(){
        deleteProj(project.id);
        handleClose();
    }

    return(

        <Modal
            open={open}
            onClose={handleClose}
          >
            <Box sx={style}>
              <div><TextField value={name} onChange = {(e) => setName(e.target.value)} id="outlined-basic" label="Name" variant="outlined" /></div>
              <Button variant="outlined" onClick={saveProject}>Save</Button>
              {project.id !== -1 &&
                <Button variant="outlined" onClick={deleteProject} color="error">Delete</Button>
              }

            </Box>
          </Modal>
    )
}