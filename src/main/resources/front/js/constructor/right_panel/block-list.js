import React, { useState, useEffect  } from 'react'
import axios from 'axios';

import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import SettingsIcon from '@mui/icons-material/Settings';
import TextField from '@mui/material/TextField';
import Pagination from '@mui/material/Pagination';



export default function EntityList({getUrl, createModal, chooseFunc, addFunc}) {

    const PAGE_ELEMENTS_COUNT = 11;

    const [entities, setEntities] = useState([]);
    const [pagesCount, setPagesCount] = useState(1);
    const [curPage, setCurPage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [chosenEntity, setChosenEntity] = useState(-1);

    const [hoverEntity, setHoverEntity] = useState(-1);

    const [optEntity, setOptEntity] = useState({});

    const [openModal, setOpenModal] = useState(false);



    useEffect(() => {
        loadEntities();
    }, [])


    useEffect(() => {
        if (!searchTerm){
            setCurPage(1);
            loadEntities();
            return;
        }
        const delayDebounceFn = setTimeout(() => {
                setCurPage(1);
                loadEntities();
            }, 500)
        return () => clearTimeout(delayDebounceFn)
    }, [searchTerm])

    useEffect(() => {
            loadEntities();
    }, [curPage])

    function handleOpen(e, entity){
        e.stopPropagation();
        setOptEntity(entity);
        setOpenModal(true);
    }

    function handleClose(){
        setOpenModal(false);
        loadEntities();
    }

    function loadEntities(){
        axios.get(getUrl + 'page', { params :{
             pageNumber: curPage,
             pageElementsCount: PAGE_ELEMENTS_COUNT,
             projectId: getProjectId(),
             name: searchTerm
         }})
        .then((response) => {
            setEntities(response.data.blocks)
            setPagesCount(response.data.pageCount)
        }).catch((error) => {
          alert(error)
       })
    }



    function modal(){
        return(
            createModal(optEntity, openModal, handleClose)
        )
    }

    function choose(entity){
        setChosenEntity(entity.id);
        if (chooseFunc !== undefined) chooseFunc(entity.id);
    }

    function getChosenStyle(entity){
        if (entity.id === chosenEntity){
            return 'entity-chosen';
        }
        return 'entity'
    }

    function deleteEntity(){
        if (chosenEntity === -1) return

        axios.delete(getUrl + chosenEntity)
            .then((response) => {
                 loadEntities();
             }).catch((error) => {
                alert(error)
             })
    }

    function addButtonClick(e, entity){
        e.stopPropagation();
         addFunc(entity);
    }

    return (
        <div>
            <TextField value={searchTerm} onChange = {(e) => setSearchTerm(e.target.value)} id="outlined-basic" label="Search" variant="outlined" />
            <div>
                <IconButton aria-label="delete" onClick={deleteEntity}>
                    <DeleteIcon />
                </IconButton>

                    <IconButton aria-label="add" onClick={(e) => handleOpen(e,{})}>
                        <AddIcon />
                    </IconButton>

            </div>


            {entities.map((entity, index) => {
                return(
                    <div key={index} className={getChosenStyle(entity)} onClick={() => choose(entity)}
                    onMouseEnter={() => {setHoverEntity(entity.id)}}>
                        <h4 >{entity.name}</h4>
                        {entity.id === hoverEntity &&
                            <div>
                                {addFunc !== undefined &&
                                    <IconButton aria-label="add" onClick={(e)=> addButtonClick(e, entity)}>
                                        <AddIcon />
                                    </IconButton>
                                }

                                <IconButton aria-label="settings" onClick={(e) => handleOpen(e, entity)}>
                                    <SettingsIcon />
                                </IconButton>
                            </div>
                        }
                    </div>
                )
            })}
            <div style={{marginTop: 'auto' }}>
                <Pagination count={pagesCount} page={curPage} onChange={(event, value) => setCurPage(value)}/>
            </div>
            {openModal &&
                modal()
            }

        </div>
    )
}