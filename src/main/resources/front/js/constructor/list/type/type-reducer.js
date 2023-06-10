import {TYPES} from './type-action-types'

const initialState = {
    types: [],

}



export const typeReducer = function(state = initialState, action) {
    switch(action.type){

          case TYPES:
              let tps = Object.assign([], state, {types: action.types});
              return tps;
    }

    return state;
}