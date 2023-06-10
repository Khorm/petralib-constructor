import React from 'react'



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