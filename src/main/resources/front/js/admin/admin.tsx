import React, { useState, useEffect  }  from 'react';
// @ts-ignore
import { createRoot } from 'react-dom/client';

import 'bootstrap/dist/css/bootstrap.min.css';

import './sass/admin.sass';
import axios from 'axios';
import UserListEntry from './user';
import Button from '@mui/material/Button';
import AddCircleIcon from '@mui/icons-material/AddCircle';




export interface User {
    id: number;
    userEmail: string;
    userName: string;
    password: string;
    role: 'USER' | 'ADMIN';
    changed: boolean;
    deleted: boolean;
}

/**
 * currentVariable - переменная блока
 * acceptedVariables - доступные для выбора переменные блока
 * scenarioVariable - переменная сценария относящаяся к этой переменной блока
 */
export default function AdminRoot(){

    const [users, setUsers] = React.useState<User[]>([]);

      useEffect(() => {
            axios
            .get<User[]>('/api/v1/admin', {})
            .then((response) => {
                console.log("USERS ", response.data);
                setUsers(response.data);
            })
            .catch((error) => {
                console.error('Ошибка загрузки переменных:', error);
                alert(`Не удалось загрузить пользователей: ${error.message}`);
            });
      }, []);    


    const changeUser = (editerUser: User, key: number) => {
        let newUserArr : User[] = [];

        for (let i = 0; i<users.length; i++){
            if (i === key){                
                editerUser.changed = true;
                newUserArr.push(editerUser);
            }else{
                newUserArr.push(users[i])
            }
            
        }
        setUsers(newUserArr);
    }

    const deleteUser = (key: number) => {
        let newUserArr : User[] = [];

        for (let i = 0; i<users.length; i++){
            if (i === key){
                users[i].deleted = true;
            }
            newUserArr.push(users[i])
        }
        setUsers(newUserArr);
    }

    const addUser = () => {
        let newUserArr : User[] = [...users];
        newUserArr.push({
            id: undefined,
            password:'',
            role: 'USER',
            userEmail: '',
            userName: '',
            changed: true,
            deleted: false
        })
        setUsers(newUserArr);
    }

    const save = () => {
            axios
            .post('/api/v1/admin', users)
            .then((response) => {
                console.log("USERS SAVED");                
            })
            .catch((error) => {
                console.error('Ошибка сохранения переменных:', error);
                alert(`Не удалось сохранить пользователей: ${error.message}`);
            });
    }

    return(
        <div className='users-box'>
            {users
            .filter(user => !user.deleted)
            .map((user, index)=>(
                <UserListEntry changeUser={changeUser} key={index} index={index} user={user} deleteUser={deleteUser}/>
            ))}
            <Button variant="outlined" color='success' onClick={addUser} startIcon={<AddCircleIcon />}></Button>
            <Button variant="outlined" onClick={save} >Save</Button>
        </div>
    )
}


const container = document.getElementById('react');
const root = createRoot(container);
root.render(<AdminRoot />);