import React from 'react'
import axios from 'axios';

import Dropdown from 'react-bootstrap/Dropdown'

//props
//changeRedactVariable - function redact variable. Not null.
//name - string. current variable name. Null
//multiplicity - {id, name, description}. current value multiplicity. Null
//multiplicities -[] multiplicities list. Not null.
//type - {id, name, description}. current type. Null
//types - []. list of all types. Not null.
//description - string. current description. Not null.
//isReadOnly - boolean. Current isReadOnly value. Not null.
//index - index of variable in array
class VariableManager extends React.Component{

 	constructor(props) {
        super(props);
    }

    render (){
        let valueName = this.props.name === undefined ? '' : this.props.name;
        let typeName = this.props.type === undefined ? '' : this.props.type.name;
   		return (
			<>
                <div>
                    <h5>Name: </h5>
                    <input type="text" style={{width:'100%'}}
                                onChange={(event) => {this.props.changeRedactVariable({name:event.target.value}, this.props.index)}}
                                value={valueName}/>
                </div>
                <div>
                    <h5>Multiplicity: </h5>
                    <Dropdown>
                      <Dropdown.Toggle variant="success" id="dropdown-basic">
                        {this.props.multiplicity}
                      </Dropdown.Toggle>

                      <Dropdown.Menu>
                        {this.props.multiplicities.map((value,key) => {
                            return(
                                <Dropdown.Item key={key}
                                onClick={() => {this.props.changeRedactVariable({multiplicity: value}, this.props.index)}}>
                                    {value}
                                </Dropdown.Item>
                            )
                        })}
                      </Dropdown.Menu>
                    </Dropdown>
                </div>

                <div>
                    <h5>Type: </h5>
                    <Dropdown>
                      <Dropdown.Toggle variant="success" id="dropdown-basic">
                         {typeName}
                      </Dropdown.Toggle>

                      <Dropdown.Menu>
                        {this.props.types.map((value,key) => {
                            return(
                                <Dropdown.Item key={key}
                                onClick={() => {this.props.changeRedactVariable({type: value}, this.props.index)}}>
                                    {value.name}
                                </Dropdown.Item>
                            )
                        })}
                      </Dropdown.Menu>
                    </Dropdown>
                </div>

                <div>
                    <h5>ReadOnly: </h5>
                    <input type="checkbox" checked={this.props.isReadOnly}
                    onClick={(event) => {this.props.changeRedactVariable({isReadOnly: event.target.value}, this.props.index)}} />
                </div>

                <div>
                    <h5>Description: </h5>
                    <textarea style={{resize:'none', width:'100%', height: '300px'}}
                     onChange={(event) => {this.props.changeRedactVariable({description: event.target.value}, this.props.index)}}
                     value={this.props.description}/>
                </div>
			</>
    	);
    }

}

export default VariableManager;