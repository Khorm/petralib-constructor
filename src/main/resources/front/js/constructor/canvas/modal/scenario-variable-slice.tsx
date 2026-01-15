import { createSlice } from '@reduxjs/toolkit'
import { ScenarioVariableDto } from './scenario-block-modal';

export const scenarioVariablesSlice = createSlice({
  name: 'scenarioVariables',
  initialState: {
    list: [],
  },

  reducers: {

    add: (state, action) => {        
        const payloadVar : ScenarioVariableDto = action.payload       

        let newArr :ScenarioVariableDto[] = [];
        let find : boolean = false;
        for (let i = 0; i<state.list.length; i++){
            if (state.list[i].consumerVariableId === payloadVar.consumerVariableId 
                    && state.list[i].blockVariableId === payloadVar.blockVariableId){
                newArr.push(payloadVar);
                find = true;
                console.log("UPDATE ", payloadVar)
            }else{
                newArr.push(state.list[i]);
            }
        }

        if (!find){
            newArr.push(payloadVar);
            console.log("ADD ",payloadVar)
        }

        state.list = newArr;
    },

    remove: (state, action) => {
        
        if (!action.payload){
            return;
        }
        action.payload = action.payload;

        let newArr = []
        for (let i = 0; i<state.list.length; i++){
            if ( state.list[i].consumerVariableId === action.payload.consumerVariableId
                    && state.list[i].blockVariableId === action.payload.blockVariableId
            ){
                continue;
            }else{
                newArr.push(state.list[i]);
            }
        }
        console.log("REMOVE : ", newArr)
        
        state.list = newArr;

    },

    removeAll: (state, action) => {

            action.payload = action.payload.map(remVar => remVar.getData())

            let removedVars = new Set(action.payload.map(remVar => remVar.consumerVariableId));
            console.log("REMOVE ALL " , action.payload)
            console.log(action)

            let newArr = []
            for (let i = 0; i<state.list.length; i++){
               if (removedVars.has(state.list[i].consumerVariableId)){
                   continue;
               }else{
                   newArr.push(state.list[i]);
               }
            }
            state.list = newArr;
       },

    set: (state, action) => {
        state.list = action.payload;
    },

    clear: (state, action) => {
       state.list = [];
    },

  },
})

export const { add, remove, set, removeAll, clear } = scenarioVariablesSlice.actions

export default scenarioVariablesSlice.reducer