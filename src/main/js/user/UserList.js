import React, {Component} from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import UserWebService from '../webService/UserWebService';

import UserDialog from './UserDialog';
import {ContextMenu} from 'primereact/contextmenu';
import {Button} from 'primereact/button';

export default class UserList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            selectedUser: {username: '', login: '', password: '', roles: []},
            isDialogDisplay: false
        };

        this.menu = [
            {label: 'Add', icon: 'pi pi-fw pi-plus', command: () => this.onRowAdd()},
            {label: 'Edit', icon: 'pi pi-fw pi-pencil', command: () => this.onRowEdit(this.state.selectedUser)},
            {label: 'Delete', icon: 'pi pi-fw pi-trash', command: () => this.onRowDelete(this.state.selectedUser)}
        ];

        this.onRowAdd = this.onRowAdd.bind(this);
        this.onRowEdit = this.onRowEdit.bind(this);
        this.onRowDelete = this.onRowDelete.bind(this);
    }

    componentDidMount() {
        UserWebService.getUsers().then(response => {
            this.setState({users: response.data});
        }, error => {
            this.props.getGrowl().show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    onRowAdd() {
        this.setState({
            selectedUser: {username: '', login: '', password: '', roles: []},
            isDialogDisplay: true
        });
    }

    onRowEdit(selectedUser) {
        this.setState({
            selectedUser: selectedUser,
            isDialogDisplay: true
        });
    }

    onRowDelete(selectedUser) {
        UserWebService.deleteUser(selectedUser).then(response => {
            let index = this.state.users.indexOf(selectedUser);
            this.setState({users: this.state.users.filter((val, i) => i !== index)});
            this.props.getGrowl().show({severity: 'success', summary: response.status, detail: response.data});
        }, error => {
            this.props.getGrowl().show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    handleUserFromDialog = (user, isNew) => {
        if (user) {
            if (isNew) {
                UserWebService.registerUser(user).then(response => {
                    let users = [...this.state.users];
                    users.push(response.data);
                    this.setState({
                        users: users,
                        isDialogDisplay: false
                    });
                    this.props.getGrowl().show({severity: 'success', summary: 'Success', detail: 'User is registered'});
                }, error => {
                    this.setState({isDialogDisplay: false});
                    this.props.getGrowl().show({severity: 'error', summary: error.status, detail: error.message});
                });
            } else {
                UserWebService.updateUser(user).then(response => {
                    let users = [...this.state.users];
                    let index = users.findIndex((item) => item.id === response.data.id);
                    users[index] = Object.assign({}, response.data);
                    this.setState({
                        isDialogDisplay: false,
                        users: users});
                    this.props.getGrowl().show({severity: 'success', summary: 'Success', detail: 'User is updated'});
                }, error => {
                    this.setState({isDialogDisplay: false});
                    this.props.getGrowl().show({severity: 'error', summary: error.status, detail: error.message});
                });
            }
        } else {
            this.setState({
                isDialogDisplay: false
            });
        }
    };

    renderRoles(rowData, column) {
        return rowData.roles ? rowData.roles.join(', ') : '';
    }

    render() {

        let footer = <div className='p-clearfix' >
            <Button label='Add' icon='pi pi-plus' onClick={this.onRowAdd}/>
        </div>;

        return (
            <div>
                <ContextMenu model={this.menu} ref={el => this.cm = el} />

                <DataTable value={this.state.users} editMode='row' footer={footer} header='Users list'
                           contextMenuSelection={(e) => this.state.selectedUser}
                           onContextMenuSelectionChange={e => this.setState({selectedUser: e.value})}
                           onContextMenu={e => this.cm.show(e.originalEvent)}>
                    <Column className='overflow-text' field='id' header='User Id' style={{width: '6%'}}/>
                    <Column className='overflow-text' field='username' header='Username' style={{width: '21%'}}/>
                    <Column className='overflow-text' field='login' header='Login' style={{width: '21%'}}/>
                    <Column className='overflow-text' field='password' header='Password' style={{width: '21%'}}/>
                    <Column body={this.renderRoles} field='roles' header='Roles' style={{width: '21%'}}/>
                </DataTable>
                <UserDialog isDialogDisplay={this.state.isDialogDisplay}
                            onChangeFinish={this.handleUserFromDialog}
                            user={Object.assign({}, this.state.selectedUser)} />
            </div>
        )
    }
};