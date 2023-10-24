import axios from 'axios';
import React from 'react'

import Button from 'react-bootstrap/Button'


class TypeList extends React.Component{

 	constructor(props) {
        super(props);
    }

    render (){
        let filteredTypes;
        if (this.props.filter !== undefined){
            filteredTypes = this.props.types.filter((value,key)=>{
                if (value.name.includes(this.props.filter)){
                    return true
                }
                return false;
            });
        }else{
            filteredTypes = this.props.types;
        }
   		return (
			<>
              {filteredTypes.map((value,key)=>{
                let chosenStyle = undefined;
                if (value.id === this.props.chosenType.id){
                    chosenStyle = {
                        background: 'blue',
                        color: 'white'
                    }
                }
                return (
                    <div key={key} style={chosenStyle} onClick={()=>{this.props.chooseType(value)}}
                     data-toggle="tooltip" data-placement="right" title={value.description}>{value.name}</div>
                )
              })
              }
			</>
    	);
    }

}

export default TypeList;