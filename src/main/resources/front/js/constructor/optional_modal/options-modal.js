import React from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import ButtonGroup from 'react-bootstrap/ButtonGroup';

import VariableManager from './variable-manager'


//props
//isModalOpened - boolean flag of opened model. Not null.
//closeFunc - function which close modal. Not null.
//name - name of modal. Not null.
//entityForOpt{id, name, description} - options entity. Not Null.
//optionAlerts - alerts and errors after options set. Null, Empty.
//changeRedact - function receives changes. Not null Not Empty.
//saveFunc - function, saves redact entity
//deleteFunc - function, delete redact entity
//values - array of entity values. Null. Empty.
//acceptAddValues - flag, contains permission to show add value buttons. Not null.
//addValue - function. create a new empty value.
//removeValue - function. Removing value. requires index of value.

//values props
  //changeRedactVariable - function redact variable. Not null.
  //multiplicities -[] multiplicities list. Not null.
  //types - []. list of all types. Not null.
class EntityOptionsModal extends React.Component{

	constructor(props) {
        super(props);
        this.state = {
            chosenValueIndex: undefined
        }
    }

    chooseValue = (index) => {
        this.setState({chosenValueIndex: index});
    }

    removeValue = () => {
        this.props.removeValue(this.state.chosenValueIndex);
        this.setState({chosenValueIndex: undefined});
    }

	render (){
        return(
            <>
                <Modal
                      show={this.props.isModalOpened}
                      size="lg"
                      aria-labelledby="contained-modal-title-vcenter"
                      centered
                      onHide={this.props.closeFunc}
                    >

                  <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title-vcenter">
                      {this.props.name} options
                    </Modal.Title>
                  </Modal.Header>
                  <Modal.Body>
                    <p>{this.props.name} name: </p>
                    <input type="text" style={{width:'100%'}} onChange={(event) => {this.props.changeRedact({name: event.target.value})}}
                     value={this.props.entityForOpt.name}/>

                    {this.props.acceptAddValues === true &&
                        <div>
                            <ButtonGroup aria-label="Basic example">
                              <Button variant="primary" onClick={this.props.addValue}>Add</Button>
                              <Button variant="danger" onClick={this.removeValue}>Delete</Button>
                            </ButtonGroup>
                            <h4>Variables</h4>
                        </div>
                    }

                     {this.props.values !== undefined &&
                        <div>
                            {this.props.values.map((value, key)=>{
                                let chosenStyle = undefined;
                                if (this.state.chosenValueIndex === key){
                                    chosenStyle = { backgroundColor: '#AFEEEE'};
                                }
                                return(
                                    <div key={key} style={chosenStyle} onClick={() => {this.chooseValue(key)}}>
                                        <VariableManager changeRedactVariable={this.props.changeRedactVariable} name={value.name}
                                        multiplicity={value.multiplicity} multiplicities={this.props.multiplicities}
                                        type={value.type} types={this.props.types} description={value.description}
                                        isReadOnly={value.isReadOnly} index={key}/>
                                    </div>
                                )
                            })}
                        </div>
                     }

                    <p>{this.props.name} description: </p>
                         <textarea style={{resize:'none', width:'100%', height: '300px'}}
                          onChange={(event) => {this.props.changeRedact({description: event.target.value})}}
                          value={this.props.entityForOpt.description}/>

                    <div style={{marginTop: "10px"}}>
                    {
                        this.props.optionAlerts.map((alert,key) =>{
                            return (
                                <Alert key={key} variant={'danger'}>
                                    {alert}
                                </Alert>
                            )
                        })
                    }
                    </div>


                  </Modal.Body>
                  <Modal.Footer>
                    <Button onClick={this.props.closeFunc}>Close</Button>
                    <Button onClick={this.props.saveFunc}>Save</Button>
                    <Button variant="danger" onClick={this.props.deleteFunc}>Delete</Button>
                  </Modal.Footer>
                </Modal>
            </>
        );
	}
}

export default EntityOptionsModal;