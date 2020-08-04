import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { Switch, Route, Redirect } from 'react-router-dom';
import {Growl} from "primereact/growl";

import Login from './initial/Login';
import Register from './initial/Register';
import Home from './initial/Home'
import Logout from "./app/Logout";
import Main from "./app/Main";

import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

class App extends Component {

    constructor(props) {
        super(props);

        this.state = {
            growl: null
        };
    }

    render() {
        return (
            <div>
                <Growl ref={(el) => this.state.growl ? void(0): this.setState({growl: el}) } />
                <Switch>
                    <Route path='/home' render={(props) => <Home {...props} growl={this.state.growl} /> } />
                    <Route path='/login' render={(props) => <Login {...props} growl={this.state.growl} /> }/>
                    <Route path='/register' render={(props) => <Register {...props} growl={this.state.growl} />} />
                    <Route path='/logout' render={(props) => <Logout {...props} growl={this.state.growl} /> }/>
                    <Route path='/main' render={(props) => <Main {...props} growl={this.state.growl} />} />
                    <Redirect from='/' to={{pathname: '/home', state:{...this.props?.location?.state} }} />
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