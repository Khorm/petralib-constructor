import React, { useState, useEffect  } from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

import Project from './project';

export default function App() {

    const [projects, setProjects] = useState([]);

    useEffect(() => {
        axios.get('/api/v1/project')
            .then((response) => {
                console.log(response)
                setProjects(response.data)
            })
    }, [])

    function onProjectChoose(projectId){
        console.log(projectId);
    }

    return (
        <div>
            {projects.map((project, index) => {
                return(
                    <Project project={project} onClickProject={onProjectChoose} key={index}/>
                )
            })}
        </div>
    )
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<App />);
