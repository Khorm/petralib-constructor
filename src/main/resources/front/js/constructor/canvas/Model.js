import { DefaultPortModel, NodeModel } from '@projectstorm/react-diagrams';


/**
 * Example of a custom model using pure javascript
 */
export class Model extends NodeModel {
	constructor(options = {}) {	
		//let idOfModel = options.id;		
		//delete options.id;		
		super({
			//...options,
			type: 'js-custom-node'
		});
		
		this.name = options.name;
		this.id = options.id;

		// setup an in and out port
//		this.addPort(
//			new DefaultPortModel({
//				in: false,
//				name: 'out'
//			})
//		);
	}

	serialize() {
		return {
			...super.serialize(),
			
			name: this.name,
			id: this.id
		};
	}

	deserialize(ob, engine) {
		super.deserialize(ob, engine);
	
		this.name = ob.name;
		this.id = ob.id;
	}
}