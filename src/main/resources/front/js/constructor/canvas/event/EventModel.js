import { Model } from '../Model';
import { DefaultPortModel } from '@projectstorm/react-diagrams';

/**
 * Example of a custom model using pure javascript
 */
export class EventModel extends Model {

	constructor(options = {}) {
		super(options);	
		
		this.widgetType = 'event';
		
		// setup an in and out port
		this.addPort(
			new DefaultPortModel({
				in: false,
				name: this.id
			})
		);
	}
	
	
	serialize() {
		return {
			...super.serialize(),			
			
			widgetType: this.widgetType
		};
	}

	deserialize(ob, engine) {
		super.deserialize(ob, engine);	
		
		this.widgetType = ob.widgetType;
	}

}