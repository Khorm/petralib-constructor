import * as React from 'react';
import { PortWidget } from '@projectstorm/react-diagrams';
import Overlay from 'react-bootstrap/Overlay'


import './widget.css';

export class EventPort extends React.Component {
	
    constructor(props) {
        super(props);
        this.state = {
			showConnectors: false
        };
        this.target = React.createRef();

	}
	
	showConnectors = () =>{
		this.setState({
			showConnectors: !this.state.showConnectors
		});
	}
	
	getConnectors = (id) =>{
		return [{name: "Action with event " + id}];
	}
	
	render() {
		let connectorsSize;
		if (this.getConnectors(this.props.id) !== undefined && this.getConnectors(this.props.id).length > 0){
			connectorsSize = "+" + this.getConnectors(this.props.id).length;			
		}
		
		//side of parent
		let placement = this.props.type === 'out' ? 'right' : 'left' ;
		let flex_direction = placement === 'right' ? {flexDirection: 'row'} : {flexDirection: 'row-reverse'};

		return (
		<>					
			<PortWidget engine={this.props.engine} port={this.props.node.getPort(this.props.id)}>
				<div ref={this.target} className="port-container" onClick={this.showConnectors} style={flex_direction}>
					<div className={placement + "-circle-port-button"}/>						
					<div className="event-port">
						<span className="port-span">{connectorsSize}</span>
					</div>
				</div>
			</PortWidget>
			
			<Overlay target={this.target.current} show={this.state.showConnectors} placement={placement}>
			
			   {({ placement, arrowProps, show: _show, popper, ...props }) => (
			      <div
			        {...props}
			        style={{
			          backgroundColor: 'white',
			          padding: '2px 10px',
			          color: 'black',
			          borderRadius: 3,
			          ...props.style,
			        }}
			      >
			            {this.getConnectors(this.props.id).map((connector, i) => {     
					                           
					           return (<div key={i}>
										{connector.name}
										</div>) 
				        })}
			      </div>
				)}
			</Overlay>
	      </>
		);
	}
}