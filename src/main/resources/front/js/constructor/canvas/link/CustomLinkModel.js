import { DefaultLinkModel, DefaultLinkModelOptions } from '@projectstorm/react-diagrams';

export interface CustomLinkModelOptions extends DefaultLinkModelOptions {
  color?: string;
  width?: number;
}

export class CustomLinkModel extends DefaultLinkModel {
  color: string;
  width: number;

  constructor(options: CustomLinkModelOptions = {}) {
    super({
      ...options,
      type: 'custom-link', // ”никальный тип св€зи
    });
    this.color = options.color || 'orange';
    this.width = options.width || 3;
  }
}