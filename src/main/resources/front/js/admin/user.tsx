import React from 'react';
import _ from 'lodash';

import 'bootstrap/dist/css/bootstrap.min.css';

import TextField from '@mui/material/TextField';


import {User}  from './admin';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';

interface UserListInput {
    user : User;
    index : number;
    changeUser: (editerUser: User, key: number) => void;
    deleteUser: (key: number) => void
}


/**
 * currentVariable - переменная блока
 * acceptedVariables - доступные для выбора переменные блока
 * scenarioVariable - переменная сценария относящаяся к этой переменной блока
 */
export default function UserListEntry({user, index, changeUser, deleteUser}:UserListInput){
  

    const changeRole = (event: any) =>{
        const newUser : User = _.cloneDeep(user);
        newUser.role = event.target.value;
        changeUser(newUser, index);
    }

    const changeName = (event: any) => {
        const newUser : User = _.cloneDeep(user);
        newUser.userName = event.target.value;
        changeUser(newUser, index);
    };

    const changeEmail = (event: any) => {
        const newUser : User = _.cloneDeep(user);
        newUser.userEmail = event.target.value;
        changeUser(newUser, index);
    };

    const changePassword = (event: any) => {
        const newUser : User = _.cloneDeep(user);
        newUser.password = event.target.value;
        changeUser(newUser, index);
    };
    

    return(
        <div className='user'>
            <TextField id="standard-basic" label="Name" variant="outlined" onChange={changeName} value={user.userName}/>
            <TextField id="standard-basic" label="Password" variant="outlined" onChange={changePassword} value={user.password}/>
            <TextField id="standard-basic" label="Email" variant="outlined" onChange={changeEmail} value={user.userEmail}/>

            <InputLabel id="role-select-label">Role</InputLabel>
            <Select
                labelId="role-select-label"
                id="role-select-label"
                value={user.role}
                label="Role"
                onChange={changeRole}
            >
                <MenuItem value={'USER'}>User</MenuItem>
                <MenuItem value={'ADMIN'}>Administrator</MenuItem>
            </Select>
            <IconButton  onClick={()=>deleteUser(index)}><DeleteIcon color='error'/></IconButton>
        </div>
    )
}