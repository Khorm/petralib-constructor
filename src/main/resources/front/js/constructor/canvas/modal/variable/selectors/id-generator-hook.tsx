// import React, { useState, useEffect, useRef } from 'react'


const generator = () => {
    const counterRef = new Map();

    const createId = (blockVariableId: number) => {
        let id = counterRef.get(blockVariableId);
        if (id === undefined){
            id = 0;
        }
        id = id+1;
        counterRef.set(blockVariableId, id);
        return id;
    }

    const setDefaultId = (basVariableId: number, localId: number) => {
        counterRef.set(basVariableId, localId);
    }


    function setId(basVariableId: number, localId: number){
        setDefaultId(basVariableId, localId);
    }

    function generateId(blockVariableId: number){
        return createId(blockVariableId);
    }

    return {
        setId,
        generateId
    }

    
}

const idGenerator = generator();
export default idGenerator;


