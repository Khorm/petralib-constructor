import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import 'bootstrap/dist/css/bootstrap.min.css';

import store from './redux-mod';

import EntitiesCanvas from './canvas/canvas';
import PrecedentManager from './precedent/precedent-manager';
import TransactionManager from './precedent/transaction/transaction-manager';
//import EntitiesList from './list/list';


class App extends React.Component{


    render (){

        return (
        <>
            <div>

                <div>
                    <PrecedentManager />
                    <TransactionManager />
                    <EntitiesCanvas />
                </div>

            </div>
        </>
        );
    }

}

ReactDOM.render(

  <Provider store={store}>
    <div >
   		<App/>
    </div>
  </Provider>,
  document.getElementById('react')
);