import React from 'react';
import axios from 'axios';

import { precedentsList, currentPrecedent } from './precedent-action-types';
import store from '../redux-mod';
import { connect } from 'react-redux';

import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Button from 'react-bootstrap/Button';

import OptionsModal from './options-modal';
//import GroupInterface from './group-button-interface';
import List from './list';

class PrecedentManagerClass extends React.Component{

 	constructor(props) {
        super(props);
        this.state = {
            showPrecedentModal: false,
            modalAlerts:[],
            modalPrecedent: undefined,

        }
    }

	componentDidMount(){
	    //store.dispatch(setCurrentPrecedentSetFunc(this.setCurrentPrecedent));

        this.updatePrecedents();
	}

    updatePrecedents = () => {
        axios.get('/api/precedent')
            .then(response => {
                store.dispatch(precedentsList(response.data));
            });
    }

	switchModal = () => {
	    this.setState({
	        showPrecedentModal: !this.state.showPrecedentModal,
	        modalAlerts: [],
	    });
	}


	addNewPrecedent = () => {
	    console.log("addnew " + this.state.showPrecedentModal);
	    let newPrecedent = {};
	    this.setState({
            showPrecedentModal: !this.state.showPrecedentModal,
            modalPrecedent: newPrecedent,
        });
	}

	editPrecedent = () => {
	    let precedentToEdit = undefined;

	    for (let i = 0; i< this.props.precedents.length; i++){

	        if (this.props.precedents[i].id === this.props.currentPrecedent){
	            precedentToEdit = this.props.precedents[i];
	            break;
	        }
	    }
	    if (precedentToEdit === undefined) return;
        console.log(precedentToEdit);
        this.setState({
            showPrecedentModal: !this.state.showPrecedentModal,
            modalPrecedent: Object.assign({}, precedentToEdit),
        });
	}

//	changeRedactName = (event) => {
//	    let newOptionGroup = this.state.optionGroup;
//	    newOptionGroup.name = event.target.value;
//	    this.setState({
//	        optionGroup : Object.assign({}, newOptionGroup)
//	    });
//	}

//	changeRedactDescr = (event) => {
//    	    let newOptionGroup = this.state.optionGroup;
//    	    newOptionGroup.description = event.target.value;
//    	    this.setState({
//    	        optionGroup : Object.assign({}, newOptionGroup)
//    	    });
//    }

    savePrecedent = (id, name, description) => {
        let savingPrecedent = {
            id: id,
            name: name,
            description: description,
        }

        axios.post('/api/precedent', savingPrecedent)
        .then(response => {
//            let newPrecedent = response.data;
//            let map = new Map(this.props.groups);
//            map.set(''+newGroup.id, newGroup);
//            store.dispatch(groupsList(map));
            this.updatePrecedents();
            this.switchModal();
        }).catch(error => {
              let errors = [error.response.data.errorMessage].concat(error.response.data.errors);
              this.setState({
                 optionAlerts: errors
              })
        });
    }

    deletePrecedent = (precedentId) =>{
        axios.delete('/api/precedent/'+precedentId)
            .then(response=>{
//                let map = new Map(this.props.groups);
//                map.delete(this.props.currentGroup);
//                store.dispatch(currentGroup('-1'));
//                store.dispatch(groupsList(map));
                store.dispatch(currentPrecedent(-1))
                this.updatePrecedents();

            })
    }


    setCurrentPrecedent = (precedentId) => {
        store.dispatch(currentPrecedent(precedentId));
    }



    render (){
   		return (
			<>
			    <Button variant="outline-primary" onClick={this.editPrecedent}>Precedent options</Button>
                <Button variant="outline-primary" onClick={this.addNewPrecedent}>Add</Button>

                <List objectsList={this.props.precedents} chosenObjectId={this.props.currentPrecedent}
                chooseObjectFunc={this.setCurrentPrecedent} />

                <OptionsModal workObject={this.state.modalPrecedent} alerts={this.state.modalAlerts} name={"Precedent"}
                closeFunc={this.switchModal} saveFunc={this.savePrecedent} deleteFunc={this.deletePrecedent}
                isModalOpened={this.state.showPrecedentModal} />
			</>
    	);
    }

}


const mapStateToProps = function(state){
    return {
        precedents: state.precedentState.precedents,
        currentPrecedent: state.precedentState.currentPrecedent,
    }
}

const PrecedentManager = connect(mapStateToProps) (PrecedentManagerClass);
export default PrecedentManager;