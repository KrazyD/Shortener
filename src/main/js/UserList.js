import React from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {InputText} from 'primereact/inputtext';
import {Growl} from 'primereact/growl';
import client from './client';
import {ProgressSpinner} from 'primereact/progressspinner';

export default class UserList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            updatedUsers: [],
            loading: false
        };

        this.clonedUsers = [];

        this.editorForRowEditing = this.editorForRowEditing.bind(this);
        this.onRowEditorValidator = this.onRowEditorValidator.bind(this);
        this.onRowEditInit = this.onRowEditInit.bind(this);
        this.onRowEditSave = this.onRowEditSave.bind(this);
        this.onRowEditCancel = this.onRowEditCancel.bind(this);
    }

    componentDidMount() {
        this.state.users = this.props.users;
    }

    onRowEditorValidator(rowData) {
        function sleep(time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        }

        client({method: 'GET', path: '/user'}).done(response => {

            sleep(10000).then(() => {
                console.log("TIMER STOP!");
                this.setState({loading: false});
            });
        });

        return true;
    }

    onRowEditInit(event) {
        this.clonedUsers[event.data.userId] = {...event.data};
    }

    onEditorValueChangeForRowEditing(allFieldsInTable, value) {
        let updatedUsers = [...allFieldsInTable.value];
        updatedUsers[allFieldsInTable.rowIndex][allFieldsInTable.field] = value;
        this.setState({updatedUsers: updatedUsers});
    }

    editorForRowEditing(rowProps, field) {
        return <InputText type="text" value={rowProps.rowData[field]} onChange={(e) => this.onEditorValueChangeForRowEditing(rowProps, e.target.value)} />;
    }

    onRowEditSave(event) {
        this.setState({loading: true});
        if (this.onRowEditorValidator(event.data)) {
            delete this.clonedUsers[event.data.userId];
            this.growl.show({severity: 'success', summary: 'Success', detail: 'User is updated'});
        }
        else {
            this.growl.show({severity: 'error', summary: 'Error', detail: 'Username is required'});
        }
    }

    onRowEditCancel(event) {
        let updatedUsers = [...this.state.updatedUsers];
        updatedUsers[event.index] = this.clonedUsers[event.data.userId];
        delete this.clonedUsers[event.data.userId];
        this.setState({
            updatedUsers: updatedUsers
        })
    }


    render() {
        return (
            <div>
                <Growl ref={(el) => this.growl = el} />

                <DataTable value={this.props.users} editMode="row" onRowEditInit={this.onRowEditInit} onRowEditSave={this.onRowEditSave} onRowEditCancel={this.onRowEditCancel}>
                    <Column field="userId" header="UserId" style={{height: '3.5em'}}/>
                    <Column field="username" header="Username" editor={(props) => this.editorForRowEditing(props, 'username')} style={{height: '3.5em'}}/>
                    <Column field="login" header="Login" editor={(props) => this.editorForRowEditing(props, 'login')} style={{height: '3.5em'}}/>
                    <Column field="password" header="Password" editor={(props) => this.editorForRowEditing(props, 'password')} style={{height: '3.5em'}}/>
                    <Column field="isAdmin" header="IsAdmin" editor={(props) => this.editorForRowEditing(props, 'isAdmin')} style={{height: '3.5em'}}/>
                    <Column rowEditor={true} style={{'width': '70px', 'textAlign': 'center'}}></Column>
                </DataTable>
                {this.state.loading ? <ProgressSpinner/> : <div/>}
            </div>
        )
    }
};