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
import TempVariableInput from './local/local-secetor';

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

export interface CTypeShortDto{
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
  variableType: 'LOCAL' | 'GLOBAL'
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
  // const [allVariables, setAllVariables] = React.useState<VariableDto[]>([]);

  const [blockVariables, setBlockVariables] = React.useState<VariableDto[]>([]);
  const [localVariables, setLocalVariables] = React.useState<VariableDto[]>([]);
  const [inputVariables, setInputVariables] = React.useState<VariableDto[]>([]);

  const [blockVersion, setBlockVersion] = React.useState<number>(undefined);
  const [types, setTypes] = React.useState<CTypeShortDto[]>([]);

  const dispatch = useDispatch();

  const scenarioVariables = useSelector((state: any) => state.scenarioVariables.list);

  useEffect(() => {
    if (!open || !scenarioBlock?.id) return;

    loadTypes();
    load();
  }, []);


  const load = () => {

    axios
    .get<{
      inputVariables: VariableDto[];
      currentVariables: CurrentVariableDto[];
      version: number;
      
    }>('/api/v1/scenario/' + scenarioBlock.id + '/variables', {
      params: {
        // @ts-ignore
        projectId: getProjectId(),
      },
    })
    .then((response) => {
      setBlockVersion(response.data.version);
      // dispatch(setLocalVars(response.data.localVariables))

      // const variables: VariableDto[] = [...response.data.inputVariables];
      const blockVariables: VariableDto[] = [];
      const localVariables: VariableDto[] = [];
      const scenarioValues: ScenarioVariableDto[] = [];
      const inputVariables: VariableDto[] = response.data.inputVariables;
      setInputVariables(inputVariables);
      // variables.push(...response.data.localVariables)
      // 

      response.data.currentVariables.forEach((element) => {
        if (element.variableType === 'LOCAL') {
          localVariables.push(element.variable);
        }else{
          blockVariables.push(element.variable);
        }
        
        scenarioValues.push(...element.scenarioVariables);          
        idGenerator.setId(element.variable.id, element.maxLocalId);
      });

      
      console.log('LOAD : ', blockVariables, scenarioValues);
      // setAllVariables(variables);
      setBlockVariables(blockVariables);
      setLocalVariables(localVariables);
      dispatch(set(scenarioValues));
    })
    .catch((error) => {
      console.error('Ошибка загрузки переменных:', error);
      alert(`Не удалось загрузить переменные: ${error.message}`);
    });
  }

  /**
   * Сохраняет отредактированные переменные сценария на сервере.
   */
  const save = () => {
    const sendedDto = {
      dtos : scenarioVariables,
      localVariables : localVariables,
      version: blockVersion
    }
    console.log('SAVE : ', scenarioVariables);

    axios
      .post(
        '/api/v1/scenario/' + scenarioBlock.id + '/variables',
        sendedDto,
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

  const loadTypes = () => {
    axios
      .get<CTypeShortDto[]>('/api/v1/type', {
        params: {
          // @ts-ignore
          projectId: getProjectId(),
        },
      })
      .then((response) => {         
         setTypes(response.data);
      })
      .catch((error) => {
        console.error(
          'Ошибка загрузки типов переменных:',
          error
        );
        alert('Ошибка загрузки типов переменных')
      });
      
  }

  /**
   * Находит стандартный localId для переменной по её ID.
   * @param {number} variableId - ID переменной.
   * @returns {number | undefined} Найденный localId или undefined.
   */
  const findDefaultScenarioVariable = (variableId: number): ScenarioVariableDto | undefined => {    
    const v = scenarioVariables.find(
      (sv: ScenarioVariableDto) =>
        sv.blockVariableId === variableId 
          && (sv.parentId === null || sv.parentId === undefined || sv.parentId === 0)
    );
    console.log('search ', v, scenarioVariables, variableId);
    return v;
  };

  const close = () => {    
    dispatch(clear(undefined));
    handleClose();
  }

  const getAllVariables = () : VariableDto[] => {
    const allVariables = [...blockVariables, ...localVariables, ...inputVariables];
    return allVariables;
  }

  const saveLocalVariables = (variables: VariableDto[]) => {

      axios
        .post('/api/v1/scenario/' + scenarioBlock.id + '/variables/local',
        variables,
      {
        params: {
          // @ts-ignore
          projectId: getProjectId(),
        },
      })
      .then((response) => {
        const newLocalVariables = [...localVariables];
        newLocalVariables.push(...response.data);
        setLocalVariables(newLocalVariables);
      })
      .catch(function (error) {
        console.log(
          'Ошибка сохранения локальных переменных:',
          error
        );
      });
  }

  const removeLocalVariable = (localVariableId: number) => {
    axios
      .delete('/api/v1/scenario/' + scenarioBlock.id + '/variables/local/' + localVariableId, {
        params: {
          // @ts-ignore
          projectId: getProjectId(),
        },
      })
      .then((response) => {
        reload();
      })
      .catch(function (error) {
        console.log(
          'Ошибка удаления локальной переменной:',
          error
        );
      });
  }

  const reload = () => {
        setBlockVersion(undefined);
        setInputVariables([]);
        setBlockVariables([]);
        setLocalVariables([]);
        dispatch(clear(undefined));
        load();

  }


  return (
    <Modal open={open} onClose={close}>
      <Box sx={style}>
        <h2>{scenarioBlock.name}</h2>
        <TempVariableInput types={types} onSave={saveLocalVariables} />
        {blockVariables.map((currentVariable, index) => (
          <ScenarioVariable
            key={index}
            currentVariable={currentVariable}
            acceptedVariables={getAllVariables()}
            scenarioVariable={findDefaultScenarioVariable(currentVariable.id)}
            isLocalVariable = {false}                     
          />
        ))}
        {localVariables.map((localVariable, index) => (
            <ScenarioVariable
              key={index}
              currentVariable={localVariable}
              acceptedVariables={getAllVariables()}
              scenarioVariable={findDefaultScenarioVariable(localVariable.id)}
              isLocalVariable = {true}
              removeLocalVariable={removeLocalVariable}                     
            />
        ))}
        <Button variant="outlined" onClick={save} sx={{ mt: 2 }}>
          Сохранить
        </Button>
      </Box>
    </Modal>
  );
}
