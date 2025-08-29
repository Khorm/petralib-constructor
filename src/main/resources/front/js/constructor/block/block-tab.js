import React, { useState, useEffect  } from 'react'

import 'bootstrap/dist/css/bootstrap.min.css'

import axios from 'axios'

import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';

import BlockList from './block-list';

import { useSelector, useDispatch } from 'react-redux'
import { chooseWorkflow } from '../workflowSlice'


export default function BlockTab() {

    const [value, setValue] = useState('Action')
    const chosenWorkflow = useSelector((state) => state.workflow.value)
    const dispatch = useDispatch()

    function handleChange(e){
        setValue(e.target.textContent)
    }

    function chooseWf(workflow){
        dispatch(chooseWorkflow(workflow))
    }

    return (
        <div>
            <Tabs
                onChange={handleChange}
                value={value}
              aria-label="wrapped label tabs example"
            >
              <Tab value="Action"  label="Action" />
              <Tab value="Workflow" label="Workflow" />
              <Tab value="Service" label="Service" />
              <Tab value="Signal" label="Signal" />
            </Tabs>
            {value === "Action" && <BlockList blockTypeUrl="block/action"/>}
            {value === "Workflow" && <BlockList blockTypeUrl="block/workflow" choose={chooseWf} chosen={chosenWorkflow}/>}
            {value === "Service" && <BlockList blockTypeUrl="service"/>}
        </div>
    )
}