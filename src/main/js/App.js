import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import Login from "./initial/Login";
import Register from "./initial/Register";
import { BrowserRouter } from 'react-router-dom';
import { Button } from 'primereact/button';
import { Link, Switch, Route, Redirect } from "react-router-dom";

import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import UserList from "./user/UserList";

class App extends Component {

    constructor(props) {
        super(props);

    }

    componentDidMount() {
    }

    render() {
        return (
            <div>
                <Link to={'/login'} ><Button label="Login" className="p-button-raised"/></Link>
                <Link to={'/register'}><Button label="Register" className="p-button-raised"/></Link>
                <Switch>
                    <Route path='/login' component={Login} />
                    <Route path='/register' component={Register} />
                    <Route path='/usersList' component={UserList} />
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