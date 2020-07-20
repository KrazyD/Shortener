const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const UserList = require('./UserList');

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