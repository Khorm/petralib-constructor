import { Model } from '../Model';
import { DefaultPortModel } from '@projectstorm/react-diagrams';

/**
 * Example of a custom model using pure javascript
 */
export class ActionModel extends Model {

	constructor(options = {}) {
		super(options);	

		this.subscribedEvents = options.subscribedEvents;
		this.sendedEvents = options.sendedEvents;
		this.widgetType = 'action';
		
				
		// setup an in and out port
		if (this.subscribedEvents !== undefined){
			this.subscribedEvents.forEach((event)=>{
				this.addPort(
					new DefaultPortModel({
						in: true,
						name: event.id
					})
				);
			});
		}
		
		if (this.sendedEvents !== undefined){
			this.sendedEvents.forEach((sendedEvent)=>{
				this.addPort(
					new DefaultPortModel({
						in: false,
						name: sendedEvent.event.id
					})
				);
			});	
		}
		
	}
	
	serialize() {
		return {
			...super.serialize(),
			
			subscribedEvents: this.subscribedEvents,
			sendedEvents: this.sendedEvents,
			widgetType: this.widgetType
		};
	}

	deserialize(ob, engine) {
		super.deserialize(ob, engine);
	
		this.subscribedEvents = ob.subscribedEvents;
		this.sendedEvents = ob.sendedEvents;
		this.widgetType = ob.widgetType;
	}
}