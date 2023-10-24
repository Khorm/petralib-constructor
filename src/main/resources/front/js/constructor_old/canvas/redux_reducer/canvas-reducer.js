import {ADD_FUNC, REMOVE_FUNC} from './canvas-action-types'

const initialState = {
    addBaseFunc: undefined,
    removeBaseFunc: undefined,
}



export const canvasReducer = function(state = initialState, action) {
    switch(action.type){

          case ADD_FUNC:
              let bss = Object.assign({}, state, {addBaseFunc: action.addFunc});
              return bss;

          case REMOVE_FUNC:
               let rf = Object.assign({}, state, {removeBaseFunc: action.removeFunc});
               return rf;

    }
    return state;
}