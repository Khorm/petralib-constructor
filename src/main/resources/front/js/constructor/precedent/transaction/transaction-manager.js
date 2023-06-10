import React from 'react';
import axios from 'axios';

import { transactionsList, currentTransaction } from './transaction-action-types';
import store from '../../redux-mod';
import { connect } from 'react-redux';

import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Button from 'react-bootstrap/Button';

import OptionsModal from '../options-modal';
//import GroupInterface from './group-button-interface';
import List from '../list';

class TransactionManagerClass extends React.Component{
	
 	constructor(props) {
        super(props);
        this.state = {
            showTransactionModal: false,
            modalAlerts:[],
            modalTransaction: undefined,
        }
    }

	componentDidMount(){
	    //store.dispatch(setCurrentPrecedentFunc(this.setCurrentPrecedent));
        this.updateTransactions();
	}

	updateTransactions = () => {
        if (this.props.currentPrecedent === undefined) return;
	    axios.get('/api/transaction/precedent/'+ this.props.currentPrecedent)
        .then(response => {
            store.dispatch(transactionsList(response.data));
        });
	}



	switchModal = () => {
	    this.setState({
	        showTransactionModal: !this.state.showTransactionModal,
	        modalAlerts: [],
	    });
	}


	addNewTransaction = () => {
	    let newTransaction = {};
	    this.setState({
            showTransactionModal: !this.state.showTransactionModal,
            modalTransaction: newTransaction,
        });
	}

	editTransaction = (transactionToEdit) => {
        this.setState({
            showTransactionModal: !this.state.showTransactionModal,
            modalTransaction: Object.assign({}, transactionToEdit),
        });
	}

//	changeRedactName = (event) => {
//	    let newOptionGroup = this.state.optionGroup;
//	    newOptionGroup.name = event.target.value;
//	    this.setState({
//	        optionGroup : Object.assign({}, newOptionGroup)
//	    });
//	}
//
//	changeRedactDescr = (event) => {
//    	    let newOptionGroup = this.state.optionGroup;
//    	    newOptionGroup.description = event.target.value;
//    	    this.setState({
//    	        optionGroup : Object.assign({}, newOptionGroup)
//    	    });
//    }

    saveTransaction = (id, name, description) => {
         let savingTransaction = {
                id: id,
                name: name,
                description: description,
            }
        axios.post('/api/transaction', savingTransaction)
        .then(response => {
//            let newGroup = response.data;
//            let map = new Map(this.props.groups);
//            map.set(''+newGroup.id, newGroup);
//            store.dispatch(groupsList(map));
            this.updateTransactions();
            this.switchModal();
        }).catch(error => {
              let errors = [error.response.data.errorMessage].concat(error.response.data.errors);
              this.setState({
                 optionAlerts: errors
              })
        });
    }

    deleteTransaction = (transactionId) =>{
        axios.delete('/api/transaction/'+transactionId)
            .then(response=>{
//                let map = new Map(this.props.groups);
//                map.delete(this.props.currentGroup);
                store.dispatch(currentTransaction(-1));
                this.updateTransactions();
                //store.dispatch(groupsList(map));
            })
    }


    setCurrentTransaction = (precedentId) => {
        store.dispatch(currentTransaction(precedentId));
    }



    render (){
   		return (
			<>
                <Button variant="outline-primary" onClick={this.switchModal}>Transaction options</Button>

                <List objectsList={this.props.precedentTransactions} chosenObjectId={this.props.currentTransaction}
                chooseObjectFunc={this.setCurrentTransaction} addFunc={this.addNewTransaction}/>

                <OptionsModal workObject={this.props.currentTransaction} alerts={this.state.modalAlerts} name={"Transaction"}
                closeFunc={this.switchModal} saveFunc={this.saveTransaction} deleteFunc={this.deleteTransaction}
                idModalOpened={this.state.showTransactionModal} />
			</>
    	);
    }

}


const mapStateToProps = function(state){
    return {
        precedentTransactions: state.transactionState.precedentTransactions,
        currentTransaction: state.transactionState.currentTransaction,
    }
}

const TransactionManager = connect(mapStateToProps) (TransactionManagerClass);
export default TransactionManager;

