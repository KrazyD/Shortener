import React, {Component} from 'react';
import { Button } from 'primereact/button';
import {Link} from 'react-router-dom';

export default class Home extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
                <div className='center-container'>
                    <h3>Log in or create an account</h3>
                    <div>
                        <Link to={'/login'} ><Button label="Login" className="p-button-raised"/></Link>
                        <Link to={'/register'}><Button label="Register" className="p-button-raised"/></Link>
                    </div>
                </div>
        )
    }
}