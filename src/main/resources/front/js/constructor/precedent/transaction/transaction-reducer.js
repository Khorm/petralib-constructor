import {PRECEDENT_TRANSACTIONS, CURRENT_TRANSACTION} from './transaction-action-types'

const initialState = {
    precedentTransactions: [],
    currentTransaction: -1,
}



export const transactionReducer = function(state = initialState, action) {
    switch(action.type){

          case PRECEDENT_TRANSACTIONS:
              let trans = Object.assign({}, state, {precedentTransactions: action.precedentTransactions});
              return trans;

          case CURRENT_TRANSACTION:
               let curTrans = Object.assign({}, state, {currentTransaction: action.currentTransaction});
               return curTrans;
    }
    return state;
}