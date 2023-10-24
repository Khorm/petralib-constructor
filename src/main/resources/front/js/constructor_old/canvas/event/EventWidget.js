import * as React from 'react';
import VariablesList from '../VariablesList';
import {EventPort} from '../EventPort';


import '../widget.css';

export class EventWidget extends React.Component {
	render() {
		return (
			<div className="event-node" >	
				
				<div className="title">
					<h3> {this.props.node.name} </h3>
					<EventPort engine={this.props.engine} node={this.props.node} id={this.props.node.id} type={'out'}/>					
					
				</div>					
				<VariablesList variables = {this.props.node.variables}/>				
			</div>
		);
	}
}