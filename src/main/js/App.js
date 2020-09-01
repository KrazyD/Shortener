import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { Switch, Route, Redirect } from 'react-router-dom';
import { Growl } from "primereact/growl";

import Login from './initial/Login';
import Register from './initial/Register';
import Home from './initial/Home'
import Logout from "./app/Logout";
import Main from "./app/Main";
import Error from "./Error";

import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

class App extends Component {

    constructor(props) {
        super(props);

        this.growl = null;
    }


    render() {

        this.getGrowl = ref => {
            if(ref) {
                this.growl = ref;
            }
            return this.growl;
        };

        return (
            <div>
                <Growl ref={this.getGrowl} />
                <Switch>
                    <Route path='/home' render={(props) => <Home {...props} getGrowl={this.getGrowl} /> } />
                    <Route path='/login' render={(props) => <Login {...props} getGrowl={this.getGrowl} /> }/>
                    <Route path='/register' render={(props) => <Register {...props} getGrowl={this.getGrowl} />} />
                    <Route path='/logout' render={(props) => <Logout {...props} getGrowl={this.getGrowl} /> }/>
                    <Route path='/main' render={(props) => <Main {...props} getGrowl={this.getGrowl} />} />
                    <Route path='/error' render={(props) => <Error from='/' {...props} getGrowl={this.getGrowl} />} />
                    <Redirect exact from='/' to={{pathname: '/home', state:{...this.props?.location?.state} }} />
                </Switch>
            </div>
        )
    }
}

ReactDOM.render(
    <BrowserRouter>
        <App/>
    </BrowserRouter>,
    document.getElementById('react')
);