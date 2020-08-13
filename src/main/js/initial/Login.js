import React, {Component} from 'react';
import {InputText} from "primereact/inputtext";
import {Password} from "primereact/password";
import { Button } from 'primereact/button';
import UserWebService from "../webService/UserWebService";
import {Link, Redirect} from 'react-router-dom';

export default class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {login: '', password: ''},
            isLoggedIn: false,
            authorizedUser: {}
        };

        this.onSubmit = this.onSubmit.bind(this);
    }

    updateProperty(property, value) {
        let user = this.state.user;
        user[property] = value;
        this.setState({user: user});
    }

    onSubmit(event) {
        event.preventDefault();
        UserWebService.login(this.state.user).then(response => {
            console.log('Login.onSubmit response=');
            console.log(response);
            this.setState({ isLoggedIn: true, authorizedUser: response.data })
        }, error => {
            this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    render() {
        return ( this.state.isLoggedIn ?
            <Redirect to={{pathname: '/main', state: { currentUser: this.state.authorizedUser, from: this.props?.location?.pathname }}} /> :
            <form className='p-grid p-fluid input-fields center-container' onSubmit={this.onSubmit}>
                <div>
                    <div className='p-col-4'><label htmlFor='login'>Login</label></div>
                    <div className='p-col-8'>
                        <InputText id='login' onChange={(e) => this.updateProperty('login', e.target.value)} value={this.state.user.login}/>
                    </div>
                </div>

                <div>
                    <div className='p-col-4'><label htmlFor='password'>Password</label></div>
                    <div className='p-col-8'>
                        <Password id='password' onChange={(e) => this.updateProperty('password', e.target.value)} feedback={false} value={this.state.user.password} />
                    </div>
                </div>
                <div className='container-space-between'>
                    <Button label="Submit" className="p-button-raised submit-button" onClick={this.onSubmit} />
                    <Link to={'/home'} ><Button label="Back" className="p-button-raised"/></Link>
                </div>
            </form>
        )
    }
}