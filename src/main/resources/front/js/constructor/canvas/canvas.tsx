import * as React from 'react';
import * as ReactDOM from 'react-dom';

import axios from 'axios';

import {setAddFunc, setRemoveFunc} from './redux_reducer/canvas-action-types';
import store from '../redux-mod';
import { connect } from 'react-redux';

import './canvas.css';

import createEngine, { DefaultLinkModel, DiagramModel } from '@projectstorm/react-diagrams';
import { JSCustomNodeFactory } from './JSCustomNodeFactory';
import { ActionModel } from './action/ActionModel';
import { EventModel } from './event/EventModel';

import { DiagramEngine } from '@projectstorm/react-diagrams';
import { CanvasWidget } from '@projectstorm/react-canvas-core';



// create an instance of the engine
const engine = createEngine();

// register the two engines
engine.getNodeFactories().registerFactory(new JSCustomNodeFactory() as any);

// create a diagram model
const newModel = new DiagramModel();

//####################################################
// now create two nodes of each type, and connect them

const eventOne = {id: 1,  name: 'Test event 1', x:150, y:150,
 variables: [{id: 1, type: 'String', name: 'locationName'}] }

const eventTwo = {id: 20,  name: 'Test event 2', x:250, y:250,
 variables: [{id: 2, type: 'Integer', name: 'locId'}] }

const eventThree = {id: 21,  name: 'Test event 3', x:255, y:255,
 variables: [{id: 3, type: 'Integer', name: 'statusId'}] }

const eventFour = {id: 40,  name: 'Test event 4', x:255, y:255,
 variables: [{id: 5, type: 'Integer', name: 'statusId'}] }

const actionOne = {id: 1, name: 'Action event 1', variables: [], 
subscribedEvents : [eventOne, eventTwo],  
	sendedEvents: [
	{
		conditions:[{id:1, name: 'fstCondition'}, {id:2, name: 'scdCondition'}],
		event: eventFour
	}
	] };
	
const actionTwo = {id: 2, name: 'Action event 2', variables: [], 
subscribedEvents : [eventFour],  
	sendedEvents: [
	{
		conditions:[{id:3, name: 'fstCondition'}, {id:4, name: 'scdCondition'}],
		event: eventThree
	}
	] };
	
const actions = new Map(); 
actions.set(actionOne.id, actionOne);
actions.set(actionTwo.id, actionTwo);


//const events = new Map();// [eventOne, eventTwo];
//events.set(eventOne.id, eventOne);
//events.set(eventTwo.id, eventTwo);
//events.set(eventThree.id, eventThree);



//const link1 = new DefaultLinkModel();
//link1.setSourcePort(node1.getPort('out'));
//link1.setTargetPort(node2.getPort('in'));

//model.addAll(/*node1, node2,*/ node3);

//####################################################




// install the model into the engine
engine.setModel(newModel);

export interface BodyWidgetProps {
	engine: DiagramEngine;
}

class BodyWidgetClass extends React.Component<BodyWidgetProps> {

	componentDidMount() {

	}


	generateCanvasData = () => {

		let model = new DiagramModel();
		engine.setModel(model);
		//model.registerListener({
		//	nodesUpdated: (event) => { console.log(event) }
		//});

		let eventsWithPorts = new Map();
		let nodes = [];

		this.props.basePrecedents.forEach((basePrecedent, key) => {
			let event = this.props.events.get(basePrecedent.baseId+'');

			let eventNode = new EventModel(event);
			eventNode.registerListener({
				selectionChanged: (canvasEvent) => {
					this.updatePrecedentBase(event, canvasEvent.entity.position.x, canvasEvent.entity.position.y);
				}
			});

			model.addNode(eventNode);
			eventNode.setPosition(basePrecedent.x, basePrecedent.y);
			nodes.push(eventNode);

			if (eventsWithPorts.has(event.id)){
				eventsWithPorts.get(event.id).sendedPorts.push(eventNode.getPort('' + event.id));
			}else{
				let newPortKeeper = {
					eventId: event.id,
					sendedPorts: [eventNode.getPort('' + event.id)],
					listenPorts: []
				}
				eventsWithPorts.set( event.id, newPortKeeper);
			}
		});

		engine.zoomToFit();

		//eventId
		//sendedPorts []
		//listenPorts []

		/*
		this.props.events.forEach((event, key) => {
			let eventNode = new EventModel(event);
			model.addNode(eventNode);
			eventNode.setPosition(event.x, event.y);
			nodes.push(eventNode);

			//console.log(eventNode.getPort('' + event.id));

			if (eventsWithPorts.has(event.id)){
				eventsWithPorts.get(event.id).sendedPorts.push(eventNode.getPort('' + event.id));
			}else{
				let newPortKeeper = {
					eventId: event.id,
					sendedPorts: [eventNode.getPort('' + event.id)],
					listenPorts: []
				}
				eventsWithPorts.set( event.id, newPortKeeper);
			}
		});



		actions.forEach((action, key) => {
		let actionNode = new ActionModel(action);
		model.addNode(actionNode);
		nodes.push(actionNode);

		action.subscribedEvents.forEach((subEvent) => {
		if (eventsWithPorts.has(subEvent.id)){
		eventsWithPorts.get(subEvent.id).listenPorts.push(actionNode.getPort('' + subEvent.id));
		}else{
		let newPortKeeper = {
		eventId: subEvent.id,
		sendedPorts: [],
		listenPorts: [actionNode.getPort('' + subEvent.id)]
		}
		eventsWithPorts.set( subEvent.id, newPortKeeper);
		}
		})

		action.sendedEvents.forEach((sendEvent) => {
		if (eventsWithPorts.has(sendEvent.event.id)){
		eventsWithPorts.get(sendEvent.event.id).sendedPorts.push(actionNode.getPort('' + sendEvent.event.id));
		}else{
		let newPortKeeper = {
		eventId: sendEvent.event.id,
		sendedPorts: [actionNode.getPort('' + sendEvent.event.id)],
		listenPorts: []
		}
		eventsWithPorts.set(sendEvent.event.id, newPortKeeper);
		}
		})
		});



		eventsWithPorts.forEach((portKeeper, id) =>{
			portKeeper.sendedPorts.forEach(sendedPort => {
				portKeeper.listenPorts.forEach(listenPort => {
					let link = new DefaultLinkModel();
					link.setSourcePort(sendedPort);
					link.setTargetPort(listenPort);
					model.addLink(link);
				})
			})
		});
		*/
	}




	addBase = (base) => {
		let newPrecedentBase = {
			baseId: base.id,
			precedentId: this.props.currentGroup,
			x: 0,
			y: 0,
		}

		axios.post('/api/precedent/base', newPrecedentBase)
			.then(response => {
				this.props.setCurrentPrecedent(this.props.currentGroup);
			}).catch(error => {

			});
	}


	updatePrecedentBase = (base,x,y) => {
		let newPrecedentBase = {
			baseId: base.id,
			precedentId: this.props.currentGroup,
			x: x,
			y: y,
		}

	axios.post('/api/precedent/base', newPrecedentBase)
		.then(response => {
			console.log(response.data);
		}).catch(error => {

		});
	}

	removeBase = (base) => {
		axios.delete('/api/precedent/base/'+ base.id + '/' + this.props.currentGroup)
			.then(response => {
				this.props.setCurrentPrecedent(this.props.currentGroup);
			}).catch(error => {

			});
	}




	render() {
		return (<>
			<h1>canvas</h1>
			 <CanvasWidget className="diagram-container" engine={engine} />
		</>)
	}
}

const mapStateToProps = function(state){
	return {
		//events: state.eventState.events,
		//basePrecedents: state.groupState.basePrecedents,
		//currentGroup: state.groupState.currentGroup,
		//setCurrentPrecedent: state.groupState.setCurrentPrecedent,
		//precedentBasesFunc: state.groupState.precedentBasesFunc,
		//types: state.typeState.types,
		//multiplicities: state.multiplicityState.multiplicities,
	}
}

const EntitiesCanvas = connect(mapStateToProps) (BodyWidgetClass);
export default EntitiesCanvas;