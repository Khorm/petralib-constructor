import React, { useState, useEffect, useRef } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Popper from '@mui/material/Popper';

import 'bootstrap/dist/css/bootstrap.min.css';
import './type-selector.sass';

import TypeDropdown from './dropdown';


//baseTypeId - айди типа от котрого идет отсчет его значений
//typeArray - список выбранных переменных
//setNewTypesArray - функция выставления новой коллекции
export default function TypeSelector({baseTypeId, typeArray, setNewTypesArray}){

    const [nextVarAccept, setNextVarAccept] = React.useState(false);
    const [dropdownObj, setDropdownObj] = React.useState(undefined);

    useEffect(() => {
         checkNextVar();
    }, [typeArray])

    function handleOpen (ownerTypeId, e, type) {
        setDropdownObj({
            varTypeId: ownerTypeId,
            currentTarget: e.currentTarget,
            currentVar: type,
        })
    }

    function handleChange(typeVar, remove = false) {
        if (typeVar === undefined){
            setDropdownObj(undefined);
            return;
        }

        let newTypeArr = [];
        if (typeArray !== undefined){
            let find = false;
            for (let i = 0; i < typeArray.length; i++){
                if (typeVar.ownerId === typeArray[i].ownerId || typeVar.ownerId === baseTypeId){
                    if (!remove){
                        newTypeArr.push(typeVar);
                    }
                    find = true;
                    break;
                }else{
                    newTypeArr.push(typeArray[i])
                }
            }

            if (!find && !remove){
                newTypeArr.push(typeVar);
            }
        }else{
            newTypeArr.push(typeVar)
        }
        console.log("setNewTypesArray",  newTypeArr);
        setNewTypesArray(newTypeArr);
        setDropdownObj(undefined);
    }

    function checkNextVar(){
        if (typeArray === undefined || typeArray.length == 0){
            return;
        }
        axios.get('/api/v1/type/values/' + typeArray[typeArray.length - 1].varType.id,{ params: {
            projectId: getProjectId()
        }})
        .then((response) => {
            setNextVarAccept(response.data.length > 0);
        }).catch((error) => {
            console.log(error);
            alert(error)
        })
    }


    if (typeArray !== undefined && typeArray.length > 0 ){
        return(
            <div className='variable-container'>
                {typeArray.map((type, index) => {
                    return(
                        <div className='variable-container' key={index}>
                            <button className='btn-type' type="button" onClick={(e)=>handleOpen(type.ownerId,e, type)}>
                                {type.name}
                            </button>
                            {index < typeArray.length-1 &&
                                <h4>|</h4>
                            }
                            <TypeDropdown owner={dropdownObj} chooseVariableFunc={handleChange}/>
                        </div>
                    )
                })}
                {nextVarAccept &&
                    <div className='variable-container'>
                    <h4>|</h4>
                    <button className='btn-type add-type-btn' type="button" onClick={(e)=>handleOpen(typeArray[typeArray.length - 1].varType.id,e)}>
                        Select a variable...
                    </button>
                    <TypeDropdown owner={dropdownObj} chooseVariableFunc={handleChange}/>
                    </div>
                }

            </div>
        )
    }else{
        return(<div className='variable-container'>
            <button type="button" className='btn-type add-type-btn' onClick={(e)=>handleOpen(baseTypeId,e)}>
                Select a variable...
            </button>
            <TypeDropdown owner={dropdownObj} chooseVariableFunc={handleChange}/>
        </div>)

    }
}