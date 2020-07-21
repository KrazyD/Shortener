import React from 'react';
import ReactDOM from 'react-dom';
import client from './client';
import UserList from './UserList';

import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';


class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {users: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/user'}).done(response => {
            this.setState({users: response.entity.data});
    });
    }

    render() {
        return (
            <UserList users={this.state.users}/>
            )
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
);