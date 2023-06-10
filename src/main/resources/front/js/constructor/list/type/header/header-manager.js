import axios from 'axios';
import React from 'react'

//import Button from 'react-bootstrap/Button'


class TypeHeader extends React.Component{

 	constructor(props) {
        super(props);
        this.state = {
            filter: "",
        }
    }


	changeFilter = (event) => {
	     this.setState({
            filter:event.target.value
	     }, () => {this.props.setFilter(this.state.filter)});

	}


    render (){
   		return (
			<div>
                <h6>Types</h6>
                <input type="text" onChange={this.changeFilter} value={this.state.filter}/>
                <button type="button" className="btn btn-primary btn-sm" onClick={this.search} >search</button>
                <button type="button" className="btn btn-primary btn-sm" onClick={this.props.openCloseModal}>add</button>
                <button type="button" className="btn btn-primary btn-sm" onClick={this.props.chooseType}>deselect</button>
                <button type="button" className="btn btn-primary btn-sm" onClick={this.props.redactFunction}>redact</button>
			</div>
    	);
    }

}

export default TypeHeader;