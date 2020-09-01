import React, {Component} from 'react';
import {InputText} from "primereact/inputtext";
import {Password} from "primereact/password";
import { Button } from 'primereact/button';
import {Link} from 'react-router-dom';

export default class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {login: '', password: ''},
            authorizedUser: {}
        };
    }

    updateProperty(property, value) {
        let user = this.state.user;
        user[property] = value;
        this.setState({user: user});
    }

    render() {
        return (
            <form className='p-grid p-fluid input-fields center-container' action={'/login'} method='post'>
                <div>
                    <div className='p-col-4'><label htmlFor='login'>Login</label></div>
                    <div className='p-col-8'>
                        <InputText name='login' id='login'
                                   onChange={(e) => this.updateProperty('login', e.target.value)}
                                   value={this.state.user.login}/>
                    </div>
                </div>

                <div>
                    <div className='p-col-4'><label htmlFor='password'>Password</label></div>
                    <div className='p-col-8'>
                        <Password name='password' id='password'
                                  onChange={(e) => this.updateProperty('password', e.target.value)}
                                  feedback={false} value={this.state.user.password} />
                    </div>
                </div>
                <div className='container-space-between'>
                    <Button type='submit' label="Submit" className="p-button-raised submit-button" />
                    <Link to={'/home'} ><Button label="Back" className="p-button-raised"/></Link>
                </div>
            </form>
        )
    }
}