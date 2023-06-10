import * as React from 'react';
import VariablesList from '../VariablesList';
import { EventPort } from '../EventPort';
import { OutActionEvent } from './OutActionEvent';

import '../widget.css';
import './action.css';


export class ActionWidget extends React.Component {
	
	render() {
		return (
			<div className="event-node" >		
				<div className="title">
					<h3> {this.props.node.name} </h3>
									
					
				</div>					
				<VariablesList variables = {this.props.node.variables}/>
				<hr />
				<div className="action-events">
					<div>				
						{this.props.node.subscribedEvents !== undefined &&
							this.props.node.subscribedEvents.map((event, key)=>{
								return(<div key={key} className="action-in-event">									
									<EventPort engine={this.props.engine} node={this.props.node} id={event.id} type={'in'}/>
									<span>{event.name}</span>										
								</div>);
							})
						}
					</div>
					
					
					
					<div className="action-out-events">
						{this.props.node.sendedEvents !== undefined &&							
							this.props.node.sendedEvents.map((sendEvent, key)=>{
								return(<OutActionEvent key={key} engine={this.props.engine} node={this.props.node} sendEvent = {sendEvent}/>);
							})							
						}
					</div>
				</div>
				
			</div>
		);
	}
}