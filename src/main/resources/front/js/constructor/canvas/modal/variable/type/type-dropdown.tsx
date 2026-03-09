import React, { useState, useEffect, useRef } from 'react'
import axios from 'axios';

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Popper from '@mui/material/Popper';

import 'bootstrap/dist/css/bootstrap.min.css';
import './dropdown.sass';
import { CTypeFieldDto } from './type-selector';
import { VarFunctionType } from '../enum/variable-function-type';


export interface DropdownInfo{    
    parentTypeId: number;          
    currentTarget: HTMLElement;
    chosenField?: CTypeFieldDto;      
}

/**
 * Пропсы компонента TypeDropdown.
 */
interface TypeDropdownProps {
  /**
   * Объект, управляющий отображением выпадающего списка.
   * Если `undefined` — список скрыт.
   */
  owner: DropdownInfo | undefined;
  /**
   * Функция обратного вызова при выборе или удалении типа.
   * @param field - Выбранное поле или `undefined` при отмене.
   * @param remove - Флаг, указывающий на удаление.
   */
  chooseTypeFunc: (field: CTypeFieldDto | undefined, remove?: boolean) => void;

  
}


/**
 * Выпадающий список выбора поля типа (например, полей объекта).
 *
 * Отображается с помощью MUI `Popper` рядом с кнопкой выбора.
 * Позволяет:
 * - Выбрать поле из списка доступных
 * - Удалить уже выбранное поле
 * - Закрыть список без действий
 *
 * Используется внутри `TypeSelector` для построения цепочки вложенных типов.
 *
 * @component
 * @param {TypeDropdownProps} props - Пропсы компонента.
 * @example
 * <TypeDropdown
 *   owner={dropdownObj}
 *   chooseTypeFunc={(field) => console.log(field)}
 * />
 */
export default function TypeDropdown({ owner, chooseTypeFunc }: TypeDropdownProps) {
  const [anchorEl, setAnchorEl] = React.useState<HTMLElement | null>(null);
  const [dropdownTypesVar, setDropdownTypesVar] = React.useState<CTypeFieldDto[]>([]);

  const open = Boolean(anchorEl);
  const id = open ? 'type-dropdown-popper' : undefined;

  // Следим за изменением `owner` — показываем/скрываем Popper
  useEffect(() => {
    if (!owner) {
      setAnchorEl(null);
      return;
    }

    setAnchorEl(owner.currentTarget);

    // Загружаем поля типа по `parentTypeId`
    axios
      .get<CTypeFieldDto[]>('/api/v1/type/fields/' + owner.parentTypeId, {
        // @ts-ignore
        params: { projectId: getProjectId() },
      })
      .then((response) => {
        setDropdownTypesVar(response.data);
      })
      .catch((error) => {
        console.error('Ошибка загрузки полей типа:', error);
        // Можно добавить уведомление пользователю при необходимости
      });      
  }, [owner]);

  /**
   * Выбор нового поля.
   * @param field - Выбранное поле.
   */
  const select = (field: CTypeFieldDto) => {
    setAnchorEl(null);
    chooseTypeFunc(field);
  };

  /**
   * Удаление текущего поля.
   */
  const remove = () => {
    setAnchorEl(null);

    chooseTypeFunc(owner.chosenField, true);
  };

  /**
   * Закрытие без сохранения.
   */
  const close = () => {
    setAnchorEl(null);
    chooseTypeFunc(undefined);
  };

  return (
    <Popper id={id} open={open} anchorEl={anchorEl} disablePortal placement="bottom-start" sx={{ zIndex: 1300 }}>
      <Box className="dropdown" sx={{ zIndex: 1300 }}>
        <div className="dropdown-content">
          <div className="close-item" onClick={close}>
            Закрыть
          </div>
          {owner?.chosenField && (
            <div className="remove-item" onClick={remove}>
              Удалить
            </div>
          )}
          {dropdownTypesVar.length === 0 ? (
            <div className="empty-state">Нет доступных полей</div>
          ) : (
            dropdownTypesVar.map((field, index) => (
              <div key={index} className="dropdown-item" onClick={() => select(field)}>
                <h3>{field.name}</h3>
              </div>
            ))
          )}
        </div>
      </Box>
    </Popper>
  );
}


// export default function TypeDropdown({ owner, chooseVariableFunc }) {

//     const [anchorEl, setAnchorEl] = React.useState(null);
//     const open = Boolean(anchorEl);
//     const id = open ? 'simple-popper' : undefined;

//     const [dropdownTypesVar, setDropdownTypesVar] = React.useState([]);

//     useEffect(() => {
//         if (!owner) {
//             setAnchorEl(null);
//             return;
//         }
//         setAnchorEl(owner.currentTarget);
//         axios.get('/api/v1/type/fields/' + owner.varTypeId, {
//             params: {
//                 projectId: getProjectId()
//             }
//         })
//             .then((response) => {
//                 setDropdownTypesVar(response.data);
//             }).catch((error) => {
//                 console.log(error);
//                 alert(error)
//             })

//     }, [owner])

//     function select(typeVar) {
//         setAnchorEl(null);
//         chooseVariableFunc(typeVar);
//     }

//     function remove(removedVar) {
//         console.log("removedVar ", removedVar)
//         setAnchorEl(null);
//         chooseVariableFunc(removedVar, true);
//     }

//     function close() {
//         setAnchorEl(null);
//         chooseVariableFunc(undefined);
//     }

//     return (
//         <Popper style={{ zIndex: 2 }} id={id} open={open} anchorEl={anchorEl} disablePortal>
//             <Box className='dropdown'>
//                 <div className='dropdown-content'>
//                     <div className='close-item' onClick={close}>Close</div>
//                     {owner?.currentVar !== undefined &&
//                         <div className='remove-item' onClick={() => remove(owner.currentVar)}>Remove</div>
//                     }
//                     {dropdownTypesVar.map((dropDownTypeVar, index) => {
//                         return (
//                             <div key={index} onClick={() => select(dropDownTypeVar)} ><h3>{dropDownTypeVar.name}</h3></div>
//                         )
//                     })}
//                 </div>
//             </Box>
//         </Popper>
//     )
// }