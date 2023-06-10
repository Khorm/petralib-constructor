import {PRECEDENTS, CURRENT_PRECEDENT} from './precedent-action-types'

const initialState = {
    precedents: [],
    currentPrecedent: -1,
}



export const precedentReducer = function(state = initialState, action) {
    switch(action.type){

          case PRECEDENTS:
              let prec = Object.assign({}, state, {precedents: action.precedents});
              return prec;

          case CURRENT_PRECEDENT:
               let curPrec = Object.assign({}, state, {currentPrecedent: action.currentPrecedent});
               return curPrec;
    }
    return state;
}