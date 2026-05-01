import React, { useState, useEffect } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import {User}  from '../admin/admin';
import axios from 'axios';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';


export default function UserCard(){
    const [user, setCurrentUser] = useState<User>(undefined);
    // const navigate = useNavigate();

    useEffect(() => {
        axios.get<User>('/api/v1/admin/current-user')
            .then((response) => {
                console.log(response)
                setCurrentUser(response.data)
            })
    }, [])

    const openPanel = () => {
        // navigate('admin');
        window.location.href = '/admin';
    }

    if (user === undefined) {
        return (<div>load...</div>)
    }
    return(
        <Card sx={{ minWidth: 275 }}>
            <CardContent>
                <h1>{user.userEmail}</h1>
                <h3>{user.role}</h3>
                {user.role === 'ADMIN' &&
                    <Button variant="outlined" onClick={openPanel}>Administrator panel</Button>
                }
            </CardContent>
        </Card>
    )
}