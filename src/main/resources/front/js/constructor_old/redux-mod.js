import { createStore, combineReducers } from 'redux';
import {precedentReducer} from './precedent/precedent-reducer';
import {transactionReducer} from './precedent/transaction/transaction-reducer';
import {typeReducer} from './list/type/type-reducer';
import {eventReducer} from './list/event/event-reducer';
import {canvasReducer} from './canvas/redux_reducer/canvas-reducer';

const MULTIPLICITIES = 'MULTIPLICITIES'


const initialState = {
    multiplicities: [],
}

export function multiplicitiesList (multiplicities){
    return {
        type: MULTIPLICITIES,
        multiplicities: multiplicities,
    }
}


const multiplicityReducer = function(state = initialState, action) {
    switch(action.type){

          case MULTIPLICITIES:
              let mltps = Object.assign([], state, {multiplicities: action.multiplicities});
              return mltps;
    }

    return state;
}


const reducers = combineReducers({
    transactionState: transactionReducer,
    precedentState: precedentReducer,
//    typeState: typeReducer,
//    eventState: eventReducer,
//    multiplicityState: multiplicityReducer,
//    canvasState: canvasReducer,
});


const store = createStore(reducers);
export default store;
