import React, { useState, useEffect , useRef  } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import 'bootstrap/dist/css/bootstrap.min.css';
import VariableList from './variable/variable-list';
import { Block } from 'typescript';
import { CTypeFieldDto } from '../../canvas/modal/variable/type/type-selector';
import { CTypeShortDto, VariableData } from './variable/variable';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '50%',
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

interface CTypeField{
      id?: number;
      name: string;
      ownerId: number;
      multiplicity: 'SINGLE' | 'COLLECTION';
      description?: string;      
      fieldType: CTypeShortDto;
}

interface TypeFull{
    id: number,
    name: string,
    description: string,
    variables: CTypeField[]
}

interface TypeModalProps {
  type: TypeFull;
  open: boolean;
  handleClose: () => void;
}

export default function TypeModal({type, open, handleClose}:TypeModalProps){

    const [name, setName] = React.useState<string>('');
    const [variables, setVariables] = React.useState<VariableData[]>([]);
    // const childrenRefs = useRef([]);


    useEffect(() => {
        if (type.id !== undefined){
            setName(type.name);
            setVariables(type.variables.map((v) => {
                return {
                    id: v.id,
                    name: v.name,
                    multiplicity: v.multiplicity,
                    description: v.description,
                    pinType: 'IN',
                    variableType: v.fieldType
                }}
            ));            
        }
    }, [])

    function saveType(){
        
        // childrenRefs.current.forEach((childRef) => {
        //     if (childRef) {
        //         variables.push(childRef.variable.getVariable());
        //         console.log("NAME " + childRef.variable.getVariable().name);
        //     }
        // });
        console.log("Saved variables:", variables);

        const dtoVariables: CTypeFieldDto[] = variables.map((v) => {
            return {
                id: v.id,
                ownerId: type.id,
                name: v.name,
                multiplicity: v.multiplicity,
                description: v.description,
                fieldType: v.variableType
            }
        });
        const dto : TypeFull = {
            id: type.id,
            name: name,
            variables: dtoVariables,
            description: ''
        }

        axios.post('/api/v1/type',dto,{
         params :{
            // @ts-ignore
            projectId: getProjectId()
        }}
        ).then((response) => {
             console.log("OK: ",response);
             handleClose();
         }).catch((error) => {
            console.log(error);
            alert(error)
         })
    }


    const addVariable = () => {
        const newVariable :VariableData = {
            name: "",
            multiplicity: 'SINGLE',
            pinType: 'IN'
        }

        const newVarArray = [...variables, newVariable];
        setVariables(newVarArray);
    }

    const removeVariable = (index: number) => {
        setVariables((prevVariables) => {
            const newVariables = [...prevVariables];
            newVariables.splice(index, 1);
            return newVariables;
        });
    }

    const editVariable = (variable: VariableData, index: number) => {
        setVariables((prevVariables) => {
            if (index >= 0 && index < prevVariables.length) {
                const newVariables = [...prevVariables];
                newVariables[index] = variable;
                return newVariables;
            } else {
                return prevVariables;
        }});
    }


    return(
        <Modal
            open={open}
            onClose={handleClose}
          >
            <Box sx={style}>
              <div><TextField value={name} onChange = {(e) => setName(e.target.value)} id="outlined-basic" label="Name" variant="outlined" /></div>
              <VariableList variables={variables} addInVariable={addVariable}
               removeVariable={removeVariable} addOutVariable={undefined} editVariable={editVariable}
               listOwner='TYPE'/>
              <Button variant="outlined" onClick={saveType}>Save</Button>
            </Box>
          </Modal>
    )
}