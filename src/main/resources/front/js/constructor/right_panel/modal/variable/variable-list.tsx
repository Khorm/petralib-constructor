import React, { useEffect, useState } from 'react';

import { Button, Box, Grid, Typography } from '@mui/material';

import Variable, { VariableData, CTypeShortDto } from './variable';
import axios from 'axios';

// Интерфейс для пропсов
interface VariablesListProps {
  variables: VariableData[];
  addInVariable: () => void;
  addOutVariable: () => void;
  removeVariable: (index: number) => void;
  editVariable: (variable: VariableData, index: number) => void;
  listOwner: 'SOURCE' | 'ACTION' | 'WORKFLOW'
}

export default function VariablesList({
  variables,
  addInVariable,
  addOutVariable,
  removeVariable,
  editVariable,
  listOwner,
}: VariablesListProps) {

  const [types, setTypes] = useState<CTypeShortDto[]>([]);
  // const [loading, setLoading] = useState<boolean>(true);

    // Фильтруем переменные
  const inVariables = variables.filter((v) => v.pinType === 'IN');
  const outVariables = variables.filter((v) => v.pinType === 'OUT');
  const hasOut = outVariables.length > 0;

  useEffect(() => {
    axios
      .get<CTypeShortDto[]>('/api/v1/type', {
        params: { projectId: getProjectId() },
      })
      .then((response) => {
        setTypes(response.data);
        // setError(null);
      })
      .catch((err) => {
        console.error('Ошибка загрузки типов:', err);
        // setError('Не удалось загрузить список типов. Проверьте подключение.');
      })
      .finally(() => {
        // setLoading(false);
      });
  }, []);


  return (
    <Box sx={{ mt: 3 }}>
      <Typography variant="h6" gutterBottom>
        Переменные
      </Typography>

      <Grid container spacing={3}>
        {/* Колонка IN */}
        <Grid item xs={6}>
          <Typography variant="subtitle1" fontWeight="bold" gutterBottom>
            Входные (IN)
          </Typography>
          {inVariables.length === 0 ? (
            <Typography variant="body2" color="text.secondary" sx={{ fontStyle: 'italic' }}>
              Нет входных переменных
            </Typography>
          ) : (
            inVariables.map((variable, index) => {
              // Находим оригинальный индекс в общем массиве
              const globalIndex = variables.indexOf(variable);
              return (
                <Variable
                  key={`in-${globalIndex}`}
                  variable={variable}
                  index={globalIndex}
                  editVariable={editVariable}
                  removeVariable={removeVariable}
                  types={types}
                />
              );
            })
          )}
          <Button size="small" variant="outlined" color="primary" onClick={addInVariable} sx={{ mt: 1 }}>
            Добавить IN
          </Button>
        </Grid>

        {/* Колонка OUT */}
        <Grid item xs={6}>
          <Typography variant="subtitle1" fontWeight="bold" gutterBottom>
            Выходные (OUT)
          </Typography>
          {outVariables.length === 0 ? (
            <Typography variant="body2" color="text.secondary" sx={{ fontStyle: 'italic' }}>
              Нет выходной переменной
            </Typography>
          ) : (
            outVariables.map((variable, index) => {
              const globalIndex = variables.indexOf(variable);
              return (
                <Variable
                  key={`out-${globalIndex}`}
                  variable={variable}
                  index={globalIndex}
                  editVariable={editVariable}
                  removeVariable={removeVariable}
                  types={types}
                />
              );
            })
          )}

          {listOwner === 'SOURCE' &&
            <Button
              size="small"
              variant="outlined"
              color="secondary"
              onClick={addOutVariable}
              disabled={hasOut}
              sx={{ mt: 1 }}
            >
              {hasOut ? 'Только одна OUT' : 'Добавить OUT'}
            </Button>
          }

          {listOwner !== 'SOURCE' &&
            <Button
              size="small"
              variant="outlined"
              color="secondary"
              onClick={addOutVariable}              
              sx={{ mt: 1 }}
            >
              {'Добавить OUT'}
            </Button>
          }
        </Grid>
      </Grid>
    </Box>
  );
}
// import React, { useState, useEffect ,forwardRef } from 'react';
// import axios from 'axios';

// import IconButton from '@mui/material/IconButton';
// import AddIcon from '@mui/icons-material/Add';


// import 'bootstrap/dist/css/bootstrap.min.css';
// import './variable.sass';

// import Variable from './variable';



// const VariableList = forwardRef(({incomeVariables, setVariables, pinAccepted = true}, ref) => {

//     const [types, setTypes] = React.useState([]);

//     useEffect(() => {
//         axios.get('/api/v1/type',{ params :{
//                 projectId: getProjectId()
//             }}
//             ).then((response) => {
//                  setTypes(response.data)
//              }).catch((error) => {
//                 alert(error)
//              })
//         }, [])




//     function addNewVariable(){
//         let newVars = [...incomeVariables];
//         let newVar = {variable: {name: 'newVar ' + incomeVariables.length}};
//         newVars.push(newVar);
//         ref.current.push(newVar);
//         setVariables(newVars);
//         console.log("ADD VAR",ref)
//     }

//     function removeVariable(index){
//         let newVars = [];
//         console.log("REMOVE",index)
//         for (let i = 0; i<incomeVariables.length; i++){
//             console.log("check ",ref.current[i])
//             if (i !== index){
//                 newVars.push(ref.current[i].variable.getVariable())
//             }
//         }
//         ref.current = newVars;
//         console.log("END ",ref.current)
//         setVariables(newVars);
//     }


//     return(
//         <div className='variable-list-div'>

//             <IconButton aria-label="add" onClick={addNewVariable}>
//                 <AddIcon />
//             </IconButton>

//                 {incomeVariables.map((variable, index) => {
//                     return(
//                         <Variable key={index} index={index} variable = {variable} 
//                         types = {types} removeVar={removeVariable} ref={ref.current[index]} pinAccepted={pinAccepted}/>
//                     )
//                 })}
//         </div>
//     )
// });

// export default VariableList;