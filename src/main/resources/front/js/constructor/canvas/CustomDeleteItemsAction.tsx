import _forEach from 'lodash/forEach';
import createEngine, { DiagramModel, DefaultNodeModel, DefaultLinkModel } from '@projectstorm/react-diagrams';
import { CanvasWidget, Action, ActionEvent, InputType } from '@projectstorm/react-canvas-core';

interface CustomDeleteItemsActionOptions {
	keyCodes?: number[];
}

export default class CustomDeleteItemsAction extends Action {
	constructor(options: CustomDeleteItemsActionOptions = {}) {
		options = {
			keyCodes: [46, 8],
			...options
		};
		super({
			type: InputType.KEY_DOWN,
			fire: (event: ActionEvent<React.KeyboardEvent>) => {
                console.log("DELETE EVENT : ", event.event)
				if (options.keyCodes.indexOf(event.event.keyCode) !== -1) {
					const selectedEntities = this.engine.getModel().getSelectedEntities();
					if (selectedEntities.length > 0) {
						// const confirm = window.confirm('Are you sure you want to delete?');

						// if (confirm) {
							// _forEach(selectedEntities, (model) => {
							// 	// only delete items which are not locked
							// 	if (!model.isLocked()) {
							// 		model.remove();
							// 	}
							// });
							// this.engine.repaintCanvas();
						// }
					}
				}
			}
		});
	}
}