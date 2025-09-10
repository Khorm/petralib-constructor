import { createSlice } from '@reduxjs/toolkit'

export const scenarioVariablesSlice = createSlice({
  name: 'scenarioVariables',
  initialState: {
    list: [],
  },

  reducers: {

    add: (state, action) => {

        action.payload = action.payload.getData();

        let newArr = [];
        let find = false;
        for (let i = 0; i<state.list.length; i++){
            if (state.list[i].consumerVariableId === action.payload.consumerVariableId){
                newArr.push(action.payload);
                find = true;
                console.log("ADD ", action.payload)
            }else{
                newArr.push(state.list[i]);
            }
        }

        if (!find){
            newArr.push(action.payload);
            console.log("ADD ", action.payload)
        }

        state.list = newArr;
    },

    remove: (state, action) => {
        console.log("REMOVE")
        console.log(action)
        if (!action.payload){
            return;
        }
        action.payload = action.payload.getData();

        let newArr = []
        for (let i = 0; i<state.list.length; i++){
            if ( state.list[i].consumerVariableId === action.payload.consumerVariableId){
                continue;
            }else{
                newArr.push(state.list[i]);
            }
        }
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