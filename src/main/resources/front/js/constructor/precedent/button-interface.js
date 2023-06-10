import React from 'react';
import { groupsList, currentGroup } from './group-action-types';
import store from '../redux-mod';
import { connect } from 'react-redux';

import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Button from 'react-bootstrap/Button';


class GroupInterfaceClass extends React.Component{

    render (){
        return (
            <ButtonGroup aria-label="Group manager">
              {this.props.currentGroup !== '-1' &&
                    <>
                        <Button variant="outline-danger" onClick={this.props.deleteGroup}>Delete</Button>
                        <Button variant="outline-primary" onClick={this.props.redactGroup}>Options</Button>
                    </>
              }
              <Button variant="outline-primary" onClick={this.props.addNewGroup}>Add</Button>
              <Button variant="outline-secondary">Previous</Button>
              <Button variant="outline-secondary">Next</Button>
            </ButtonGroup>
        )
    }
}

const mapStateToProps = function(state){
    return {
        groups: state.groupState.groups,
        currentGroup: state.groupState.currentGroup,
    }
}

const GroupInterface = connect(mapStateToProps) (GroupInterfaceClass);
export default GroupInterface;