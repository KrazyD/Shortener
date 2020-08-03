import React, {Component} from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';

import UserList from "../user/UserList";
import Error from "../Error";
import Header from "./Header";
import RefList from "../reference/RefList";

export default class Main extends Component {

    constructor(props) {
        super(props);
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
                                return <UserList {...props} />;
                            } else {
                                return <Error {...props} message={'Access is denied!'} />
                            }
                        }
                    } />
                    <Route path='/main/refsList' render={(props) => <RefList {...props} growl={this.growl}  />} />
                    <Redirect exact from='/main' to={{pathname: '/main/usersList', state:{...this.props?.location?.state} }}/>
                </Switch>
            </div>
        )
    }
}
