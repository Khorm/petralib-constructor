import React, { useState, useEffect, useRef } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Popper from '@mui/material/Popper';

import 'bootstrap/dist/css/bootstrap.min.css';
import './dropdown.sass';

//owner {ownerVarTypeId, currentTarget} - объект владелец дропдауна
// varTypeId - айди типа для котрого надо показать переменные
//currentTarget - таргет к которому надо привязать дропдаун
//currentVar - текущая переменнаая у котрой открыт список (может быть undefined)
export default function TypeDropdown({owner, chooseVariableFunc}){

    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const id = open ? 'simple-popper' : undefined;

    const [dropdownTypesVar, setDropdownTypesVar] = React.useState([]);

    useEffect(() => {
        if (!owner) {
            setAnchorEl(null);
            return;
        }
        setAnchorEl(owner.currentTarget);
        axios.get('/api/v1/type/values/' + owner.varTypeId,{ params: {
            projectId: getProjectId()
        }})
        .then((response) => {
            setDropdownTypesVar(response.data);
        }).catch((error) => {
            console.log(error);
            alert(error)
        })

    }, [owner])

    function select(typeVar){
        setAnchorEl(null);
        chooseVariableFunc(typeVar);
    }

    function remove(removedVar){
        console.log("removedVar ", removedVar)
        setAnchorEl(null);
        chooseVariableFunc(removedVar, true);
    }

    function close(){
        setAnchorEl(null);
        chooseVariableFunc(undefined);
    }

    //style={{ border: 2, borderColor: 'black', backgroundColor: 'white', zIndex: 20000, height:'auto' }}
    return(
        <Popper style={{zIndex: 2}} id={id} open={open} anchorEl={anchorEl} disablePortal>
            <Box className='dropdown'>
                <div className='dropdown-content'>
                    <div className='close-item' onClick={close}>Close</div>
                    {owner?.currentVar !== undefined &&
                        <div className='remove-item' onClick={() => remove(owner.currentVar)}>Remove</div>
                    }
                {dropdownTypesVar.map((dropDownTypeVar, index) => {
                      return(
                         <div key={index} onClick={() => select(dropDownTypeVar)} ><h3>{dropDownTypeVar.name}</h3></div>
                      )
                  })}
                  </div>
            </Box>
        </Popper>
    )
}