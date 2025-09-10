import React, { useState, useEffect, forwardRef, useRef } from 'react';


import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import InputLabel from '@mui/material/InputLabel';
import Autocomplete from '@mui/material/Autocomplete';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import IconButton from '@mui/material/IconButton';
import RemoveIcon from '@mui/icons-material/Remove';
import Divider from '@mui/material/Divider';

import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

import 'bootstrap/dist/css/bootstrap.min.css';


const Variable = forwardRef(({ variable, types, removeVar, index, pinAccepted }, ref) => {

  const [name, setName] = React.useState('');
  const [multiplicity, setMultiplicity] = React.useState('');
  const [pinType, setPinType] = React.useState('');
  const [type, setType] = React.useState({});
  const [description, setDescription] = React.useState('');

  const inputRef = useRef({});


  useEffect(() => {
    update();
    if (ref) {
      ref.variable = {
        getVariable: () => inputRef.current,
        setVariable: (newVariable) => (inputRef.current = newVariable),
      };
    }
  }, [ref]);



  function update() {
    console.log("UPDATE ",)
    handleVarName(variable.name);
    handleDescription(variable.description);
    handleMultiplicity(variable.multiplicity);
    handleType('', variable.fieldType);
    handlePinType(variable.pinType)

    inputRef.current.id = variable.id;
    console.log("UPDATE ", inputRef)
  }


  function handleVarName(name) {
    if (name === undefined) name = '';
    inputRef.current.name = name;
    setName(name);
  }

  function handleMultiplicity(m) {
    if (m === undefined) m = '';
    inputRef.current.multiplicity = m;
    setMultiplicity(m);
  }

  function handlePinType(m) {
    if (m === undefined) m = '';
    inputRef.current.pinType = m;
    setPinType(m);
  }

  function handleType(event, newValue) {
    if (newValue === undefined) newValue = {};
    inputRef.current.fieldType = newValue;
    setType(newValue);
  }

  function handleDescription(d) {
    if (d === undefined) d = '';
    inputRef.current.description = d;
    setDescription(d);
  }



  return (
    <div>
      <div className='variable-div'>
        <div style={{ width: '30%' }}>
          <TextField value={name} onChange={(e) => handleVarName(e.target.value)}
            id="outlined-basic" label="Name" variant="outlined" fullWidth />
        </div>
        <div style={{ width: '30%' }}>
          <InputLabel id="demo-simple-select-label" >Multiplicity</InputLabel>
          <Select
            fullWidth sx={{ minWidth: 1 }}
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={multiplicity}
            label="multiplicity"
            onChange={(e) => handleMultiplicity(e.target.value)}
          >
            <MenuItem value={'SINGLE'}>Single</MenuItem>
            <MenuItem value={'COLLECTION'}>Collection</MenuItem>
          </Select>
        </div>
        {pinAccepted &&
          <div style={{ width: '30%' }}>
            <InputLabel id="pin-type-select-label" >pinType</InputLabel>
            <Select
              fullWidth sx={{ minWidth: 1 }}
              labelId="pin-type-select-label"
              id="pin-type-select"
              value={pinType}
              label="pin-type"
              onChange={(e) => handlePinType(e.target.value)}
            >
              <MenuItem value={'IN'}>In</MenuItem>
              <MenuItem value={'OUT'}>Out</MenuItem>
            </Select>
          </div>
        }

        <div style={{ width: '30%' }}>
          <Autocomplete
            fullWidth
            getOptionLabel={(option) => { if (option !== undefined && option.name !== undefined) return option.name; else return '' }}
            value={type}
            options={types}
            onChange={handleType}
            renderInput={(params) => <TextField {...params} label="Type" />}
          />
        </div>
        <div style={{ width: '10%' }}>
          <IconButton aria-label="remove" onClick={() => removeVar(index)}>
            <RemoveIcon />
          </IconButton>
        </div>

      </div>
      <Accordion>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1-content"
          id="panel1-header"
        >
          <Typography component="span">Description</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <TextField
            fullWidth sx={{ minWidth: 1 }}
            id="outlined-multiline-flexible"
            label="Multiline"
            multiline
            maxRows={4}
            value={description} onChange={(e) => handleDescription(e.target.value)}
          />
        </AccordionDetails>
      </Accordion>
      <Divider sx={{ mt: '10px', mb: '10px', opacity: 1 }} variant="middle" />
    </div>
  )
});

export default Variable;