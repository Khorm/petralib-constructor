import React from 'react'
import axios from 'axios';

import { eventsList } from './event-action-types';
import store from '../../redux-mod';
import { connect } from 'react-redux';

import EntityHeader from '../header/header-manager';
import EntityList from '../entity_list/entity-list';
import EntityOptionsModal from '../../optional_modal/options-modal'

class EventManagerClass extends React.Component{

 	constructor(props) {
        super(props);
        this.state = {
            //header
            filter: undefined,

           //modal
           isModalOpened: false,
           chosenEvent: {},
           chosenVariables: [],
           optionAlerts: [],

        }
    }

	componentDidMount(){
        this.requestEvents();
	}

	requestEvents = () => {
	    axios.get('/api/event')
            .then(response => {
                let map = new Map(Object.entries(response.data));
                store.dispatch(eventsList(map));
            });
	}

	openCloseModal = () => {
	    this.setState({isModalOpened: !this.state.isModalOpened});
	}

	deselect = () => {
        this.setState({chosenEvent: {}, chosenVariables:[]});
	}

	addNew = () => {
	    this.deselect();
	    this.openCloseModal();
	}

	redact = () => {
	    if (this.state.chosenEvent.id === undefined) return;
	    this.openCloseModal();
	}

	setFilter = (filter) => {
	    this.setState({filter:filter});
	}

	chooseEvent = (event) => {
	    let chosenEvent = Object.assign({}, event);
	    this.setState({chosenEvent:chosenEvent}, () => {
	        if(this.state.chosenEvent.id === undefined) return;
	        axios.get('/api/variable/'+this.state.chosenEvent.id)
                .then(response => {
                    this.setState({chosenVariables: response.data});
                });
	    });
	}


	changeRedact = (newEvent) => {
	    let chosenEvent = Object.assign({}, this.state.chosenEvent);
	    if ('name' in newEvent){
	        chosenEvent.name = newEvent.name;
	    }
	    if ('description' in newEvent){
	        chosenEvent.description = newEvent.description;
	    }
        this.setState({chosenEvent:chosenEvent});
	}

	saveEvent = () => {
	    let savingEvent = this.state.chosenEvent;
	    savingEvent.variables = this.state.chosenVariables;

	    axios.post('/api/event', this.state.chosenEvent)
            .then(response => {
                let newEvent = response.data;
                let map = new Map(this.props.events);
                map.set(''+newEvent.id, newEvent);
                store.dispatch(eventsList(map));
                this.chooseEvent(savingEvent);
                this.openCloseModal();
            }).catch(error => {
                  let errors = [error.response.data.errorMessage].concat(error.response.data.errors);
                  this.setState({
                     optionAlerts: errors
                  })
            });
	}

	deleteEvent = () => {
	    if (this.state.chosenEvent.id === undefined) return;
        axios.delete('/api/event/' + this.state.chosenEvent.id)
            .then(response => {
                let map = new Map(this.props.events);
                map.delete(''+this.state.chosenEvent.id);
                store.dispatch(eventsList(map));
                this.openCloseModal();
            })
	}


	addVariable = () => {
	    let chosenVariables = Object.assign([], this.state.chosenVariables);
	    let newVariable = {isReadOnly: false};
	    chosenVariables.push(newVariable);
	    this.setState({chosenVariables:chosenVariables});
	}

	removeValue = (index) => {
	    let chosenVariables = this.state.chosenVariables;
	    chosenVariables.splice(index,1);
	    this.setState({chosenVariables:chosenVariables});
	}


	changeRedactVariable = (newVariable, index) => {
	    let chosenVariables = Object.assign([], this.state.chosenVariables);
        if ('name' in newVariable){
            chosenVariables[index].name = newVariable.name;
        } else
        if ('multiplicity' in newVariable){
            chosenVariables[index].multiplicity = newVariable.multiplicity;
        } else
        if ('type' in newVariable){
            chosenVariables[index].type = newVariable.type;
        } else
        if ('isReadOnly' in newVariable){
            chosenVariables[index].isReadOnly = !chosenVariables[index].isReadOnly;
        } else
        if ('description' in newVariable){
            chosenVariables[index].description = newVariable.description;
        }
        this.setState({chosenVariables:chosenVariables});
	}

    render (){

   		return (
			<div>
                <EntityHeader name={'Event'} addNewFunction={this.addNew} deselectFunction={this.deselect}
                redactFunction={this.redact} setFilterFunction={this.setFilter}/>

                <EntityList filter={this.state.filter} entities={Array.from(this.props.events.values())} chosenEntity={this.state.chosenEvent}
                chooseEntity={this.chooseEvent} basePrecedents={this.props.basePrecedents} addBaseFunc={this.props.addBaseFunc}
                removeBaseFunc = {this.props.removeBaseFunc}/>

                <EntityOptionsModal isModalOpened={this.state.isModalOpened} closeFunc={this.openCloseModal}
                name={'Event'} entityForOpt={this.state.chosenEvent} values={this.state.chosenVariables}
                optionAlerts={this.state.optionAlerts} changeRedact={this.changeRedact}
                saveFunc={this.saveEvent} deleteFunc={this.deleteEvent}
                addValue={this.addVariable} acceptAddValues={true} removeValue={this.removeValue}

                changeRedactVariable={this.changeRedactVariable} multiplicities={this.props.multiplicities}
                types={this.props.types}/>
			</div>
    	);
    }

}

const mapStateToProps = function(state){
    return {
        events: state.eventState.events,
        types: state.typeState.types,
        multiplicities: state.multiplicityState.multiplicities,
        basePrecedents: state.groupState.basePrecedents,
        addBaseFunc: state.canvasState.addBaseFunc,
        removeBaseFunc: state.canvasState.removeBaseFunc,
    }
}

const EventManager = connect(mapStateToProps) (EventManagerClass);
export default EventManager;