import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';

//import { groupsList, currentGroup } from './group-action-types';
//import store from '../redux-mod';

import Nav from 'react-bootstrap/Nav';

//objectsList [{id,name,description}] - list of working objects
//chosenObjectId int - chosen object id
//chooseObjectFunc(id) - choosing function
class List extends React.Component{


    render (){
        return (
            <Nav variant="pills" defaultActiveKey={this.props.chosenObjectId}>
                {
                    this.props.objectsList.map((object,key) => {
                    return (
                            <Nav.Item key = {key} >
                                <Nav.Link eventKey={''+object.id} onClick={()=>{this.props.chooseObjectFunc(object.id)}}
                                 data-toggle="tooltip" data-placement="bottom" title={object.description}>
                                    {object.name}
                                 </Nav.Link>
                            </Nav.Item>
                          )
                    })
                }
            </Nav>
        )
    }
}

export default List;

//const mapStateToProps = function(state){
//    return {
//        groups: state.groupState.groups,
//        currentGroup: state.groupState.currentGroup,
//        setCurrentPrecedent: state.groupState.setCurrentPrecedent,
//    }
//}
//
//const GroupList = connect(mapStateToProps) (GroupListClass);
//export default GroupList;