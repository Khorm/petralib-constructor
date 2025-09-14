import React, { useState, useEffect, useRef } from 'react'
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';
import './type-selector.sass';

import TypeDropdown from './dropdown';


//baseTypeId - ���� ���� �� ������� ���� ������ ��� ��������
//typeArray - ������ ��������� ����������
//setNewTypesArray - ������� ����������� ����� ���������
export default function TypeSelector({baseTypeId, typeArray, setNewTypesArray}){

    const [nextVarAccept, setNextVarAccept] = React.useState(false);
    const [dropdownObj, setDropdownObj] = React.useState(undefined);

    useEffect(() => {
         checkNextVar();
    }, [typeArray])

    function handleOpen (ownerTypeId, e, type) {
        setDropdownObj({
            varTypeId: ownerTypeId,
            currentTarget: e.currentTarget,
            currentVar: type,
        })
    }

    function selectField(selectedField, remove = false) {
        if (selectedField === undefined){
            setDropdownObj(undefined);
            return;
        }
        
        const typeDependency = {
            ownerId: selectedField.ownerId,
            fieldId: selectedField.id,
            fieldName: selectedField.name,
            fieldType: selectedField.fieldType,
        }

        let newTypeArr = [];
        if (typeArray !== undefined){
            let find = false;
            for (let i = 0; i < typeArray.length; i++){
                if (typeDependency.ownerId === typeArray[i].ownerId || typeDependency.ownerId === baseTypeId){
                    if (!remove){
                        newTypeArr.push(typeDependency);
                    }
                    find = true;
                    break;
                }else{
                    newTypeArr.push(typeArray[i])
                }
            }

            if (!find && !remove){
                newTypeArr.push(typeDependency);
            }
        }else{
            newTypeArr.push(typeDependency)
        }
        setNewTypesArray(newTypeArr);
        setDropdownObj(undefined);
    }

    function checkNextVar(){
        // if (typeArray === undefined || typeArray.length == 0){
        //     return;
        // }

        let fieldTypeId;
        if (typeArray === undefined || typeArray.length == 0){
            fieldTypeId = baseTypeId;
        }else{
            fieldTypeId = typeArray[typeArray.length - 1].fieldType.id;
        }
        console.log("NEXT FIELDS : ", typeArray, fieldTypeId);
        axios.get('/api/v1/type/fields/' + fieldTypeId,{ params: {
            projectId: getProjectId()
        }})
        .then((response) => {
            
            setNextVarAccept(response.data.length > 0);
        }).catch((error) => {
            console.log(error);
            alert(error)
        })
    }


    if (typeArray !== undefined && typeArray.length > 0 ){
        return(
            <div className='variable-container'>
                {typeArray.map((type, index) => {
                    return(
                        <div className='variable-container' key={index}>
                            <button className='btn-type' type="button" onClick={(e)=>handleOpen(type.ownerId,e, type)}>
                                {type.fieldName}
                            </button>
                            {index < typeArray.length-1 &&
                                <h4>|</h4>
                            }
                            <TypeDropdown owner={dropdownObj} chooseVariableFunc={selectField}/>
                        </div>
                    )
                })}
                {nextVarAccept &&
                    <div className='variable-container'>
                    <h4>|</h4>
                    <button className='btn-type add-type-btn' type="button" onClick={(e)=>handleOpen(typeArray[typeArray.length - 1].fieldType.id,e)}>
                        Select a variable...
                    </button>
                    <TypeDropdown owner={dropdownObj} chooseVariableFunc={selectField}/>
                    </div>
                }

            </div>
        )
    }else{
        
        return(
            <div>
            {nextVarAccept && 
                <div className='variable-container'>
                    <button type="button" className='btn-type add-type-btn' onClick={(e)=>handleOpen(baseTypeId,e)}>
                        Select a variable...
                    </button>
                    <TypeDropdown owner={dropdownObj} chooseVariableFunc={selectField}/>
                </div>
            }
            </div>
        )
        

    }
}