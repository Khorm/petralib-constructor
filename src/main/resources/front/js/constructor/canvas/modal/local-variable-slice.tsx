import { createSlice } from '@reduxjs/toolkit'
import { VariableDto } from './scenario-block-modal';


export const localVariablesSlice  = createSlice({
  name: 'localVariables',
  initialState: {
    list: [],
  } ,

  reducers: {

    add: (state, action) => {        
        const payloadVar : VariableDto = action.payload       

        let newArr :VariableDto[] = [];
        let find : boolean = false;
        for (let i = 0; i<state.list.length; i++){
            if (state.list[i].name === payloadVar.name){
                newArr.push(payloadVar);
                find = true;                
            }else{
                newArr.push(state.list[i]);
            }
        }

        if (!find){
            newArr.push(payloadVar);            
        }

        state.list = newArr;
    },

    remove: (state, action) => {
        
        if (!action.payload){
            return;
        }
        const payloadVar : VariableDto = action.payload;

        let newArr = []
        for (let i = 0; i<state.list.length; i++){
            if ( state.list[i].name === payloadVar.name){
                continue;
            }else{
                newArr.push(state.list[i]);
            }
        }        
        state.list = newArr;
    },


    setLocalVars: (state, action) => {
        state.list = action.payload;
    },

    clear: (state, action) => {
       state.list = [];
    },

  },
})

export const { add, remove, setLocalVars,  clear } = localVariablesSlice.actions

export default localVariablesSlice.reducer