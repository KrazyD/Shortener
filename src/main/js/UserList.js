const User = require('./User');
const React = require('react');

module.exports = class UserList extends React.Component{
    render() {
        const users = this.props.users.map(user =>
            <User key={user.userId} user={user}/>
        );
        return (
            <table>
                <tbody>
                <tr>
                    <th>UserId</th>
                    <th>Username</th>
                    <th>Login</th>
                    <th>Password</th>
                    <th>IsAdmin</th>
                </tr>
                {users}
                </tbody>
            </table>
        )
    }
};