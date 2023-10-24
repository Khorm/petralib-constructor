import * as React from 'react';
import VariablesList from '../VariablesList';
import { EventPort } from '../EventPort';
import { OutActionEvent } from './OutActionEvent';

import '../widget.css';
import './action.sass';


export class ActionWidget extends React.Component {
	
	render() {
	    let nodeClassName = this.props.node.blockType + '-node'
		return (
			<div className={nodeClassName} >
				<div className="title">
					<h3> {this.props.node.name} </h3>
									
					
				</div>					
				<VariablesList variables = {this.props.node.variables}/>
				<hr />
				<div className="action-events">
					<div>				
						{this.props.node.subscribedSignals !== undefined &&
							this.props.node.subscribedSignals.map((signal, key)=>{
								return(<div key={key} className="action-in-event">									
									<EventPort engine={this.props.engine} node={this.props.node} id={signal.id} type={'in'}/>
									<span>{signal.name}</span>
								</div>);
							})
						}
					</div>
					
					
					
					<div className="action-out-events">
						{this.props.node.sendSignals !== undefined &&
							this.props.node.sendSignals.map((signal, key)=>{
                                return(<div key={key} className="action-in-event">
                                        <span>{signal.name}</span>
                                        <EventPort engine={this.props.engine} node={this.props.node} id={signal.id} type={'out'}/>
                                    </div>);
							})							
						}
					</div>
				</div>
				
			</div>
		);
	}
}