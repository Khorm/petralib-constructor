import React, { useState, useEffect  } from 'react';
import { createRoot } from 'react-dom/client';
import 'bootstrap/dist/css/bootstrap.min.css';

import { Provider } from 'react-redux'
import store from './store'

import List from './right_panel/list';
import Canvas from './canvas/canvas';

import './constructor.sass'



export default function App() {

    return (
        <div className='constructor-form'>
            <Canvas/>
            <List/>
        </div>
    )
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<Provider store={store}>
                <App />
              </Provider>);