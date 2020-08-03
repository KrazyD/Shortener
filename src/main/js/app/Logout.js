import React, {Component} from 'react';
import { Redirect} from 'react-router-dom';

export default class Logout extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        this.props.growl.show({severity: 'success', summary: 'Logout', detail: 'You successfully logout from account '
                + this.props?.location?.state?.currentUser?.username });
        return ( <Redirect to={{pathname: '/home'}} /> )
    }
}
