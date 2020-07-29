import React, {Component} from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Growl} from 'primereact/growl';
import WebService from '../webService/WebService';

import UserDialog from "./UserDialog";
import {ContextMenu} from 'primereact/contextmenu';

// TODO Убрать PUT из метода валидации
// TODO сделать валидацию ввода
// TODO Возвращать старое значение если после сохранения сервер ответил ошибкой

export default class UserList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            loading: false,
            selectedUser: {username: '', login: '', password: '', roles: []},
            isDialogDisplay: false
        };

        this.menu = [
            {label: 'Add', icon: 'pi pi-fw pi-plus', command: () => this.addRowAdd()},
            {label: 'Edit', icon: 'pi pi-fw pi-pencil', command: () => this.onRowEdit(this.state.selectedUser)},
            {label: 'Delete', icon: 'pi pi-fw pi-trash', command: () => this.onRowDelete(this.state.selectedUser)}
        ];

        this.addRowAdd = this.addRowAdd.bind(this);
        this.onRowEdit = this.onRowEdit.bind(this);
        this.onRowDelete = this.onRowDelete.bind(this);
    }

    componentDidMount() {
        WebService.getUsers().then(response => {
            this.setState({users: response.data});
        }, error => {
            this.growl.show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    addRowAdd() {
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
        WebService.deleteUser(selectedUser).then(response => {
            let index = this.state.users.indexOf(selectedUser);
            this.setState({users: this.state.users.filter((val, i) => i !== index)});
            this.growl.show({severity: 'success', summary: response.status, detail: response.data});
        }, error => {
            this.growl.show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    handleUserFromDialog = (user, isNew) => {
        if (user) {
            if (isNew) {
                WebService.registerUser(user).then(response => {
                    let users = [...this.state.users];
                    users.push(response.data);
                    this.setState({
                        users: users,
                        isDialogDisplay: false
                    });
                    this.growl.show({severity: 'success', summary: 'Success', detail: 'User is registered'});
                }, error => {
                    this.setState({isDialogDisplay: false});
                    this.growl.show({severity: 'error', summary: error.status, detail: error.message});
                });
            } else {
                WebService.updateUser(user).then(response => {
                    let users = [...this.state.users];
                    let index = users.findIndex((item) => item.id === response.data.id);
                    users[index] = Object.assign({}, response.data);
                    this.setState({
                        isDialogDisplay: false,
                        users: users});
                    this.growl.show({severity: 'success', summary: 'Success', detail: 'User is updated'});
                }, error => {
                    this.setState({isDialogDisplay: false});
                    this.growl.show({severity: 'error', summary: error.status, detail: error.message});
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

        return (
            <div>
                <Growl ref={(el) => this.growl = el} />

                <ContextMenu model={this.menu} ref={el => this.cm = el} />

                <DataTable value={this.state.users} editMode="row"
                           contextMenuSelection={(e) => this.state.selectedUser}
                           onContextMenuSelectionChange={e => this.setState({selectedUser: e.value})}
                           onContextMenu={e => this.cm.show(e.originalEvent)}>
                    <Column field="id" header="UserId" style={{height: '3.5em'}}/>
                    <Column field="username" header="Username" style={{height: '3.5em'}}/>
                    <Column field="login" header="Login" style={{height: '3.5em'}}/>
                    <Column field="password" header="Password" style={{height: '3.5em'}}/>
                    <Column body={this.renderRoles} field="roles" header="Roles" style={{height: '3.5em'}}/>
                </DataTable>
                <UserDialog isDialogDisplay={this.state.isDialogDisplay}
                            onChangeFinish={this.handleUserFromDialog}
                            user={Object.assign({}, this.state.selectedUser)} />
            </div>
        )
    }
};