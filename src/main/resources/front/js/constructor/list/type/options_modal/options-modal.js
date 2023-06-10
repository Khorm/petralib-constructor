import React from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';


class TypeOptionsModal extends React.Component{

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
                      Type options
                    </Modal.Title>
                  </Modal.Header>
                  <Modal.Body>
                    <p>Type name: </p>
                    <input type="text" style={{width:'100%'}} onChange={(event) => {this.props.changeRedactName(event.target.value)}}
                     value={this.props.typeForOpt.name}/>

                    <p>Type description: </p>
                    <textarea style={{resize:'none', width:'100%', height: '300px'}}
                     onChange={(event) => {this.props.changeRedactDescr(event.target.value)}}
                     value={this.props.typeForOpt.description}/>

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

export default TypeOptionsModal;