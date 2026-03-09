import {ValueType} from '../enum/value-type';

import { useSelector } from 'react-redux';

import { ScenarioVariableDto } from '../../scenario-block-modal';
import { CTypeFieldDto } from '../type/type-selector';
import { VarFunctionType } from '../enum/variable-function-type';


interface VariableFuncs {
  updateSimpleScenarioVariable: ( consumerVariableId: number,
    producerId: number,
    blockVariableId: number,
    functionType: VarFunctionType
    ) => ScenarioVariableDto;
    
  updateScenarioVarTypeInheritance : (  
    functionType: VarFunctionType,  
    typeInheritance: CTypeFieldDto[],
    blockId: number
  ) => ScenarioVariableDto;

  updateSourceScenarioVar : (
    consumerVariableId: number,
    producerId: number,
    blockVariableId: number,
    functionType: VarFunctionType
  ) => ScenarioVariableDto;

   updateScriptScenarioVariable : (
    consumerVariableId: number,
    script: string,
    blockVariableId: number,
    functionType: VarFunctionType
  )=> ScenarioVariableDto; 

}


export const useVariable = () : VariableFuncs => {


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
    functionType: VarFunctionType,

  ): ScenarioVariableDto => {
    return update(
      consumerVariableId,
      producerId,
      blockVariableId,
      'SOURCE',
      functionType,
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
    functionType: VarFunctionType,   
  ): ScenarioVariableDto => {
    return update(
      consumerVariableId,
      producerId,
      blockVariableId,
      'SIMPLE',
      functionType,
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
    functionType: VarFunctionType,
  ): ScenarioVariableDto => {
    return update(
      consumerVariableId,
      undefined,
      blockVariableId,
      'SCRIPT',
      functionType,
      script
    );
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
    functionType: VarFunctionType,
    script?: string
  ): ScenarioVariableDto {
    const scenarioVariable: ScenarioVariableDto = {
      
      consumerVariableId,
      producerId,
      blockVariableId,      
      type,
      script,
      functionType,
      typeInheritance: []
    };

    return scenarioVariable;
  }



  /**
   * Находит переменную сценария по `localId` и `blockId`.
   *
   * @param localId - Локальный ID переменной
   * @param blockId - ID блока
   * @returns {ScenarioVariable | undefined} Найденная переменная или undefined
   */
  const getScenarioVariable = (
    functionType: VarFunctionType,
    blockId: number
  ): ScenarioVariableDto | undefined => {
    return scenarioVariablesList.find(
      (sv) => sv.functionType === functionType && sv.blockVariableId === blockId
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
    functionType: VarFunctionType,
    typeInheritance: CTypeFieldDto[],
    blockId: number
  ): ScenarioVariableDto => {
    const scenarioVariable :ScenarioVariableDto = getScenarioVariable(functionType, blockId);   

    const updatedVar = { ...scenarioVariable, typeInheritance };
    updatedVar.scenarioVariableId = undefined;
    return updatedVar;
  };


  return {
    updateSimpleScenarioVariable,
    updateScenarioVarTypeInheritance,
    updateSourceScenarioVar,
    updateScriptScenarioVariable,  
  };


}