export const PRECEDENT_TRANSACTIONS = 'PRECEDENT_TRANSACTIONS'
export const CURRENT_TRANSACTION = 'CURRENT_TRANSACTION'


export function transactionsList (transactions){
    return {
        type: PRECEDENT_TRANSACTIONS,
        precedentTransactions: precedentTransactions,
    }
}


export function currentTransaction (transactionId){
    return {
        type: CURRENT_TRANSACTION,
        currentTransaction: transactionId,
    }
}

//export function basePrecedentsList (basePrecedents){
//    return {
//        type: BASE_PRECEDENTS,
//        basePrecedents: basePrecedents,
//    }
//}
//
//
//export function setCurrentPrecedentFunc(func){
//    return{
//        type: SET_CURRENT_PRECEDENT_FUNC,
//        setCurrentPrecedent: func
//    }
//}
