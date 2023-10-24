import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';

import { groupsList, currentGroup } from './group-action-types';
import store from '../redux-mod';

import Nav from 'react-bootstrap/Nav';

class GroupListClass extends React.Component{


    render (){
        return (
            <Nav variant="pills" defaultActiveKey={this.props.currentGroup}>
                {this.props.groups.size >0 &&
                    Array.from(this.props.groups.values()).map((group,key) => {
                    return (
                            <Nav.Item key = {key} >
                                <Nav.Link eventKey={''+group.id} onClick={()=>{this.props.setCurrentPrecedent(group.id)}}
                                 data-toggle="tooltip" data-placement="bottom" title={group.description}>
                                    {group.name}
                                 </Nav.Link>
                            </Nav.Item>
                          )
                    })
                }
            </Nav>
        )
    }
}

const mapStateToProps = function(state){
    return {
        groups: state.groupState.groups,
        currentGroup: state.groupState.currentGroup,
        setCurrentPrecedent: state.groupState.setCurrentPrecedent,
    }
}

const GroupList = connect(mapStateToProps) (GroupListClass);
export default GroupList;