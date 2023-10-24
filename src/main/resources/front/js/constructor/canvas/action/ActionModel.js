import { Model } from '../Model';
import { DefaultPortModel } from '@projectstorm/react-diagrams';

/**
 * Example of a custom model using pure javascript
subscribedSignals - list of subscribed signals. Each signal contains:
{
id - signal id
name - signal name
}
 */
export class ActionModel extends Model {

	constructor(options = {}) {
		super(options);	

		this.subscribedSignals = options.subscribedSignals;
		this.sendSignals = options.sendSignals;
		this.widgetType = 'action';
		this.blockType = options.blockType;
		
				
		// setup an in and out port
		if (this.subscribedSignals !== undefined){
			this.subscribedSignals.forEach((signal)=>{
			    let port = new DefaultPortModel({
                                in: true,
                                name: signal.id
                            })
                port.setLocked(true);
				this.addPort(port);
			});
		}
		
		if (this.sendSignals !== undefined){
			this.sendSignals.forEach((signal)=>{
			 let port = new DefaultPortModel({
                            in: false,
                            name: signal.id
                        })
                port.setLocked(true);
				this.addPort(port);
			});	
		}
		
	}
	
	serialize() {
		return {
			...super.serialize(),
			
			subscribedSignals: this.subscribedSignals,
			sendSignals: this.sendSignals,
			widgetType: this.widgetType
		};
	}

	deserialize(ob, engine) {
		super.deserialize(ob, engine);
	
		this.subscribedSignals = ob.subscribedSignals;
		this.sendSignals = ob.sendSignals;
		this.widgetType = ob.widgetType;
	}

//	getPort = (portId) => {
//	    console.log('PORTS');
//        console.log(this.ports);
//        console.log(portId);
//        for (let i = 0; i< this.ports.length; i++){
//            console.log('PORT');
//            console.log(this.ports[i]);
//
//            if (this.ports[i] === portId){
//                return this.ports[i]
//            }
//        }
//	}
}