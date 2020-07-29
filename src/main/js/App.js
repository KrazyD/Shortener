import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { Switch, Route, Redirect } from 'react-router-dom';
import {Growl} from "primereact/growl";

import Login from './initial/Login';
import Register from './initial/Register';
import Home from './initial/Home'
import UserList from './user/UserList';
import RefList from "./reference/RefList";

import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

class App extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <Growl ref={(el) => this.growl = el} />
                <Switch>
                    <Route path='/home' component={Home} />
                    <Route path='/login' render={() => <Login growl={this.growl} />}/>
                    <Route path='/register' render={() => <Register growl={this.growl} />} />
                    <Route path='/usersList' render={() => <UserList growl={this.growl} />} />
                    <Route path='/refsList' render={() => <RefList growl={this.growl} />} />
                    <Redirect from='/' to='/home'/>
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