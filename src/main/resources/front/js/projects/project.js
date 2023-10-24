import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

export default function Project({project, onClickProject}) {

    return (
        <div onClick={() => onClickProject(project.id)}>
            {project.name}
        </div>
    )
}