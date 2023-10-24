import React, { useState, useEffect  } from 'react'

import { useSelector, useDispatch } from 'react-redux'
import { set, update } from '../workflowsSlice'

import 'bootstrap/dist/css/bootstrap.min.css'
import './workflow.sass'
import axios from 'axios'

import { TreeView } from '@mui/lab';
import { TreeItem } from '@mui/lab';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';

export default function WorkflowTree() {

    const metaWorkflows = useSelector((state) => state.workflows.value)
    const dispatch = useDispatch()

    useEffect(() => {
        axios.get('/api/v1/workflow/high-level', { params :{
            pageNumber: 1,
            pageElementsCount: 10,
            projectId: 1
        }})
            .then((response) => {
                dispatch(set(response.data.workflowCollectionObjectDtos))
            })
//         .catch(function (error) {
//                console.log('error');
//              console.log(error);
//            });
    }, [])

    function loadChildren(listMetaWorkflow){
        if (listMetaWorkflow.childrenLoaded) return;
        console.log("loadChildren")
        console.log(listMetaWorkflow)
        axios.get('/api/v1/workflow/children', { params :{
            parentId: listMetaWorkflow.workflow.id,
            projectId: 1
        }})
        .then((response) => {
            let children = response.data.map((childWorkflow) => {
                return {
                    workflow: childWorkflow,
                    children: [],
                    childrenLoaded: false
                }
            })
            let updateMetaWorkflow = Object.assign({}, listMetaWorkflow) ;
            updateMetaWorkflow.children = children;
            updateMetaWorkflow.childrenLoaded = true;
            dispatch(update(updateMetaWorkflow))
        })
    }

    function renderChildren(renderMetaWorkflows){
        console.log("renderMetaWorkflows")
        console.log(renderMetaWorkflows)
        return(<>
            {renderMetaWorkflows.map((metaWorkflow, index) => {
                //проверить на закрытие открытие
                return (
                    <TreeItem nodeId={metaWorkflow.workflow.name}
                     key={index}
                     label={<div>{metaWorkflow.workflow.name}</div>}
                     onClick={() =>loadChildren(metaWorkflow)}
                     >
                     {renderChildren(metaWorkflow.children)}
                     </TreeItem>
                 )
            })}
        </>)
    }

    function test(){
        console.log("HELLO")
    }

    return (
        <div className='workflow-tree'>
            <TreeView
              aria-label="workflow navigator"
              defaultCollapseIcon={<ExpandMoreIcon />}
              defaultExpandIcon={<ChevronRightIcon />}
              sx={{ height: 240, flexGrow: 1, maxWidth: 400, overflowY: 'auto' }}
            >
                {renderChildren(metaWorkflows)}
            </TreeView>
        </div>
    )
}