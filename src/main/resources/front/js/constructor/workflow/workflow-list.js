import React, { useState, useEffect  } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css'
import './workflow.sass'
import axios from 'axios'

import { Button } from '@mui/material';
import { Grid } from '@mui/material';
import { Item } from '@mui/material';


export default function WorkflowList() {

    const [workflows, setWorkflows] = useState([]);
    const [pages, setPages] = useState([]);
    const [curPages, setCurPage] = useState([]);
    const [searchTerm, setSearchTerm] = useState('')

    useEffect(() => {
        if (!searchTerm){
            openPage(1)
            console.log("EMTY")
            return;
        }
        const delayDebounceFn = setTimeout(() => {
              console.log(searchTerm)
              openPage(1)
            }, 2000)
        return () => clearTimeout(delayDebounceFn)
    }, [searchTerm])

    useEffect(() => openPage(1), [])

    function openPage(pageNumber){
            console.log("LOAD WITH " + searchTerm)
            axios.get('/api/v1/workflow', { params :{
                pageNumber: pageNumber,
                pageElementsCount: 10,
                projectId: 1,
                workflowName: searchTerm
            }})
            .then((response) => {

                setWorkflows(response.data.workflowCollectionObjectDtos)
                setCurPage(pageNumber)
                let pageButtons = []
                let buttonType;
                for (let i = 1; i <= response.data.pageCount; i++) {
                    if (pageNumber === i){ buttonType = 'contained'} else {buttonType = 'outlined'}
                    pageButtons.push(<Button onClick={() => {openPage(i)}} variant={buttonType} key = {i}>{i}</Button>);
                  }
                setPages(pageButtons)
            })
    //         .catch(function (error) {
    //                console.log('error');
    //              console.log(error);
    //            });
    }



    return (
        <div className='workflow-tree'>
            <input
                  autoFocus
                  type='text'
                  autoComplete='off'
                  className='live-search-field'
                  placeholder='Search here...'
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
            <div>
                {workflows.map((workflow, index) => {
                    return(
                        <div key={workflow.id}>{workflow.name}</div>
                    )
                })}
            </div>
            <div>
                {pages}
            </div>
            <Button>Add new workflow</Button>
        </div>
    )
}