import * as React from 'react';
import { JSCustomNodeModel } from './JSCustomNodeModel';
import { EventWidget } from './event/EventWidget';
import { ActionWidget } from './action/ActionWidget';
import { AbstractReactFactory } from '@projectstorm/react-canvas-core';

export class JSCustomNodeFactory extends AbstractReactFactory {
	constructor() {
		super('js-custom-node');
	}

	generateModel(event) {
		return new JSCustomNodeModel();
	}

	generateReactWidget(event) {
		if (event.model.widgetType === 'action'){
			return <ActionWidget engine={this.engine} node={event.model}/>
		} else{
			return <EventWidget engine={this.engine} node={event.model} />;
		}
	}
}