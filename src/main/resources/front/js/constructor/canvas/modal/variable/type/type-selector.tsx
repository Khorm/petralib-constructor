import React, { useState, useEffect, useRef } from 'react'
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';
import './type-selector.sass';

import TypeDropdown, { DropdownInfo } from './type-dropdown';
import { CTypeShortDto } from '../../../../right_panel/modal/variable/variable';


/**
 * Интерфейс поля типа (например, поля объекта).
 */
export interface CTypeFieldDto {
  id: number;
  ownerId: number;
  name: string;
  multiplicity: 'SINGLE' | 'COLLECTION'
  fieldType: CTypeShortDto;
}

/**
 * Интерфейс зависимости типа — цепочка выбранных полей.
 */
// export interface TypeInheritance {
//   /**
//    * ID владельца (типа, к которому принадлежит поле).
//    */
//   ownerId: number;
//   /**
//    * ID поля.
//    */
//   fieldId: number;
//   /**
//    * Имя поля.
//    */
//   fieldName: string;
//   /**
//    * Тип поля (следующий тип в цепочке).
//    */
//   fieldType: CTypeShortDto;
// }

/**
 * Пропсы компонента TypeSelector.
 */
interface TypeSelectorProps {
  /**
   * Базовый ID типа, с которого начинается выбор.
   */
  ownerTypeId: number;
  /**
   * Текущий массив выбранных типов (цепочка).
   */
  typeArray: CTypeFieldDto[] | undefined;
  /**
   * Функция для обновления массива типов.
   */
  setNewTypesArray: (types: CTypeFieldDto[]) => void;
}


/**
 * Компонент выбора цепочки типов (например, `user.address.city`).
 *
 * Позволяет последовательно выбирать вложенные поля объектов.
 * Отображает текущую цепочку и кнопку для выбора следующего поля,
 * если оно доступно.
 *
 * Использует выпадающий список `TypeDropdown` для выбора полей.
 *
 * @component
 * @param {TypeSelectorProps} props - Пропсы компонента.
 * @example
 * <TypeSelector
 *   ownerTypeId={1}
 *   typeArray={types}
 *   setNewTypesArray={setTypes}
 * />
 */
export default function TypeSelector({
  ownerTypeId,
  typeArray,
  setNewTypesArray,
}: TypeSelectorProps) {
  const [nextVarAccept, setNextVarAccept] = useState<boolean>(false);
  const [dropdownObj, setDropdownObj] = useState<DropdownInfo | undefined>(undefined);

  /**
   * Проверяет, есть ли у последнего выбранного типа вложенные поля.
   * Если есть — показывает кнопку для продолжения выбора.
   */
  const checkNextVar = () => {
    const fieldTypeId = typeArray && typeArray.length > 0
      ? typeArray[typeArray.length - 1].fieldType.id
      : ownerTypeId;

    axios
      .get<CTypeFieldDto[]>('/api/v1/type/fields/' + fieldTypeId, {
        // @ts-ignore
        params: { projectId: getProjectId() }, 
      })
      .then((response) => {
        setNextVarAccept(response.data.length > 0);
      })
      .catch((error) => {
        console.error('Ошибка загрузки полей типа:', error);
        // alert(`Не удалось загрузить поля: ${error.message}`);
      });
  };

  useEffect(() => {
    checkNextVar();
  }, [typeArray, ownerTypeId]);

  /**
   * Открывает выпадающий список выбора поля.
   *
   * @param ownerTypeId - ID типа, чьи поля будут показаны
   * @param e - Событие клика
   * @param type - Текущее выбранное поле (опционально)
   */
  const handleOpen = (
    parentTypeId: number,
    e: React.MouseEvent<HTMLButtonElement>,
    type?: CTypeShortDto
  ) => {

    const dropdownObj : DropdownInfo = {
      parentTypeId: parentTypeId,      
      currentTarget: e.currentTarget,      
    };
    setDropdownObj(dropdownObj);
  };

  /**
   * Выбирает или удаляет поле из цепочки.
   *
   * @param selectedField - Выбранное поле или `undefined`, если отмена
   * @param remove - Флаг удаления
   */
  const selectField = (selectedField: CTypeFieldDto | undefined, remove = false) => {
    if (!selectedField) {
      setDropdownObj(undefined);
      return;
    }

    // const typeDependency: CTypeFieldDto = {
    //   ownerId: selectedField.ownerId,
    //   fieldId: selectedField.id,
    //   fieldName: selectedField.name,
    //   fieldType: selectedField.fieldType,
    // };

    let newTypeArr: CTypeFieldDto[] = [];

    if (typeArray) {
      let found = false;
      for (const item of typeArray) {
        // Заменяем или удаляем элемент, если совпадает по владельцу или базовому типу
        if (item.ownerId === selectedField.ownerId || selectedField.ownerId === ownerTypeId) {
          if (!remove) {
            newTypeArr.push(selectedField);
          }
          found = true;
        } else {
          newTypeArr.push(item);
        }
      }

      if (!found && !remove) {
        newTypeArr.push(selectedField);
      }
    } else if (!remove) {
      newTypeArr.push(selectedField);
    }

    setNewTypesArray(newTypeArr);
    setDropdownObj(undefined);
  };

  const createTypeName = (type: CTypeFieldDto) => {
    let resultName: string = '';
    if (type.multiplicity === 'COLLECTION') {
      resultName += 'Collection<';
    }
    resultName += type.fieldType.name;
    if (type.multiplicity === 'COLLECTION') {
      resultName += '>';
    }
    resultName += ' : ' + type.name;
    return resultName;
  }



  // Рендер цепочки выбранных полей
  if (typeArray && typeArray.length > 0) {
    return (
      <div className="variable-container">
        <h4>.</h4>
        {typeArray.map((type, index) => (
          <div className="variable-container" key={`${type.id}-${index}`}>
            <button
              className="btn-type"
              type="button"
              onClick={(e) => handleOpen(type.ownerId, e, type.fieldType)}
            >
              {createTypeName(type)}
            </button>
            {index < typeArray.length - 1 && <h4>.</h4>}
            <TypeDropdown owner={dropdownObj} chooseTypeFunc={selectField} />
          </div>
        ))}

        {nextVarAccept && (
          <div className="variable-container">
            <h4>|</h4>
            <button
              className="btn-type add-type-btn"
              type="button"
              onClick={(e) =>
                handleOpen(typeArray[typeArray.length - 1].fieldType.id, e)
              }
            >
              Выбрать переменную...
            </button>
            <TypeDropdown owner={dropdownObj} chooseTypeFunc={selectField} />
          </div>
        )}
      </div>
    );
  }

  // Начальное состояние — выбор первого поля
  return (
    <div>
      {nextVarAccept && (
        <div className="variable-container">
          <h4>.</h4>
          <button
            type="button"
            className="btn-type add-type-btn"
            onClick={(e) => handleOpen(ownerTypeId, e)}
          >
            Выбрать переменную...
          </button>
          <TypeDropdown owner={dropdownObj} chooseTypeFunc={selectField} />
        </div>
      )}
    </div>
  );
}

// export default function TypeSelector({baseTypeId, typeArray, setNewTypesArray}){

//     const [nextVarAccept, setNextVarAccept] = React.useState(false);
//     const [dropdownObj, setDropdownObj] = React.useState(undefined);

//     useEffect(() => {
//          checkNextVar();
//     }, [typeArray])

//     function handleOpen (ownerTypeId, e, type) {
//         setDropdownObj({
//             varTypeId: ownerTypeId,
//             currentTarget: e.currentTarget,
//             currentVar: type,
//         })
//     }

//     function selectField(selectedField, remove = false) {
//         if (selectedField === undefined){
//             setDropdownObj(undefined);
//             return;
//         }
        
//         const typeDependency = {
//             ownerId: selectedField.ownerId,
//             fieldId: selectedField.id,
//             fieldName: selectedField.name,
//             fieldType: selectedField.fieldType,
//         }

//         let newTypeArr = [];
//         if (typeArray !== undefined){
//             let find = false;
//             for (let i = 0; i < typeArray.length; i++){
//                 if (typeDependency.ownerId === typeArray[i].ownerId || typeDependency.ownerId === baseTypeId){
//                     if (!remove){
//                         newTypeArr.push(typeDependency);
//                     }
//                     find = true;
//                     break;
//                 }else{
//                     newTypeArr.push(typeArray[i])
//                 }
//             }

//             if (!find && !remove){
//                 newTypeArr.push(typeDependency);
//             }
//         }else{
//             newTypeArr.push(typeDependency)
//         }
//         setNewTypesArray(newTypeArr);
//         setDropdownObj(undefined);
//     }

//     function checkNextVar(){
//         // if (typeArray === undefined || typeArray.length == 0){
//         //     return;
//         // }

//         let fieldTypeId;
//         if (typeArray === undefined || typeArray.length == 0){
//             fieldTypeId = baseTypeId;
//         }else{
//             fieldTypeId = typeArray[typeArray.length - 1].fieldType.id;
//         }
//         console.log("NEXT FIELDS : ", typeArray, fieldTypeId);
//         axios.get('/api/v1/type/fields/' + fieldTypeId,{ params: {
//             projectId: getProjectId()
//         }})
//         .then((response) => {
            
//             setNextVarAccept(response.data.length > 0);
//         }).catch((error) => {
//             console.log(error);
//             alert(error)
//         })
//     }


//     if (typeArray !== undefined && typeArray.length > 0 ){
//         return(
//             <div className='variable-container'>
//                 {typeArray.map((type, index) => {
//                     return(
//                         <div className='variable-container' key={index}>
//                             <button className='btn-type' type="button" onClick={(e)=>handleOpen(type.ownerId,e, type)}>
//                                 {type.fieldName}
//                             </button>
//                             {index < typeArray.length-1 &&
//                                 <h4>|</h4>
//                             }
//                             <TypeDropdown owner={dropdownObj} chooseVariableFunc={selectField}/>
//                         </div>
//                     )
//                 })}
//                 {nextVarAccept &&
//                     <div className='variable-container'>
//                     <h4>|</h4>
//                     <button className='btn-type add-type-btn' type="button" onClick={(e)=>handleOpen(typeArray[typeArray.length - 1].fieldType.id,e)}>
//                         Select a variable...
//                     </button>
//                     <TypeDropdown owner={dropdownObj} chooseVariableFunc={selectField}/>
//                     </div>
//                 }

//             </div>
//         )
//     }else{
        
//         return(
//             <div>
//             {nextVarAccept && 
//                 <div className='variable-container'>
//                     <button type="button" className='btn-type add-type-btn' onClick={(e)=>handleOpen(baseTypeId,e)}>
//                         Select a variable...
//                     </button>
//                     <TypeDropdown owner={dropdownObj} chooseVariableFunc={selectField}/>
//                 </div>
//             }
//             </div>
//         )
        

//     }
// }