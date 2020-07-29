import React, {Component} from 'react';
import {InputText} from "primereact/inputtext";
import {Password} from "primereact/password";
import { Button } from 'primereact/button';
import UserWebService from "../webService/UserWebService";
import {Redirect} from 'react-router-dom';

export default class Register extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {username: '', login: '', password: ''},
            isSuccessRegistered: false
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
        UserWebService.registerUser(this.state.user).then(response => {
            this.setState({ isSuccessRegistered: true })
        }, error => {
            this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    render() {
        return ( this.state.isSuccessRegistered ?
            <Redirect to={{pathname: '/usersList'}} /> :
            <div className='p-grid p-fluid input-fields'>
                <div className='p-col-4'><label htmlFor='username'>Username</label></div>
                <div className='p-col-8'>
                    <InputText id='username' onChange={(e) => this.updateProperty('username', e.target.value)} value={this.state.user.username}/>
                </div>

                <div className='p-col-4'><label htmlFor='login'>Login</label></div>
                <div className='p-col-8'>
                    <InputText id='login' onChange={(e) => this.updateProperty('login', e.target.value)} value={this.state.user.login}/>
                </div>

                <div className='p-col-4'><label htmlFor='password'>Password</label></div>
                <div className='p-col-8'>
                    <Password id='password' onChange={(e) => this.updateProperty('password', e.target.value)} value={this.state.user.password} />
                </div>
                <Button label="Submit" className="p-button-raised" onClick={this.onSubmit} />
            </div>
        )
    }
}