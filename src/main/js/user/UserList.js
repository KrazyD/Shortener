import React from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {InputText} from 'primereact/inputtext';
import {Growl} from 'primereact/growl';
import client from '../client';
import { ProgressSpinner } from 'primereact/progressspinner';
import { Button } from "primereact/button";
import UserDialog from "./UserDialog";
import {ContextMenu} from 'primereact/contextmenu';

// TODO Убрать PUT из метода валидации
// TODO сделать валидацию ввода
// TODO Возвращать старое значение если после сохранения сервер ответил ошибкой

export default class UserList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            loading: false,
            selectedUser: null,
            isDialogDisplay: false
        };

        this.menu = [
            {label: 'Add', icon: 'pi pi-fw pi-plus', command: () => this.addNew()},
            {label: 'Edit', icon: 'pi pi-fw pi-pencil', command: () => this.onRowEdit(this.state.selectedUser)},
            {label: 'Delete', icon: 'pi pi-fw pi-trash', command: () => this.onRowDelete(this.state.selectedUser)}
        ];

        this.addNew = this.addNew.bind(this);
        this.onRowEdit = this.onRowEdit.bind(this);
        this.onRowDelete = this.onRowDelete.bind(this);
    }

    componentDidMount() {
        client({method: 'GET', path: '/user'}).done(response => {
            this.setState({users: response.entity.data.sort((a, b) => a.userId - b.userId)});
        });
    }

    onRowEdit(selectedUser) {
        this.setState({
            selectedUser: selectedUser,
            isDialogDisplay: true
        });
    }

    onRowDelete(selectedUser) {
        client({method: 'DELETE', path: '/user?id=' + selectedUser.userId}).then(response => {
            let index = this.state.users.indexOf(selectedUser);
            this.setState({users: this.state.users.filter((val, i) => i !== index)});
            this.growl.show({severity: 'success', summary: 'Success', detail: 'User is deleted'});
        }, err => {
            let errMessage = err.cause ? err.cause.message : err.entity.data;
            this.growl.show({severity: 'error', summary: 'Error', detail: errMessage});
        });
    }

    handleUserFromDialog = (user, isNew) => {
        if (user) {
            if (isNew) {
                client({method: 'POST', path: '/user', entity: user}).then(response => {
                    let users = [...this.state.users];
                    users.push(response.entity.data);
                    this.setState({
                        users: users,
                        isDialogDisplay: false
                    });
                    this.growl.show({severity: 'success', summary: 'Success', detail: 'User is registered'});
                }, err => {
                    this.setState({isDialogDisplay: false});
                    let errMessage = err.cause ? err.cause.message : err.entity.data;
                    this.growl.show({severity: 'error', summary: 'Error', detail: errMessage});
                });
            } else {
                client({method: 'PUT', path: '/user', entity: user}).then(response => {
                    this.setState({isDialogDisplay: false});
                    this.growl.show({severity: 'success', summary: 'Success', detail: 'User is updated'});
                }, err => {
                    this.setState({isDialogDisplay: false});
                    let errMessage = err.cause ? err.cause.message : err.entity.data;
                    this.growl.show({severity: 'error', summary: 'Error', detail: errMessage});
                });
            }
        } else {
            this.setState({
                isDialogDisplay: false
            });
        }
    };

    addNew() {
        this.setState({
            selectedUser: null,
            isDialogDisplay: true
        });
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
                    <Column field="userId" header="UserId" style={{height: '3.5em'}}/>
                    <Column field="username" header="Username" style={{height: '3.5em'}}/>
                    <Column field="login" header="Login" style={{height: '3.5em'}}/>
                    <Column field="password" header="Password" style={{height: '3.5em'}}/>
                    <Column field="isAdmin" header="IsAdmin" style={{height: '3.5em'}}/>
                </DataTable>
                <UserDialog isDialogDisplay={this.state.isDialogDisplay}
                            onChangeFinish={this.handleUserFromDialog}
                            user={Object.assign({}, this.state.selectedUser)} />
            </div>
        )
    }
};