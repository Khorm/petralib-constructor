import React from 'react'
import axios from 'axios';

import { typesList } from './type-action-types';
import store from '../../redux-mod';
import { connect } from 'react-redux';

import TypeHeader from './header/header-manager';
import TypeList from './list/type-list';
import TypeOptionsModal from './options_modal/options-modal'


class TypeManagerClass extends React.Component{

 	constructor(props) {
        super(props);
        this.state = {
            //modal
            showModal: false,
            chosenType: {},
            optionAlerts: [],

            //filter
            filter: undefined,
        }
    }

	componentDidMount(){
        this.requestTypes();
	}

	requestTypes = () => {
	    axios.get('/api/type')
            .then(response => {
                store.dispatch(typesList(response.data));
            });
	}

	setFilter = (filter) => {
	    if (filter === ''){
	        filter = undefined;
	    }
	    this.setState({
	        filter: filter
	    });
	}

	changeRedactName = (name) => {
	    let newRedactType = this.state.chosenType;
	    newRedactType.name = name;
	    this.setState({
	        chosenType: newRedactType
	    })
	}

	changeRedactDescr = (descr) => {
	    let newRedactType = this.state.chosenType;
        newRedactType.description = descr;
        this.setState({
            chosenType: newRedactType
        })
	}

	saveType = () => {
	    axios.post('/api/type', this.state.chosenType)
        .then(response => {
            this.requestTypes();
            this.openCloseModal();
        }).catch(error => {
              console.log(error);
              let errors = [error.response.data.errorMessage].concat(error.response.data.errors);
              this.setState({
                 optionAlerts: errors
              })
        });
	}

	deleteType = () => {
	    if (this.state.chosenType.id === undefined) return;
	    axios.delete('/api/type/' + this.state.chosenType.id)
            .then(response => {
                this.requestTypes();
                this.openCloseModal();
            })
	}

	chooseType = (type) => {
	    let chosenType = undefined;
	    if (type !== undefined ){
	        chosenType = Object.assign({}, type);
	    }else{
	        chosenType = {};
	    }
	    this.setState({
             chosenType: chosenType
          });
	}

	openCloseModal = () => {
	    this.setState({showModal: !this.state.showModal});
	}

    render (){

   		return (
			<>
                <TypeHeader chooseType={this.chooseType} openCloseModal={this.openCloseModal} redactFunction={this.openCloseModal}
                setFilter={this.setFilter}/>

                <TypeList chosenType={this.state.chosenType} chooseType={this.chooseType} types={this.props.types} filter={this.state.filter}/>

                <TypeOptionsModal isModalOpened={this.state.showModal} closeFunc={this.openCloseModal}
                                 typeForOpt={this.state.chosenType} changeRedactName={this.changeRedactName}
                                 deleteFunc={this.deleteType} saveFunc={this.saveType} optionAlerts={this.state.optionAlerts}
                                 changeRedactDescr={this.changeRedactDescr}/>
			</>
    	);
    }

}

const mapStateToProps = function(state){
    return {
        types: state.typeState.types,
    }
}

const TypeManager = connect(mapStateToProps) (TypeManagerClass);
export default TypeManager;
