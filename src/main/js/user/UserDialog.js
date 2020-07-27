import React, {Component} from 'react';
import {InputText} from 'primereact/inputtext';
import {Button} from "primereact/button";
import {Dialog} from 'primereact/dialog';

export default class UserDialog extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {'username': '', 'login': '', 'password': '', 'isAdmin': ''},
            onChangeFinish: this.props.onChangeFinish,
            isNewUser: true
        };

        this.onSave = this.onSave.bind(this);
        this.onShow = this.onShow.bind(this);
        this.onCancelAdding = this.onCancelAdding.bind(this);
    }

    componentDidMount() {
    }

    onSave() {
        this.props.onChangeFinish(this.state.user, this.state.isNewUser);
    }

    onShow() {
        this.setState({
            user: this.props.user ? this.props.user : {'username': '', 'login': '', 'password': '', 'isAdmin': ''},
            isNewUser: !this.props.user
        })
    }

    onCancelAdding() {
        this.props.onChangeFinish(null, this.state.isNewUser);
    }

    updateProperty(property, value) {
        let user = this.state.user;
        user[property] = value;
        this.setState({user: user});
    }

    render() {
        let dialogFooter = <div className="ui-dialog-buttonpane p-clearfix">
            <Button label="Save" icon="pi pi-check" onClick={this.onSave}/>
            <Button label="Cancel" icon="pi pi-times" onClick={this.onCancelAdding}/>
        </div>;

        return (
            <Dialog visible={this.props.isDialogDisplay}
                    style={{width: '300px'}} header="New User"
                    modal={true} footer={dialogFooter}
                    blockScroll={false}
                    closable={false}
                    onHide={() => {}}
                    onShow={this.onShow}>
                {
                    this.state.user &&

                    <div className="p-grid p-fluid">
                        <div className="p-col-4" style={{padding:'.75em'}}><label htmlFor="username">Username</label></div>
                        <div className="p-col-8" style={{padding:'.5em'}}>
                            <InputText id="username" onChange={(e) => {this.updateProperty('username', e.target.value)}} value={this.state.user.username}/>
                        </div>

                        <div className="p-col-4" style={{padding:'.75em'}}><label htmlFor="login">Login</label></div>
                        <div className="p-col-8" style={{padding:'.5em'}}>
                            <InputText id="login" onChange={(e) => {this.updateProperty('login', e.target.value)}} value={this.state.user.login}/>
                        </div>

                        <div className="p-col-4" style={{padding:'.75em'}}><label htmlFor="password">Password</label></div>
                        <div className="p-col-8" style={{padding:'.5em'}}>
                            <InputText id="password" onChange={(e) => {this.updateProperty('password', e.target.value)}} value={this.state.user.password}/>
                        </div>

                        <div className="p-col-4" style={{padding:'.75em'}}><label htmlFor="isAdmin">IsAdmin</label></div>
                        <div className="p-col-8" style={{padding:'.5em'}}>
                            <InputText id="isAdmin" onChange={(e) => {this.updateProperty('isAdmin', e.target.value)}} value={this.state.user.isAdmin}/>
                        </div>
                    </div>
                }
            </Dialog>
        )
    }
}