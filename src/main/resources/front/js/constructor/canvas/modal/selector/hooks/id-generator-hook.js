import React, { useState, useEffect, useRef } from 'react'


const generator = () => {
    const counterRef = new Map();
    // const isGeneratingRef = false;

    const createId = (blockVariableId) => {
        // while (isGeneratingRef.current) {
        //     await new Promise(resolve => setTimeout(resolve, 0));
        // }
        
        // isGeneratingRef = true;
        // try {
            let id = counterRef.get(blockVariableId);
            if (id === undefined){
                id = 0;
            }
            id = id+1;
            counterRef.set(blockVariableId, id);
            return id;
        // } finally {
        //     isGeneratingRef = false;
        // }

    }

    const setDefaultId = (basVariableId, localId) => {
        // while (isGeneratingRef.current) {
        //     await new Promise(resolve => setTimeout(resolve, 0));
        // }

        // isGeneratingRef = true;
        // try {
            counterRef.set(basVariableId, localId);
        // } finally {
        //     isGeneratingRef.current = false;
        // }
    }


    function setId(basVariableId, localId){
        setDefaultId(basVariableId, localId);
    }

    function generateId(blockVariableId){
        // (async () => {
        //     try {
        //         const result = await setDefaultId(123, 456);
        //         console.log('Получен ID:', result);
        //     } catch (error) {
        //         console.error('Ошибка:', error);
        //     }
        //     })();
        return createId(blockVariableId);
    }

    return {
        setId,
        generateId
    }

    
}

const idGenerator = generator();
export default idGenerator;


