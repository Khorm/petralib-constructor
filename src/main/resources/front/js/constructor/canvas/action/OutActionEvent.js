import React, { useState } from 'react';

import {EventPort} from '../EventPort';

import Collapse from 'react-bootstrap/Collapse'


export function OutActionEvent({ engine, node, sendEvent}){
	const [open, setOpen] = useState(false);
	let collapseStyle = {
		paddingLeft: '50px'
	}

	return (
		<>
			<div  className="action-in-event" >
				<span onClick={() => setOpen(!open)} aria-expanded={open} aria-controls="example-collapse-text">{sendEvent.event.name}</span>
				<EventPort engine={engine} node={node} id={sendEvent.event.id} type={'out'}/>				
			</div>
			
			<div style={collapseStyle}>
				<Collapse in={open}>
					<div id="example-collapse-text">
					{
						sendEvent.conditions.map((condition,key)=>{
							return(<div key={key}>
								{condition.name}
							</div>)
						})
					}
					</div>
				</Collapse>	
			</div>
        </>
		);
}