import React, { useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';


export class GroupOptionsModal extends React.Component{

	constructor(props) {
        super(props);
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
                      Group options
                    </Modal.Title>
                  </Modal.Header>
                  <Modal.Body>
                    <p>Group name: </p>
                    <input type="text" style={{width:'100%'}} onChange={this.props.changeRedactName} value={this.props.groupForOpt.name}/>

                    <p>Group description: </p>
                    <textarea style={{resize:'none', width:'100%', height: '300px'}} onChange={this.props.changeRedactDescr}
                     value={this.props.groupForOpt.description}/>
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
                  </Modal.Footer>
                </Modal>
            </>
        );
	}
}