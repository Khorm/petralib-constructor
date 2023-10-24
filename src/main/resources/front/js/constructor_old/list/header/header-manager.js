import axios from 'axios';
import React from 'react'

//import Button from 'react-bootstrap/Button'

//props
//name - name of header
//addNewFunction - open close modal function
//deselectFunction - deselect entity function
//redactFunction - open modal of redact chosen entity function
//setFilterFunction - accept filter. requires string.
class EntityHeader extends React.Component{

 	constructor(props) {
        super(props);
        this.state = {
            filter: "",
        }
    }


	changeFilter = (event) => {
	     this.setState({
            filter:event.target.value
	     }, () => {
	        let acceptedFilter = undefined;
	        if (this.state.filter !== ''){
	            acceptedFilter = this.state.filter;
	        }
	        this.props.setFilterFunction(acceptedFilter)
	     });

	}


    render (){
   		return (
			<div>
                <h6>{this.props.name}</h6>
                <input type="text" onChange={this.changeFilter} value={this.state.filter}/>
                <button type="button" className="btn btn-primary btn-sm" onClick={this.props.addNewFunction}>add</button>
                <button type="button" className="btn btn-primary btn-sm" onClick={this.props.deselectFunction}>deselect</button>
                <button type="button" className="btn btn-primary btn-sm" onClick={this.props.redactFunction}>redact</button>
			</div>
    	);
    }

}

export default EntityHeader;