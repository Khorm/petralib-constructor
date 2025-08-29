import React, { useState, useEffect  } from 'react';
import axios from 'axios';
import { withCookies, Cookies, useCookies } from 'react-cookie';

import './login.sass';
import 'bootstrap/dist/css/bootstrap.min.css'

import { Button } from '@mui/material';


export default function Login(){
    const [cookies, setCookie] = useCookies(['Authorization']);
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');


    function sendLogin(){
        axios.post('/api/v1/auth/login', {
          email: login,
          password: password
        })
        .then(function (response) {
            setCookie('Authorization', response.data.token, { path: '/' });
            window.location.href = '/projects';
        })
        .catch(function (error) {
          console.log(error);
        });
    }


    return (
    <>
        <div className = 'login-form'>
            <h2>Petra</h2>
            <input type="text" onChange={(e) => setLogin(e.target.value)} value={login}/>
            <input type="password" onChange={(e) => setPassword(e.target.value)} value={password}/>
            <Button variant="outlined" onClick={sendLogin}>Enter</Button>

        </div>
    </>
    );
}