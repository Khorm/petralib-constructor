import React, { useEffect, useMemo } from 'react';

import TextField from '@mui/material/TextField';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import { Typography } from '@mui/material';

// Типы

export interface CTypeShortDto{
  id: number;
  name: string;
  description?: string;
}

export interface VariableData {
  id?: number;
  name: string;
  multiplicity: 'SINGLE' | 'COLLECTION';
  description?: string;
  pinType: 'IN' | 'OUT';
  variableType?: CTypeShortDto;
  
}

interface VariableProps {
  variable: VariableData;
  index: number;
  editVariable: (variable: VariableData, index: number) => void;
  removeVariable: (index: number) => void;
  types?: CTypeShortDto[];
}

const MAX_LENGTH = 100;

export default function Variable({
  variable,
  index,
  editVariable,
  removeVariable,
  types,
}: VariableProps) {
  const { name, variableType, multiplicity, description = '', pinType } = variable;

  // Валидация
  const nameError = useMemo(() => {
    if (!name) return 'Имя обязательно';
    if (name.length > MAX_LENGTH) return `Имя не должно превышать ${MAX_LENGTH} символов`;
    return '';
  }, [name]);


  const multiplicityError = useMemo(() => {
    return !multiplicity ? 'Множественность обязательна' : '';
  }, [multiplicity]);

  // Обновление родителя
  const handleChange = (field: keyof VariableData, value: any) => {
    editVariable({ ...variable, [field]: value }, index);
  };

  const typeChange = (typeName: string) => {
    const selectedType = types?.find((t) => t.name === typeName);
    handleChange('variableType', selectedType);
    // handleChange('type', selectedType ? selectedType.name : '');
  }


  if (!types) {
    return <div>Загрузка типов...</div>;
  }
  return (
    <Box
      sx={{
        display: 'flex',
        gap: 2,
        flexWrap: 'wrap',
        alignItems: 'center',
        p: 2,
        mb: 2,
        border: '1px solid #eee',
        borderRadius: 1,
        bgcolor: '#fdfdfd',
      }}
    >
      <TextField
        required
        error={!!nameError}
        helperText={nameError}
        label="Имя"
        value={name}
        onChange={(e) => handleChange('name', e.target.value)}
        variant="outlined"
        size="small"
        sx={{ flexGrow: 1, minWidth: 150 }}
      />
      
      <FormControl required size="small" sx={{ minWidth: 180 }}>
        <InputLabel>Тип</InputLabel>
        <Select
          value={variableType?.name || ''}
          label="Тип"
          onChange={(e) => typeChange(e.target.value)}
          // error={!typeName || !typeNameValid}
        >
          {types.map((type) => (
            <MenuItem key={type.id} value={type.name}>
              {type.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <FormControl required error={!!multiplicityError} size="small" sx={{ minWidth: 130 }}>
        <InputLabel>Множественность</InputLabel>
        <Select
          value={multiplicity}
          label="Множественность"
          onChange={(e) => handleChange('multiplicity', e.target.value)}
        >
          <MenuItem value="SINGLE">Single</MenuItem>
          <MenuItem value="COLLECTION">Collection</MenuItem>
        </Select>
        {multiplicityError && <Box component="div" sx={{ color: 'error.main', fontSize: 12, mt: 0.5 }}>{multiplicityError}</Box>}
      </FormControl>

      <TextField
        label="Описание"
        value={description}
        onChange={(e) => handleChange('description', e.target.value)}
        variant="outlined"
        size="small"
        multiline
        rows={1}
        sx={{ flexGrow: 2, minWidth: 200 }}
      />

      <Button
        variant="outlined"
        color="error"
        size="small"
        onClick={() => removeVariable(index)}
        sx={{ whiteSpace: 'nowrap' }}
      >
        Удалить
      </Button>
    </Box>
  );
}

// import React, { useState, useEffect, forwardRef, useRef } from 'react';


// import Modal from '@mui/material/Modal';
// import Box from '@mui/material/Box';
// import TextField from '@mui/material/TextField';
// import Button from '@mui/material/Button';
// import InputLabel from '@mui/material/InputLabel';
// import Autocomplete from '@mui/material/Autocomplete';
// import Select from '@mui/material/Select';
// import MenuItem from '@mui/material/MenuItem';
// import IconButton from '@mui/material/IconButton';
// import RemoveIcon from '@mui/icons-material/Remove';
// import Divider from '@mui/material/Divider';

// import Accordion from '@mui/material/Accordion';
// import AccordionSummary from '@mui/material/AccordionSummary';
// import AccordionDetails from '@mui/material/AccordionDetails';
// import Typography from '@mui/material/Typography';
// import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

// import 'bootstrap/dist/css/bootstrap.min.css';


// const Variable = forwardRef(({ variable, types, removeVar, index, pinAccepted }, ref) => {

//   const [name, setName] = React.useState('');
//   const [multiplicity, setMultiplicity] = React.useState('');
//   const [pinType, setPinType] = React.useState('');
//   const [type, setType] = React.useState({});
//   const [description, setDescription] = React.useState('');

//   const inputRef = useRef({});


//   useEffect(() => {
//     update();
//     if (ref) {
//       ref.variable = {
//         getVariable: () => inputRef.current,
//         setVariable: (newVariable) => (inputRef.current = newVariable),
//       };
//     }
//   }, [ref]);



//   function update() {
//     console.log("UPDATE ",)
//     handleVarName(variable.name);
//     handleDescription(variable.description);
//     handleMultiplicity(variable.multiplicity);
//     let varType =  variable.variableType;
//     if (!varType){
//       varType = variable.fieldType
//     }
//     handleType('', varType);
//     handlePinType(variable.pinType)

//     inputRef.current.id = variable.id;
//     console.log("UPDATE ", inputRef)
//   }


//   function handleVarName(name) {
//     if (name === undefined) name = '';
//     inputRef.current.name = name;
//     setName(name);
//   }

//   function handleMultiplicity(m) {
//     if (m === undefined) m = '';
//     inputRef.current.multiplicity = m;
//     setMultiplicity(m);
//   }

//   function handlePinType(m) {
//     if (m === undefined) m = '';
//     inputRef.current.pinType = m;
//     setPinType(m);
//   }

//   function handleType(event, newValue) {
//     if (newValue === undefined) newValue = {};
//     inputRef.current.variableType = newValue;
//     inputRef.current.fieldType = newValue;
//     setType(newValue);
//   }

//   function handleDescription(d) {
//     if (d === undefined) d = '';
//     inputRef.current.description = d;
//     setDescription(d);
//   }



//   return (
//     <div>
//       <div className='variable-div'>
//         <div style={{ width: '30%' }}>
//           <TextField value={name} onChange={(e) => handleVarName(e.target.value)}
//             id="outlined-basic" label="Name" variant="outlined" fullWidth />
//         </div>
//         <div style={{ width: '30%' }}>
//           <InputLabel id="demo-simple-select-label" >Multiplicity</InputLabel>
//           <Select
//             fullWidth sx={{ minWidth: 1 }}
//             labelId="demo-simple-select-label"
//             id="demo-simple-select"
//             value={multiplicity}
//             label="multiplicity"
//             onChange={(e) => handleMultiplicity(e.target.value)}
//           >
//             <MenuItem value={'SINGLE'}>Single</MenuItem>
//             <MenuItem value={'COLLECTION'}>Collection</MenuItem>
//           </Select>
//         </div>
//         {pinAccepted &&
//           <div style={{ width: '30%' }}>
//             <InputLabel id="pin-type-select-label" >pinType</InputLabel>
//             <Select
//               fullWidth sx={{ minWidth: 1 }}
//               labelId="pin-type-select-label"
//               id="pin-type-select"
//               value={pinType}
//               label="pin-type"
//               onChange={(e) => handlePinType(e.target.value)}
//             >
//               <MenuItem value={'IN'}>In</MenuItem>
//               <MenuItem value={'OUT'}>Out</MenuItem>
//             </Select>
//           </div>
//         }

//         <div style={{ width: '30%' }}>
//           <Autocomplete
//             fullWidth
//             getOptionLabel={(option) => { if (option !== undefined && option.name !== undefined) return option.name; else return '' }}
//             value={type}
//             options={types}
//             onChange={handleType}
//             renderInput={(params) => <TextField {...params} label="Type" />}
//           />
//         </div>
//         <div style={{ width: '10%' }}>
//           <IconButton aria-label="remove" onClick={() => removeVar(index)}>
//             <RemoveIcon />
//           </IconButton>
//         </div>

//       </div>
//       <Accordion>
//         <AccordionSummary
//           expandIcon={<ExpandMoreIcon />}
//           aria-controls="panel1-content"
//           id="panel1-header"
//         >
//           <Typography component="span">Description</Typography>
//         </AccordionSummary>
//         <AccordionDetails>
//           <TextField
//             fullWidth sx={{ minWidth: 1 }}
//             id="outlined-multiline-flexible"
//             label="Multiline"
//             multiline
//             maxRows={4}
//             value={description} onChange={(e) => handleDescription(e.target.value)}
//           />
//         </AccordionDetails>
//       </Accordion>
//       <Divider sx={{ mt: '10px', mb: '10px', opacity: 1 }} variant="middle" />
//     </div>
//   )
// });

// export default Variable;