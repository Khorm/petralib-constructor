import axios from 'axios';
import React from 'react'

import Button from 'react-bootstrap/Button'

//props
//filter - string. Filter by name. Not blank. Null.
//entities - list of all entities
//chosenEntity{id, name, description} - chosen entity
//chooseEntity - function of entity choose. Requires entity.

//basePrecedents {baseId} - list of basePrecedents which contains in current precedent. Null. Empty.
//addBaseFunc function(base) - function adding base to canvas.
//removeBaseFunc function(base) - removing base element from canvas function.
class EntityList extends React.Component{

 	constructor(props) {
        super(props);
    }

    containsInPrecedent = (entityId) => {
        if (this.props.basePrecedents === undefined) return false;
        for (var i = 0; i < this.props.basePrecedents.length ; i++) {
            if (this.props.basePrecedents[i].baseId === entityId){
                return true;
            }
        }
        return false;
    }


    render (){
        let filteredEntities;
        if (this.props.filter !== undefined){
            filteredEntities = this.props.entities.filter((value,key)=>{
                if (value.name.toUpperCase().includes(this.props.filter.toUpperCase())){
                    return true
                }
                return false;
            });
        }else{
            filteredEntities = this.props.entities;
        }
   		return (
			<>
              {filteredEntities.map((value,key)=>{
                let chosenStyle = undefined;
                let containsPrecedent = this.containsInPrecedent(value.id);
                let addButton = undefined;
                if (value.id === this.props.chosenEntity.id){
                    chosenStyle = {
                        background: 'blue',
                        color: 'white'
                    }
                }else{
                    if (containsPrecedent === true){
                        chosenStyle = {
                            background: '#FFB6C1',
                            color: 'black'
                        }
                    }else{
                        chosenStyle = {
                            background: 'white',
                            color: 'black'
                        }
                    }
                }

                if (this.props.basePrecedents !== undefined){
                    if (containsPrecedent == true){
                        addButton = <button onClick={() => {this.props.removeBaseFunc(value)}} >-</button>
                    }else{
                        addButton = <button onClick={() => {this.props.addBaseFunc(value)}}>+</button>
                    }
                }


                return (
                    <div key={key} style={chosenStyle} onClick={()=>{this.props.chooseEntity(value)}}
                     data-toggle="tooltip" data-placement="right" title={value.description}>
                     {value.name}
                     {addButton}
                     </div>
                )
              })
              }
			</>
    	);
    }

}

export default EntityList;