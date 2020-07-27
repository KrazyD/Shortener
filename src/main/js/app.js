import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import client from './client';
import UserList from './user/UserList';

import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';


class App extends Component {

    constructor(props) {
        super(props);
        this.state = {users: []};
    }

    componentDidMount() {
    }

    render() {
        return (
            <UserList/>
            )
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
);