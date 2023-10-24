import React from 'react';
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import { CookiesProvider } from 'react-cookie';
import { withCookies, Cookies } from 'react-cookie';
import { instanceOf } from 'prop-types';

//import 'bootstrap/dist/css/bootstrap.min.css';

//import store from './redux-mod';


class App extends React.Component{

     static propTypes = {
        cookies: instanceOf(Cookies).isRequired
      };

    constructor(props) {
        super(props);
        const { cookies } = props;
        this.state = {
            login: "",
            password: ""
        }
    }



    changeLogin = (event) => {
         this.setState({
            login: event.target.value
         });
    }

    changePass = (event) => {
         this.setState({
            password: event.target.value
         });
    }

    sendLogin = () => {

        const { cookies } = this.props;
        axios.post('/api/v1/auth/login', {
          email: this.state.login,
          password: this.state.password
        })
        .then(function (response) {
          cookies.set('Authorization', response.data.token, { path: '/' });
          window.location.href = '/projects';
        })
        .catch(function (error) {
          console.log(error);
        });
    }


    render (){

        return (
        <>
            <div>
                <h2>Login</h2>
                <input type="text" onChange={this.changeLogin} value={this.state.login}/>
                <input type="password" onChange={this.changePass} value={this.state.password}/>
                <button type="button" className="btn btn-primary btn-sm" onClick={this.sendLogin}>send</button>
            </div>
        </>
        );
    }

}

export default withCookies(App);

//const container = document.getElementById('react');
//const root = createRoot(container);
//root.render(<CookiesProvider>
//                  <App />
//                </CookiesProvider>);
//ReactDOM.render(
//   		<App/>,
//  document.getElementById('react')
//);