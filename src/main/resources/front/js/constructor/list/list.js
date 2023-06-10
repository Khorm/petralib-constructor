import React from 'react'
import axios from 'axios';

import store from '../redux-mod';
import {multiplicitiesList} from '../redux-mod'

import TypeManager from './type/type-manager';
import EventManager from './event/event-manager';

class EntitiesList extends React.Component{
	
 	constructor(props) {
        super(props);
    }

	componentDidMount(){
        store.dispatch(multiplicitiesList(['SINGLE', 'LIST']));
	}

    render (){
		
   		return (
			<div>		
                <TypeManager/>
                <EventManager/>
			</div>
    	);
    }

}

export default EntitiesList;
