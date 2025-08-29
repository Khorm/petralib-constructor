import React from 'react';
import { DefaultLinkWidget } from '@projectstorm/react-diagrams';
import { CustomLinkModel } from './CustomLinkModel';

export class CustomLinkWidget extends DefaultLinkWidget {
  render() {
    const { color, width } = (this.props.link as CustomLinkModel);

    // Переопределяем отрисовку пути
    return (
      <path
        className={`custom-link ${this.state.selected ? 'selected' : ''}`}
        stroke={color}
        strokeWidth={width}
        strokeDasharray={this.state.selected ? '10,5' : '0'} // Пример кастомизации
        ref={this.refPaths}
        {...this.getProps()} // Обработчики событий
      />
    );
  }
}