import {ValueType} from '../value-type';

import { useSelector, useDispatch } from 'react-redux';
import { set } from '../../scenario-variable-slice';
import idGenerator from './id-generator-hook';

import { ScenarioVariableDto } from '../../scenario-block-modal';
import { CTypeFieldDto } from '../type/type-selector';


interface VariableFuncs {
  updateSimpleScenarioVariable: ( consumerVariableId: number,
    producerId: number,
    blockVariableId: number,
    parentId: number,
    localId: number,
    ) => ScenarioVariableDto;
    
  // add: (scenarioVariable: ScenarioVariableDto) => void;
  updateScenarioVarTypeInheritance : (
    localId: number,
    typeInheritance: CTypeFieldDto[],
    blockId: number
  ) => ScenarioVariableDto;

  updateSourceScenarioVar : (
    consumerVariableId: number,
    producerId: number,
    blockVariableId: number,
    localId: number,
    parentId: number 
  ) => ScenarioVariableDto;

  // remove : (localId: number, blockVariableId: number) => void 
}

/**
 * Хук для управления переменными сценария.
 *
 * Позволяет:
 * - Создавать/обновлять переменные (Simple, Source, Script)
 * - Удалять переменные и их вложенные зависимости
 * - Находить переменные по ID
 * - Управлять наследованием типов
 *
 * @returns {Object} Объект с функциями управления переменными
 *
 * @example
 * const { updateSourceScenarioVar, remove } = useVariable();
 */
export const useVariable = () : VariableFuncs => {
    const dispatch = useDispatch();
      const scenarioVariablesList = useSelector(
            (state: any) => state.scenarioVariables.list
        ) as ScenarioVariableDto[];
    

     /**
   * Обновляет или создаёт переменную типа "Source".
   *
   * @param consumerVariableId - ID переменной-потребителя
   * @param producerId - ID переменной-источника
   * @param blockVariableId - ID переменной блока
   * @param localId - Локальный ID (если не задан — генерируется)
   * @param parentId - ID родительской переменной
   * @returns {number} Использованный localId
   */
  const updateSourceScenarioVar = (
    consumerVariableId: number,
    producerId: number,
    blockVariableId: number,
    localId: number,
    parentId: number 
  ): ScenarioVariableDto => {
    return update(
      consumerVariableId,
      producerId,
      blockVariableId,
      'SOURCE',
      localId,
      parentId,
      undefined
    );
  };

  /**
   * Обновляет или создаёт переменную типа "Simple".
   *
   * @param consumerVariableId - ID переменной-потребителя
   * @param producerId - ID производителя (например, значение по умолчанию)
   * @param blockVariableId - ID переменной блока
   * @param localId - Локальный ID (если не задан — генерируется)
   * @param parentId - ID родительской переменной
   * @returns {number} Использованный localId
   */
  const updateSimpleScenarioVariable = (
    consumerVariableId: number,
    producerId: number,
    blockVariableId: number,
    parentId: number,
    localId: number,    
  ): ScenarioVariableDto => {
    return update(
      consumerVariableId,
      producerId,
      blockVariableId,
      'SIMPLE',
      localId,
      parentId,
      undefined
    );
  };

  /**
   * Обновляет или создаёт переменную типа "Script".
   *
   * @param consumerVariableId - ID переменной-потребителя
   * @param script - Текст скрипта
   * @param blockVariableId - ID переменной блока
   * @param localId - Локальный ID (если не задан — генерируется)
   * @param parentId - ID родительской переменной
   * @returns {number} Использованный localId
   */
  const updateScriptScenarioVariable = (
    consumerVariableId: number,
    script: string,
    blockVariableId: number,
    localId: number,
    parentId: number 
  ): ScenarioVariableDto => {
    return update(
      consumerVariableId,
      undefined,
      blockVariableId,
      'SCRIPT',
      localId,
      parentId,
      script
    );
  };

  /**
   * Находит значение `localId` по умолчанию для указанной переменной.
   *
   * @param blockVariableId - ID переменной блока
   * @param parentId - ID родительской переменной
   * @param consumerVariableId - ID потребителя
   * @returns {number | undefined} Найденный localId или undefined
   */
  const findDefaultLocalId = (
    blockVariableId: number,
    parentId: number,
    consumerVariableId: number
  ): number | undefined => {
    return scenarioVariablesList.find(
      (sv) =>
        sv.blockVariableId === blockVariableId &&
        sv.parentId === parentId &&
        sv.consumerVariableId === consumerVariableId
    )?.localId;
  };

  /**
   * Обновляет или добавляет переменную сценария.
   *
   * @private
   */
  function update(
    consumerVariableId: number,
    producerId: number | undefined,
    blockVariableId: number,
    type: ValueType,
    localId: number,
    parentId: number,
    script?: string
  ): ScenarioVariableDto {

    // const id = localId ?? idGenerator.generateId(blockVariableId);

    const scenarioVariable: ScenarioVariableDto = {
      localId,
      consumerVariableId,
      producerId,
      blockVariableId,
      parentId,
      type,
      script,
      typeInheritance: []
    };

    // add(scenarioVariable);
    return scenarioVariable;
  }

  /**
   * Удаляет переменную и все её вложенные зависимости (по цепочке parentId).
   *
   * @param localId - ID удаляемой переменной
   * @param blockVariableId - ID переменной блока
   */
  const remove = (localId: number, blockVariableId: number): void => {
    let localIdsToRemove = new Set<number>([localId]);
    let arrToSearch = [...scenarioVariablesList];

    // Удаляем переменные рекурсивно по цепочке зависимостей
    while (localIdsToRemove.size > 0) {
      const newArr: ScenarioVariableDto[] = [];
      const newIdsToRemove = new Set<number>();

      for (const item of arrToSearch) {
        if (
          localIdsToRemove.has(item.localId) &&
          item.blockVariableId === blockVariableId
        ) {
          continue; // Пропускаем удаляемые
        } else {
          newArr.push(item);
          // Если родитель удаляется — добавляем и этот ID
          if (
            localIdsToRemove.has(item.parentId) &&
            item.blockVariableId === blockVariableId
          ) {
            newIdsToRemove.add(item.localId);
          }
        }
      }

      arrToSearch = newArr;
      localIdsToRemove = newIdsToRemove;
    }

    dispatch(set(arrToSearch));
  };

  /**
   * Находит переменную сценария по `localId` и `blockId`.
   *
   * @param localId - Локальный ID переменной
   * @param blockId - ID блока
   * @returns {ScenarioVariable | undefined} Найденная переменная или undefined
   */
  const getScenarioVariable = (
    localId: number,
    blockId: number
  ): ScenarioVariableDto | undefined => {
    return scenarioVariablesList.find(
      (sv) => sv.localId === localId && sv.blockVariableId === blockId
    );
  };

  /**
   * Обновляет признак наследования типа для переменной.
   *
   * @param localId - Локальный ID переменной
   * @param typeInheritance - Значение флага наследования
   * @param blockId - ID блока
   */
  const updateScenarioVarTypeInheritance = (
    localId: number,
    typeInheritance: CTypeFieldDto[],
    blockId: number
  ): ScenarioVariableDto => {
    const scenarioVariable :ScenarioVariableDto = getScenarioVariable(localId, blockId);   

    const updatedVar = { ...scenarioVariable, typeInheritance };
    return updatedVar;
    // add(updatedVar);
  };

  /**
   * Добавляет или обновляет переменную в списке.
   *
   * Если переменная с таким `localId` и `blockVariableId` уже есть — заменяет.
   * Иначе — добавляет новую.
   *
   * @param scenarioVariable - Объект переменной сценария
   */
  const add = (scenarioVariable: ScenarioVariableDto): void => {
    const updatedList = scenarioVariablesList.map((sv) =>
      sv.localId === scenarioVariable.localId &&
      sv.blockVariableId === scenarioVariable.blockVariableId
        ? { ...scenarioVariable }
        : sv
    );

    // Если не найдена — добавляем
    if (
      !scenarioVariablesList.some(
        (sv) =>
          sv.localId === scenarioVariable.localId &&
          sv.blockVariableId === scenarioVariable.blockVariableId
      )
    ) {
      updatedList.push(scenarioVariable);
    }

    dispatch(set(updatedList));
  };

  return {
    updateSimpleScenarioVariable,
    // add,
    updateScenarioVarTypeInheritance,
    updateSourceScenarioVar,
    // updateSourceScenarioVar,
    // updateScriptScenarioVariable,
    // getScenarioVariable,
    // updateScenarioVarTypeInheritance,
    // findDefaultLocalId,
    // remove,     
  };

    // function updateSourceScenarioVar (consumerVariableId, producerId, blockVariableId, localId, parentId){
    //     return update(consumerVariableId, producerId, blockVariableId, SOURCE, localId, parentId, undefined);
    // }


    // function updateSimpleScenarioVariable(consumerVariableId, producerId, blockVariableId, localId, parentId){
    //     return update(consumerVariableId, producerId, blockVariableId, SIMPLE, localId, parentId, undefined);
    // }

    // function updateScriptScenarioVariable(consumerVariableId, script, blockVariableId, localId, parentId){
    //     return update(consumerVariableId, undefined, blockVariableId, SCRIPT, localId, parentId, script);
    // }

    // function findDefaultLocalId(blockVariableId, parentId, consumerVariableId){
    //     return scenarioVariablesList.find(scenarioVar => scenarioVar.blockVariableId === blockVariableId && scenarioVar.parentId === parentId 
    //         && scenarioVar.consumerVariableId === consumerVariableId
    //     )?.localId       
    // }


    // function update(consumerVariableId, producerId, blockVariableId, type, localId, parentId, script){
    //     if (!localId){
    //         localId = idGenerator.generateId(blockVariableId);
    //     }
    //     const scenarioVariable = {
    //         localId: localId,
    //         type: type, 
    //         consumerVariableId: consumerVariableId,
    //         producerId: producerId,
    //         blockVariableId: blockVariableId, 
    //         parentId: parentId,
    //         script: script
    //     }
    //     add(scenarioVariable);
    //     return localId;
    // }


    // function remove(localId, blockId){        
    //     let localIdsToRemove = new Set([localId]);
    //     let arrToSearch = scenarioVariablesList;
    //     do{
    //         let newArr = [];
    //         let newIdsToRemove = new Set();
    //         for (let i = 0; i<arrToSearch.length; i++){
    //             if (localIdsToRemove.has(arrToSearch[i].localId) && arrToSearch[i].blockVariableId === blockId){                                  
    //                 continue;
    //             }else{
    //                 newArr.push(arrToSearch[i]);
    //                 if (localIdsToRemove.has(arrToSearch[i].parentId) && arrToSearch[i].blockVariableId === blockId){
    //                     newIdsToRemove.add(arrToSearch[i].localId)
    //                 }
    //             }
    //         }
    //         localIdsToRemove = newIdsToRemove;
    //         arrToSearch = newArr;
    //     }while(localIdsToRemove.size !== 0)
    //     console.log("REMOVE : ", arrToSearch);
    //     dispatch(set(arrToSearch));
    // }


    // function getScenarioVariable(localId, blockId){
    //     for (let i = 0; i<scenarioVariablesList.length; i++){
    //         if (scenarioVariablesList[i].localId === localId && scenarioVariablesList[i].blockVariableId === blockId){
    //             return scenarioVariablesList[i];
    //         }
    //     }
    //     return undefined;
    // }

    
    // function updateScenarioVarTypeInheritance(localId, typeInheritance, blockId){  
    //     let scenarioVariable = getScenarioVariable(localId, blockId);
    //     var scenarioVar = Object.assign({}, scenarioVariable);     
    //     scenarioVar.typeInheritance = typeInheritance;
    //     add(scenarioVar);
    // }


    // function add(scenarioVariable){
    //     var scenarioVar = Object.assign({}, scenarioVariable);
    //     let newArr = [];
    //     let find = false;
    //     for (let i = 0; i<scenarioVariablesList.length; i++){
    //         if (scenarioVariablesList[i].localId == scenarioVariable.localId && 
    //             scenarioVariablesList[i].blockVariableId === scenarioVariable.blockVariableId){

    //             newArr.push(scenarioVar);
    //             find = true;
    //             continue;
    //         }else{
    //             newArr.push(scenarioVariablesList[i]);
    //         }
    //     }

    //     if (!find){
    //         newArr.push(scenarioVar);
    //     }
    //     dispatch(set(newArr));
    // }


    // return {
    //     updateSimpleScenarioVariable,
    //     updateSourceScenarioVar,
    //     updateScriptScenarioVariable,
    //     getScenarioVariable,
    //     updateScenarioVarTypeInheritance,
    //     findDefaultLocalId,
    //     remove
    // }

}