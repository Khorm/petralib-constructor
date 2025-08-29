import React, { useState, useEffect  } from 'react';
import { createRoot } from 'react-dom/client';
//import { Provider } from 'react-redux';
import axios from 'axios';
import Stack from '@mui/material/Stack';


import 'bootstrap/dist/css/bootstrap.min.css';
import './project.sass';

import User from './user';
import Project from './project';
import ProjectAdd from './project-add';
import ProjectModal from './project-modal';


export default function App() {

    const [projects, setProjects] = useState([]);
    const [editProject, setEditProject] = useState({});
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    useEffect(() => {
        loadProjects()
    }, [])

    function loadProjects(){
        axios.get('/api/v1/project')
                .then((response) => {
                    console.log(response)
                    setProjects(response.data)
                })
    }

    function projectSave(project){
        axios.post('/api/v1/project',{
            id: project.id,
            name: project.name
        }
        ).then((response) => {
             console.log("OK: ",response);
             loadProjects();
         }).catch((error) => {
            alert(error)
         })
    }

    function deleteProject(projectId){
        axios.delete('/api/v1/project/'+projectId)
        .then((response) => {
             console.log("OK: ",response);
             loadProjects();
         }).catch((error) => {
            alert(error)
         })
    }

    function openModal(project){
        setEditProject(project);
        handleOpen();

    }


    return (
        <div className='container'>
            <User/>
            <Stack spacing={2}>
                {projects.map((project, index) => {
                    return(
                        <Project project={project} key={index} openModal={openModal}/>
                    )
                })}
                <ProjectAdd openModal={openModal}/>
            </Stack>
            {open &&
                <ProjectModal project={editProject} saveProj={projectSave} deleteProj={deleteProject} open={open} handleClose={handleClose}/>
            }
        </div>
    )
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<App />);
