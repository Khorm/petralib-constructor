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

		this.subscribedSignal = options.subscribedSignal;
		this.sendSignal = options.sendSignal;
		this.widgetType = 'action';
		this.blockType = options.blockType;
		
				
		// setup an in and out port
		if (this.subscribedSignal !== undefined){
			let port = new DefaultPortModel({
                            in: true,
                        })
            port.setLocked(true);
            this.addPort(port);
		}
		
		if (this.sendSignals !== undefined){
            let port = new DefaultPortModel({
                        in: false,
                    })
            port.setLocked(true);
            this.addPort(port);
		}
		
	}
	
	serialize() {
		return {
			...super.serialize(),
			
			subscribedSignal: this.subscribedSignal,
			sendSignal: this.sendSignal,
			widgetType: this.widgetType
		};
	}

	deserialize(ob, engine) {
		super.deserialize(ob, engine);
	
		this.subscribedSignal = ob.subscribedSignal;
		this.sendSignal = ob.sendSignal;
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