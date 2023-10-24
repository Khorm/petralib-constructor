import React from 'react'


/**
List of variables in action.
Contains in props:
variable{ - part of variables list
    id - variable id
    type - variable type
    name - variable name
}

*/
class VariablesList extends React.Component{	

    render (){	
		
   		return (
			<div>		
				<h5>Variables</h5>				
				
				{this.props.variables.map((variable, i) => {     
		                           
		           return (<div key={variable.id}>
							{variable.type + ' : ' + variable.name}
							</div>) 
		        })}
			</div>
    	);
    }

}

export default VariablesList;