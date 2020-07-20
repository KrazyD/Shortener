const React = require('react');

module.exports = class User extends React.Component{
    render() {
        return (
            <tr>
                <td>{this.props.user.userId}</td>
                <td>{this.props.user.username}</td>
                <td>{this.props.user.login}</td>
                <td>{this.props.user.password}</td>
                <td>{this.props.user.isAdmin + ''}</td>
            </tr>
        )
    }
};