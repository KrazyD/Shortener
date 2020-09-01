import React, {Component} from 'react';
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';
import {Dialog} from 'primereact/dialog';
import {Password} from 'primereact/password';
import {ListBox} from 'primereact/listbox';

export default class UserDialog extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {username: '', login: '', password: '', roles: []},
            onChangeFinish: props.onChangeFinish,
            isNewUser: true,
            selectedRoles: []
        };

        this.onSave = this.onSave.bind(this);
        this.onShow = this.onShow.bind(this);
        this.onCancelAdding = this.onCancelAdding.bind(this);
        this.updateProperty = this.updateProperty.bind(this);
    }

    onSave() {
        let user = this.state.user;
        user.roles = this.state.selectedRoles;
        this.props.onChangeFinish(user, this.state.isNewUser);
    }

    onShow() {
        this.setState({
            user: this.props.user || {username: '', login: '', password: '', roles: []},
            isNewUser: !this.props.user.hasOwnProperty('id'),
            selectedRoles: this.props.user.roles || []
        });
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
        let dialogFooter = <div className='ui-dialog-buttonpane p-clearfix'>
            <Button label='Save' icon='pi pi-check' onClick={this.onSave}/>
            <Button label='Cancel' icon='pi pi-times' onClick={this.onCancelAdding}/>
        </div>;

        let possibleRoles = ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_READER', 'ROLE_WRITER'];

        return (
            <Dialog visible={this.props.isDialogDisplay}
                    style={{width: '300px'}} header={this.state.isNewUser ? 'New user' : 'Modify user'}
                    modal={true} footer={dialogFooter}
                    blockScroll={false}
                    closable={false}
                    onHide={() => {this.setState({user: null})}}
                    onShow={this.onShow}>
                {
                    this.state.user &&

                    <div className='p-grid p-fluid input-fields'>
                        <div className='p-col-4'><label htmlFor='username'>Username</label></div>
                        <div className='p-col-8'>
                            <InputText id='username' value={this.state.user.username}
                                       onChange={(e) => this.updateProperty('username', e.target.value)}/>
                        </div>

                        <div className='p-col-4'><label htmlFor='login'>Login</label></div>
                        <div className='p-col-8'>
                            <InputText id='login' value={this.state.user.login}
                                       onChange={(e) => this.updateProperty('login', e.target.value)} />
                        </div>

                        <div className='p-col-4'><label htmlFor='password'>Password</label></div>
                        <div className='p-col-8'>
                            <Password id='password' value={this.state.user.password}
                                      onChange={(e) => this.updateProperty('password', e.target.value)} />
                        </div>

                        <ListBox value={this.state.selectedRoles}  options={possibleRoles} multiple={true}
                                 onChange={(e) => this.setState({selectedRoles: e.value})} />
                    </div>
                }
            </Dialog>
        )
    }
}