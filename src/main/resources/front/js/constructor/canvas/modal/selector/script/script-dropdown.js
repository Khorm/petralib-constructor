import React, { useState, useEffect, useRef } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Popper from '@mui/material/Popper';

import 'bootstrap/dist/css/bootstrap.min.css';
import './script-dropdown.sass';

//owner {ownerVarTypeId, currentTarget} - ������ �������� ���������
// varTypeId - ���� ���� ��� ������� ���� �������� ����������
//currentTarget - ������ � �������� ���� ��������� ��������
//currentVar - ������� ����������� � ������ ������ ������ (����� ���� undefined)
export default function ScriptDropdown({ owner, setScript, save }) {

    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const id = open ? 'simple-popper' : undefined;


    useEffect(() => {
        console.log("OPEN SCRIPT : " , owner)
        if (!owner) {
            setAnchorEl(null);
            return;
        }
        setAnchorEl(owner.currentTarget);
    }, [owner])

    // function select(typeVar) {
    //     setAnchorEl(null);
    //     chooseVariableFunc(typeVar);
    // }

    // function remove(removedVar) {
    //     console.log("removedVar ", removedVar)
    //     setAnchorEl(null);
    //     chooseVariableFunc(removedVar, true);
    // }

    // function close() {
    //     setAnchorEl(null);       
    // }

    //style={{ border: 2, borderColor: 'black', backgroundColor: 'white', zIndex: 20000, height:'auto' }}
    return (
        <Popper style={{ zIndex: 3 }} id={id} open={open} anchorEl={anchorEl} disablePortal>
            <Box className='dropdown'>
                <div className='dropdown-content'>
                    <textarea name="script" onChange={setScript}>{owner?.script}</textarea>
                    <div className='close-item' onClick={save}>Save</div>
                </div>
            </Box>
        </Popper>
    )
}