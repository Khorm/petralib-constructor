export const PRECEDENTS = 'PRECEDENTS'
export const CURRENT_PRECEDENT = 'CURRENT_PRECEDENT'
//export const SET_CURRENT_PRECEDENT_SET_FUNC = 'SET_CURRENT_PRECEDENT_SET_FUNC';


export function precedentsList (precedents){
    return {
        type: PRECEDENTS,
        precedents: precedents,
    }
}


export function currentPrecedent (precedentId){
    return {
        type: CURRENT_PRECEDENT,
        currentPrecedent: precedentId,
    }
}

//export function basePrecedentsList (basePrecedents){
//    return {
//        type: BASE_PRECEDENTS,
//        basePrecedents: basePrecedents,
//    }
//}


//export function setCurrentPrecedentSetFunc(func){
//    return{
//        type: SET_CURRENT_PRECEDENT_SET_FUNC,
//        setCurrentPrecedent: func
//    }
//}