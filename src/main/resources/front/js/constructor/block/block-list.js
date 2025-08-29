import React, { useState, useEffect  } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css'
import './workflow.sass'
import axios from 'axios'

import { Button } from '@mui/material';
import { Grid } from '@mui/material';
import { Item } from '@mui/material';

import Block from './block';

//лист блоков
export default function BlockList({blockTypeUrl, choose, chosen}) {

    const [blocks, setBlocks] = useState([]);
    const [pages, setPages] = useState([]);
    const [curPages, setCurPage] = useState([]);
    const [searchTerm, setSearchTerm] = useState('')


    useEffect(() => {
        if (!searchTerm){
            openPage(1)
            return;
        }
        const delayDebounceFn = setTimeout(() => {
              openPage(1)
            }, 1000)
        return () => clearTimeout(delayDebounceFn)
    }, [searchTerm])

    useEffect(() => openPage(1), [])

    function openPage(pageNumber){
        console.log(blockTypeUrl)
            axios.get('/api/v1/'+blockTypeUrl+'/page', { params :{
                pageNumber: pageNumber,
                pageElementsCount: 10,
                projectId: getProjectId(),
                name: searchTerm
            }})
            .then((response) => {
                console.log(response.data)
                setBlocks(response.data.blocks)
                setCurPage(pageNumber)
                let pageButtons = []
                let buttonType;
                for (let i = 1; i <= response.data.pageCount; i++) {
                    if (pageNumber === i){ buttonType = 'contained'} else {buttonType = 'outlined'}
                    pageButtons.push(<Button onClick={() => {openPage(i)}} variant={buttonType} key = {i}>{i}</Button>);
                }
                setPages(pageButtons)
            })
             .catch(function (error) {
                  console.log(error);
                  alert("Loading error. ", error.response.data);
                });
    }

    function createBlock () {
        window.location.href = '/projects/' + getProjectId() + '/constructor/'+blockTypeUrl;
    }

    function chooseBlock(e){
        if (choose){
           choose(e);
        }
    }

    function checkChosen(block){
        if (!chosen) return false;
        console.log("chosen" , chosen.id, block.id)
        if (block.id === chosen.id){
            return true
        }
        return false;
    }

    return (
        <div className='block-tree'>
            <input
                  autoFocus
                  type='text'
                  autoComplete='off'
                  className='live-search-field'
                  placeholder='Search here...'
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
            <div>
                {blocks.map((block, index) => {
                    return(
                        <Block key={block.id} block={block} url={blockTypeUrl} onChoose={chooseBlock}
                            chosen={checkChosen(block)}/>
                    )
                })}
            </div>
            <div>
                {pages}
            </div>
            <Button onClick={createBlock}>Add</Button>
        </div>
    )
}