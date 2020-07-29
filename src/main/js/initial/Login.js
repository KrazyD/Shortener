import React, {Component} from 'react';
import {InputText} from "primereact/inputtext";
import {Password} from "primereact/password";
import { Button } from 'primereact/button';
import WebService from "../webService/WebService";
import {Redirect} from 'react-router-dom';

export default class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {login: '', password: ''},
            isLoggedIn: false
        };

        this.onSubmit = this.onSubmit.bind(this);
    }

    componentDidMount() {

    }

    updateProperty(property, value) {
        let user = this.state.user;
        user[property] = value;
        this.setState({user: user});
    }

    onSubmit(event) {
        WebService.login(this.state.user).then(response => {
            this.setState({ isLoggedIn: true })
        }, err => {

        });
    }

    render() {
        return ( this.state.isLoggedIn ?
            <Redirect to={{pathname: '/usersList'}} /> :
            <div className='p-grid p-fluid input-fields'>
                <div className='p-col-4'><label htmlFor='login'>Login</label></div>
                <div className='p-col-8'>
                    <InputText id='login' onChange={(e) => this.updateProperty('login', e.target.value)} value={this.state.user.login}/>
                </div>

                <div className='p-col-4'><label htmlFor='password'>Password</label></div>
                <div className='p-col-8'>
                    <Password id='password' onChange={(e) => this.updateProperty('password', e.target.value)} feedback={false} value={this.state.user.password} />
                </div>
                <Button label="Submit" className="p-button-raised" onClick={this.onSubmit} />
            </div>
        )
    }
}