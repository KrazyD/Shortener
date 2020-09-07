import React, { Component } from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';

import UserList from "../user/UserList";
import Error from "../Error";
import Header from "./Header";
import RefList from "../reference/RefList";
import UserWebService from "../webService/UserWebService";

export default class Main extends Component {

    constructor(props) {
        super(props);

        this.state = {
            isUserReceived: false,
            authorizedUser: {}
        };

        if(this.props?.location?.search === '?authorised') {
            UserWebService.getCurrentLogin().then(response => {
                this.setState({ authorizedUser: Object.assign({}, response.data), isUserReceived: true })
            }, error => {
                this.props.getGrowl().show({severity: 'error', summary: error.status, detail: error.message});
            });
        }
    }

    render() {

        return (
            <div>
                <Header {...this.props} />
                <Switch>
                    <Route path='/main/usersList' render={
                        (props) => {
                            let roles = props?.location?.state?.currentUser?.roles;
                            if (roles && roles.includes('ROLE_ADMIN')) {
                                return <UserList {...props} getGrowl={this.props.getGrowl} />;
                            } else {
                                return <Error {...props} message={'Access is denied!'} getGrowl={this.props.getGrowl} />
                            }
                        }
                    } />
                    <Route path='/main/refsList' render={(props) => <RefList {...props} getGrowl={this.props.getGrowl} />} />
                    {this.state.isUserReceived && <Redirect to={{ pathname: '/main/refsList',
                        state:{ currentUser: this.state.authorizedUser, ...this.props?.location?.state} }} />
                    }
                </Switch>
            </div>
        )
    }
}
