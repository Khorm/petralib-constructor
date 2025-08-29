import React, { useState, useEffect  } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css'

import axios from 'axios'

import { Button } from '@mui/material';
import { Grid } from '@mui/material';
import { Item } from '@mui/material';
import TextField from '@mui/material/TextField';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';

//непосредственно отображение блока (активности, сигнала, воркфлоу)
export default function Block({block, url, onChoose, chosen}) {

    const [isButtonShow, setIsButtonShow] = useState(false)
    const [chooseStyle, setChooseStyle] = useState({})

    useEffect(() => {
        if (chosen){
            setChooseStyle({backgroundColor: "red"});
        }
    }, [chosen])


    function showOptions(){
        window.location.href = '/projects/' + getProjectId() + '/constructor/' + url + '/' + block.id;
    }

    return (
        <div style={chooseStyle} onMouseEnter={() => setIsButtonShow(true)} onMouseLeave={() => setIsButtonShow(false)} onClick={() => onChoose(block)}>
            {block.name}
            {isButtonShow && <Button onClick={() => showOptions()}>Options</Button>}
        </div>
    )
}