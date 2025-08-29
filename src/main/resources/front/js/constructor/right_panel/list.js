import React, { useState, useEffect  } from 'react'

import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';

import 'bootstrap/dist/css/bootstrap.min.css'
import './list.sass';

import EntityList from './block-list';
import ServiceModal from './service-modal';
import TypeModal from './modal/type-modal';
import BlockModal from './modal/block-modal';

import { useSelector, useDispatch } from 'react-redux';
import { chooseWorkflow } from '../workflowSlice';
import { setAdd } from '../canvas-functions-slice';



export default function List() {

    const [value, setValue] = React.useState(0);
    const handleChange = (event, newValue) => {
        setValue(newValue);
      };
    const dispatch = useDispatch();

    function buildModalService(service,  open, handleClose){
        return(<ServiceModal service={service} open={open} handleClose={handleClose} />)
    }

    function buildModalType(type,  open, handleClose){
        return(<TypeModal type={type}  open={open} handleClose={handleClose} />)
    }

    function buildModalAction(block, open, handleClose){
        return(<BlockModal block={block} open={open} handleClose={handleClose} url={'/api/v1/action'}/>)
    }

    function buildModalWorkflow(block, open, handleClose){
        return(<BlockModal block={block} open={open} handleClose={handleClose} url={'/api/v1/workflow'}/>)
    }

    function buildModalSource(block, open, handleClose){
        return(<BlockModal block={block} open={open} handleClose={handleClose} url={'/api/v1/source'}/>)
    }

    function chooseWorkflowFunction(workflowId){
        dispatch(chooseWorkflow(workflowId));
    }

    function addBlockToCanvas(block){
        dispatch(setAdd(block));
    }


    return(
        <div className='panel'>
            <Tabs value={value} onChange={handleChange} >
                  <Tab label="Service" />
                  <Tab label="Type" />
                  <Tab label="Action" />
                  <Tab label="Workflow" />
                  <Tab label="Source" />
            </Tabs>
            {value === 0 &&
                <EntityList getUrl = {'/api/v1/service/'} createModal={buildModalService}/>
            }
            {value === 1 &&
                <EntityList getUrl = {'/api/v1/type/'} createModal={buildModalType}/>
            }
            {value === 2 &&
                <EntityList getUrl = {'/api/v1/action/'} createModal={buildModalAction} addFunc = {addBlockToCanvas} />
            }
            {value === 3 &&
                <EntityList getUrl = {'/api/v1/workflow/'} createModal={buildModalWorkflow} chooseFunc={chooseWorkflowFunction} addFunc = {addBlockToCanvas} />
            }
            {value === 4 &&
                <EntityList getUrl = {'/api/v1/source/'} createModal={buildModalSource}/>
            }
        </div>
    )

}