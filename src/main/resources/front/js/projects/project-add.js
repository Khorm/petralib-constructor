import React from 'react';
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';


export default function ProjectAdd({openModal}) {


    return (
        <div onClick={() => openModal({id:-1, name: ''})} className="add-button">
            <h3>Add project</h3>
        </div>

    )
}