import React, {Component} from 'react';
import {InputText} from 'primereact/inputtext';
import {Password} from 'primereact/password';
import { Button } from 'primereact/button';
import {Link, Redirect} from 'react-router-dom';
import UserWebService from '../webService/UserWebService';

export default class Register extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {username: '', login: '', password: ''},
            isSuccessRegistered: false,
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
        UserWebService.registerUser(this.state.user).then(response => {
            console.log('UserWebService.registerUser');
            this.setState({ isSuccessRegistered: true, authorizedUser: response.data })
        }, error => {
            this.props.getGrowl().show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    render() {
        return ( this.state.isSuccessRegistered ?
            <Redirect to={{pathname: '/main/refsList', state: { currentUser: this.state.authorizedUser, from: this.props?.location?.pathname }}} /> :
            <div className='p-grid p-fluid input-fields center-container'>
                <div className='p-col-4'><label htmlFor='username'>Username</label></div>
                <div className='p-col-8'>
                    <InputText name='username' id='username' onChange={(e) => this.updateProperty('username', e.target.value)} value={this.state.user.username}/>
                </div>

                <div className='p-col-4'><label htmlFor='login'>Login</label></div>
                <div className='p-col-8'>
                    <InputText name='login' id='login' onChange={(e) => this.updateProperty('login', e.target.value)} value={this.state.user.login}/>
                </div>

                <div className='p-col-4'><label htmlFor='password'>Password</label></div>
                <div className='p-col-8'>
                    <Password name='password' id='password' onChange={(e) => this.updateProperty('password', e.target.value)} value={this.state.user.password} />
                </div>
                <div className='container-space-between'>
                    <Button type='submit' label='Submit' className='p-button-raised submit-button' onClick={this.onSubmit} />
                    <Link to={'/home'} ><Button label='Back' className='p-button-raised'/></Link>
                </div>
            </div>
        )
    }
}