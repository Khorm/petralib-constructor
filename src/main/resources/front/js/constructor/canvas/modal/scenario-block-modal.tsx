import React, { useEffect } from 'react';
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';

import 'bootstrap/dist/css/bootstrap.min.css';

import { useSelector, useDispatch } from 'react-redux';
import { set, clear } from './scenario-variable-slice';

import idGenerator from './variable/selectors/id-generator-hook';

import ScenarioVariable from './variable/scenario-variable';
import { ValueType } from './variable/value-type';
import { CTypeFieldDto } from './variable/type/type-selector';

/**
 * Интерфейс переменной, получаемой с бэкенда.
 */
export interface VariableDto {
  id: number;
  name: string;
  pinType: 'IN' | 'OUT';
  multiplicity: 'SINGLE' | 'COLLECTION';
  variableType: CTypeShortDto;
}

interface CTypeShortDto{
  id: number;
  name: string;
  description: string;
}

/**
 * Интерфейс текущей переменной блока (локальной).
 */
interface CurrentVariableDto {
  variable: VariableDto;
  maxLocalId: number;
  scenarioVariables: ScenarioVariableDto[];
}

/**
 * Интерфейс переменной сценария (связь блок-переменная).
 */
export interface ScenarioVariableDto {
  scenarioVariableId?: number;
  type: ValueType;
  consumerVariableId: number;
  producerId: number;
  script : string;
  typeInheritance: CTypeFieldDto[];
  localId: number;
  parentId: number;
  blockVariableId: number;   
}

/**
 * Пропсы компонента ScenarioBlockModal.
 */
interface ScenarioBlockModalProps {
  /**
   * Блок сценария, для которого открыта модалка.
   */
  scenarioBlock: {
    id: number;
    name: string;
  };
  /**
   * ID рабочего процесса.
   */
  workflow: number | string;
  /**
   * Флаг видимости модального окна.
   */
  open: boolean;
  /**
   * Обработчик закрытия модального окна.
   */
  handleClose: () => void;
}

/**
 * Стили для модального окна.
 */
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
  overflow: 'auto',
  maxHeight: '90%',
};


/**
 * Модальное окно редактирования переменных блока сценария.
 *
 * Загружает входные и локальные переменные блока,
 * позволяет редактировать их связи через `ScenarioVariable`,
 * и сохраняет изменения на сервере.
 *
 * @component
 * @param {ScenarioBlockModalProps} props - Пропсы компонента.
 * @example
 * <ScenarioBlockModal
 *   scenarioBlock={block}
 *   workflow={123}
 *   open={true}
 *   handleClose={() => setOpen(false)}
 * />
 */
export default function ScenarioBlockModal({
  scenarioBlock,
  workflow,
  open,
  handleClose,
}: ScenarioBlockModalProps) {
  const [allVariables, setAllVariables] = React.useState<VariableDto[]>([]);
  const [blockVariables, setBlockVariables] = React.useState<VariableDto[]>([]);
  const dispatch = useDispatch();
  const scenarioVariables = useSelector((state: any) => state.scenarioVariables.list);

  useEffect(() => {
    if (!open || !scenarioBlock?.id) return;

    axios
      .get<{
        inputVariables: VariableDto[];
        currentVariables: CurrentVariableDto[];
      }>('/api/v1/scenario/' + scenarioBlock.id + '/variables', {
        params: {
          // @ts-ignore
          projectId: getProjectId(),
        },
      })
      .then((response) => {
        const variables: VariableDto[] = [...response.data.inputVariables];
        const blockVariables: VariableDto[] = [];
        const scenarioValues: ScenarioVariableDto[] = [];

        response.data.currentVariables.forEach((element) => {
          variables.push(element.variable);
          blockVariables.push(element.variable);
          scenarioValues.push(...element.scenarioVariables);
          idGenerator.setId(element.variable.id, element.maxLocalId);
        });
        
        console.log('LOAD : ', variables, blockVariables, scenarioValues);
        setAllVariables(variables);
        setBlockVariables(blockVariables);
        dispatch(set(scenarioValues));
      })
      .catch((error) => {
        console.error('Ошибка загрузки переменных:', error);
        alert(`Не удалось загрузить переменные: ${error.message}`);
      });
  }, []);

  /**
   * Сохраняет отредактированные переменные сценария на сервере.
   */
  const save = () => {
    console.log('SAVE : ', scenarioVariables);
    axios
      .post(
        '/api/v1/scenario/' + scenarioBlock.id + '/variables',
        scenarioVariables,
        {
          params: {
            // @ts-ignore
            projectId: getProjectId(),
          },
        }
      )
      .then((response) => {
        console.log('OK: ', response);
        close();
        // dispatch(clear());
        // handleClose();
      })
      .catch((error) => {
        console.error('Ошибка сохранения переменных:', error);
        alert(`Не удалось сохранить переменные: ${error.message}`);
      });
  };

  /**
   * Находит стандартный localId для переменной по её ID.
   * @param {number} variableId - ID переменной.
   * @returns {number | undefined} Найденный localId или undefined.
   */
  const findDefaultScenarioVariable = (variableId: number): ScenarioVariableDto | undefined => {    
    const v = scenarioVariables.find(
      (sv: ScenarioVariableDto) =>
        sv.blockVariableId === variableId 
          && sv.parentId === null || sv.parentId === undefined || sv.parentId === 0
    );
    console.log('search ', v, scenarioVariables, variableId);
    return v;
  };

  const close = () => {    
    dispatch(clear(undefined));
    handleClose();
  }


  return (
    <Modal open={open} onClose={close}>
      <Box sx={style}>
        <h2>{scenarioBlock.name}</h2>
        {blockVariables.map((currentVariable, index) => (
          <ScenarioVariable
            key={index}
            currentVariable={currentVariable}
            acceptedVariables={allVariables}
            scenarioVariable={findDefaultScenarioVariable(currentVariable.id)}                     
          />
        ))}
        <Button variant="outlined" onClick={save} sx={{ mt: 2 }}>
          Сохранить
        </Button>
      </Box>
    </Modal>
  );
}

// import React, { useState, useEffect } from 'react'
// import axios from 'axios';

// import Modal from '@mui/material/Modal';
// import Box from '@mui/material/Box';
// import Button from '@mui/material/Button';

// import 'bootstrap/dist/css/bootstrap.min.css';

// import { useSelector, useDispatch } from 'react-redux';
// import { set, clear } from './scenario-variable-slice';
// // import { add } from './selector/id/local-id-slice';

// import  idGenerator  from './selector/hooks/id-generator-hook'

// import ScenarioVariable from './scenario-variable';

// const style = {
//   position: 'absolute',
//   top: '50%',
//   left: '50%',
//   transform: 'translate(-50%, -50%)',
//   width: '50%',
//   bgcolor: 'background.paper',
//   border: '2px solid #000',
//   boxShadow: 24,
//   p: 4,
//   overflow: 'auto',
//   maxHeight: '90%'
// };

// export default function ScenarioBlockModal({scenarioBlock, workflow, open, handleClose}){

//     const [allVariables, setAllVariables] = React.useState([]);
//     const [currentVariables, setCurrentVariables] = React.useState([]);
//     const dispatch = useDispatch();
//     const scenarioVariables = useSelector((state) =>state.scenarioVariables.list);
    
    


//     useEffect(() => {
//         console.log('scenarioBlock', scenarioBlock)
//         axios.get('/api/v1/scenario/' + scenarioBlock.id + '/variables',{ params: {
//             projectId: getProjectId()
//         }})
//         .then((response) => {
            
//             let variables = [];
//             let localVariables = [];
//             let scenarioValues = [];
//             variables.push(...response.data.inputVariables);           
//             console.log("LOADED : ", response.data)  
//             response.data.currentVariables.forEach(element => {
//                 variables.push(element.variable);
//                 localVariables.push(element.variable);                
//                 scenarioValues.push(...element.scenarioVariables);                
//                 idGenerator.setId(element.variable.id, element.maxLocalId)
//             });
//             setAllVariables(variables);
//             setCurrentVariables(localVariables)  
                               
//             dispatch(set(scenarioValues));
//         }).catch((error) => {
//             console.error(error);
//             alert(error.message)
//        })
//     }, [])



//     function save() {
//         console.log('SAVE : ', scenarioVariables);
//         axios.post('/api/v1/scenario/' + scenarioBlock.id + '/variables',scenarioVariables,{ params: {
//                 projectId: getProjectId()
//             }}
//             ).then((response) => {
//                 console.log("OK: ",response);
//                 dispatch(clear());
//                 handleClose();

//             }).catch((error) => {
//                 console.error(error);
//                 alert(error.message)
//            })
//     }


//     function findDefaultLocalId(variableId){
//         return scenarioVariables.find(scenarioVar => scenarioVar.blockVariableId === variableId && scenarioVar.parentId === 0)?.localId       
//     }

//     return(
//         <Modal
//             open={open}
//             onClose={handleClose}
//           >
//             <Box sx={style}>
//                 <h2>{scenarioBlock.name}</h2>
//                 {currentVariables.map((currentVariable, index) => {
//                     return(
//                         <ScenarioVariable key={index} currentVariable = {currentVariable} inputVariables={allVariables}
//                          defaultLocalId={findDefaultLocalId(currentVariable.id)}/>
//                     )
//                 })}
//                 <Button variant="outlined" onClick={save}>Save</Button>
//             </Box>
//           </Modal>
//     )
// }