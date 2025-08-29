import React from 'react';
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';


export default function Project({project, openModal}) {

    function openProject(){
        window.location.href = window.location.href +'/'+ project.id + '/constructor';
    }

    return (
        <div className="project">
            <div onClick={openProject} className="project-button">
                <h3>{project.name}</h3>
            </div>
            <div onClick={() => openModal(project)} className="edit-button">
                <h5>Edit</h5>
            </div>
        </div>
    )
}