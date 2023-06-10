export const ADD_FUNC = 'ADD_FUNC';
export const REMOVE_FUNC = 'REMOVE_FUNC';


export function setAddFunc(addFunc){
    return {
        type: ADD_FUNC,
        addFunc: addFunc,
    }
}


export function setRemoveFunc(removeFunc){
    return {
        type: REMOVE_FUNC,
        removeFunc: removeFunc,
    }
}

