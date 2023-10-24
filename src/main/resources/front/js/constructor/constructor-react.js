import React, { useState, useEffect  } from 'react';
import { createRoot } from 'react-dom/client';
import 'bootstrap/dist/css/bootstrap.min.css';

import { Provider } from 'react-redux'
import store from './store'

import WorkflowList from './workflow/workflow-list';
import BodyWidgetClass from './canvas/canvas';
//import WorkflowTree from './workflow/workflow-tree'
//import WorkflowList from './workflow/workflow-list';



export default function App() {

//    const [projects, setProjects] = useState([]);
//
//    function onProjectChoose(projectId){
//        console.log(projectId);
//    }

//            <WorkflowList/>
    return (
        <div>
            <WorkflowList/>
            <BodyWidgetClass/>
        </div>
    )
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<Provider store={store}>
                <App />
              </Provider>);