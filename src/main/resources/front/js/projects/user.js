import React, { useState, useEffect } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import axios from 'axios';

export default function User(){
    const [user, setCurrentUser] = useState({});

    useEffect(() => {
        axios.get('/api/v1/project/current-user')
            .then((response) => {
                console.log(response)
                setCurrentUser(response.data)
            })
    }, [])

    return(
        <Card sx={{ minWidth: 275 }}>
            <CardContent>
                <h1>{user.name}</h1>
                <h3>{user.email}</h3>
            </CardContent>
        </Card>
    )
}