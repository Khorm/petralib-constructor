import React, { useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';

//workObject {id, name, description} - object for modal. Null
//alerts [] - list of strings describes alerts. Empty, Null
//name string - Name of modal owner. Not Null
//closeFunc - function to close modal
//saveFunc(id, name, description) - function to save modal
//deleteFunc (id) - delete function
//isModalOpened boolean
export class OptionsModal extends React.Component{

	constructor(props) {
        super(props);
        this.state = {
            id: undefined,
            name: undefined,
            description: undefined,
        }
    }


    componentDidUpdate(prevProps) {
      if (this.props.workObject !== prevProps.workObject) {
        if (this.props.workObject === undefined){
            this.setState({
                id: undefined,
                name: undefined,
                description: undefined,
            });
        }else{
            this.setState({
                id: this.props.workObject.id,
                name: this.props.workObject.name,
                description: this.props.workObject.description,
            });
        }
      }
      console.log("this.props.workObject");
      console.log(this.props.workObject);
    }

    editName = (newName) => {
        this.setState({
            name: newName
        });
    }

    editDescription = (newDescription) => {
        this.setState({
            description: newDescription
        });
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
                    <input type="text" style={{width:'100%'}} onChange={(event) => {this.editName(event.target.value)}}
                        value={this.state.name}/>

                    <p>{this.props.name} description: </p>
                    <textarea style={{resize:'none', width:'100%', height: '300px'}} onChange={(event) => {this.editDescription(event.target.value)}}
                     value={this.state.description}/>
                    <div style={{marginTop: "10px"}}>
                    {
                        this.props.alerts.map((alert,key) =>{
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
                    <Button onClick={() => {this.props.saveFunc(this.state.id, this.state.name, this.state.description)}}>Save</Button>
                    <Button onClick={()=>{this.props.deleteFunc(this.state.id)}}>Delete</Button>
                  </Modal.Footer>
                </Modal>
            </>
        );
	}
}

export default OptionsModal;