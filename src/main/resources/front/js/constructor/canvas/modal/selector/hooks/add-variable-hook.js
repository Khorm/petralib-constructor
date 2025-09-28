import { SIMPLE, SCRIPT, SOURCE} from '../value-type';
import { useSelector, useDispatch } from 'react-redux';
import { set } from '../../scenario-variable-slice';
import idGenerator from './id-generator-hook';


export const useVariable = () => {
    const dispatch = useDispatch();
    const scenarioVariablesList = useSelector(state => state.scenarioVariables.list);
    


    function updateSourceScenarioVar (consumerVariableId, producerId, blockVariableId, localId, parentId){
        return update(consumerVariableId, producerId, blockVariableId, SOURCE, localId, parentId, undefined);
    }


    function updateSimpleScenarioVariable(consumerVariableId, producerId, blockVariableId, localId, parentId){
        return update(consumerVariableId, producerId, blockVariableId, SIMPLE, localId, parentId, undefined);
    }

    function updateScriptScenarioVariable(consumerVariableId, script, blockVariableId, localId, parentId){
        return update(consumerVariableId, undefined, blockVariableId, SCRIPT, localId, parentId, script);
    }

    function findDefaultLocalId(blockVariableId, parentId, consumerVariableId){
        return scenarioVariablesList.find(scenarioVar => scenarioVar.blockVariableId === blockVariableId && scenarioVar.parentId === parentId 
            && scenarioVar.consumerVariableId === consumerVariableId
        )?.localId       
    }


    function update(consumerVariableId, producerId, blockVariableId, type, localId, parentId, script){
        if (!localId){
            localId = idGenerator.generateId(blockVariableId);
        }
        const scenarioVariable = {
            localId: localId,
            type: type, 
            consumerVariableId: consumerVariableId,
            producerId: producerId,
            blockVariableId: blockVariableId, 
            parentId: parentId,
            script: script
        }
        add(scenarioVariable);
        return localId;
    }


    function remove(localId, blockId){        
        let localIdsToRemove = new Set([localId]);
        let arrToSearch = scenarioVariablesList;
        do{
            let newArr = [];
            let newIdsToRemove = new Set();
            for (let i = 0; i<arrToSearch.length; i++){
                if (localIdsToRemove.has(arrToSearch[i].localId) && arrToSearch[i].blockVariableId === blockId){                                  
                    continue;
                }else{
                    newArr.push(arrToSearch[i]);
                    if (localIdsToRemove.has(arrToSearch[i].parentId) && arrToSearch[i].blockVariableId === blockId){
                        newIdsToRemove.add(arrToSearch[i].localId)
                    }
                }
            }
            localIdsToRemove = newIdsToRemove;
            arrToSearch = newArr;
        }while(localIdsToRemove.size !== 0)
        console.log("REMOVE : ", arrToSearch);
        dispatch(set(arrToSearch));
    }


    function getScenarioVariable(localId, blockId){
        for (let i = 0; i<scenarioVariablesList.length; i++){
            if (scenarioVariablesList[i].localId === localId && scenarioVariablesList[i].blockVariableId === blockId){
                return scenarioVariablesList[i];
            }
        }
        return undefined;
    }

    
    function updateScenarioVarTypeInheritance(localId, typeInheritance, blockId){  
        let scenarioVariable = getScenarioVariable(localId, blockId);
        var scenarioVar = Object.assign({}, scenarioVariable);     
        scenarioVar.typeInheritance = typeInheritance;
        add(scenarioVar);
    }


    function add(scenarioVariable){
        var scenarioVar = Object.assign({}, scenarioVariable);
        let newArr = [];
        let find = false;
        for (let i = 0; i<scenarioVariablesList.length; i++){
            if (scenarioVariablesList[i].localId == scenarioVariable.localId && 
                scenarioVariablesList[i].blockVariableId === scenarioVariable.blockVariableId){

                newArr.push(scenarioVar);
                find = true;
                continue;
            }else{
                newArr.push(scenarioVariablesList[i]);
            }
        }

        if (!find){
            newArr.push(scenarioVar);
        }
        dispatch(set(newArr));
    }


    return {
        updateSimpleScenarioVariable,
        updateSourceScenarioVar,
        updateScriptScenarioVariable,
        getScenarioVariable,
        updateScenarioVarTypeInheritance,
        findDefaultLocalId,
        remove
    }

}