import React, {Component} from 'react';
import { Button } from 'primereact/button';
import {Link} from 'react-router-dom';

export default class Header extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className='container-space-between'>
                { this.props?.location?.state?.currentUser?.roles?.includes('ROLE_ADMIN') &&
                    <div>
                        <Link to={{pathname: '/main/usersList',
                            state:{ from: this.props?.location?.pathname, ...this.props?.location?.state} }} >
                            <Button label="To users list" className="p-button-raised"/>
                        </Link>
                        <Link to={{pathname: '/main/refsList',
                            state:{ from: this.props?.location?.pathname, ...this.props?.location?.state} }} >
                            <Button label="To refs list" className="p-button-raised"/>
                        </Link>
                    </div>
                }

                <span>You logged in as {this.props?.location?.state?.currentUser?.username}</span>

                <Link to={{pathname: '/logout', state:{...this.props?.location?.state} }} >
                    <Button label="Logout" className="p-button-raised"/>
                </Link>
            </div>
        )
    }
}
