import React, {Component} from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import UserWebService from '../webService/UserWebService';

import {ContextMenu} from 'primereact/contextmenu';
import RefWebService from "../webService/RefWebService";
import RefDialog from "./RefDialog";

export default class RefList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            refs: [],
            selectedRef: {fullRef: '', reducedRef: '', requestsNumb: 0, userId: 0},
            isDialogDisplay: false
        };

        this.menu = [
            {label: 'Add', icon: 'pi pi-fw pi-plus', command: () => this.addRowAdd()},
            {label: 'Edit', icon: 'pi pi-fw pi-pencil', command: () => this.onRowEdit(this.state.selectedRef)},
            {label: 'Delete', icon: 'pi pi-fw pi-trash', command: () => this.onRowDelete(this.state.selectedRef)}
        ];

        this.addRowAdd = this.addRowAdd.bind(this);
        this.onRowEdit = this.onRowEdit.bind(this);
        this.onRowDelete = this.onRowDelete.bind(this);
    }

    componentDidMount() {
        RefWebService.getRefs().then(response => {
            console.log('REFS=');
            console.log(response);

            this.setState({refs: response.data});
        }, error => {
            this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    addRowAdd() {
        this.setState({
            selectedRef:  {fullRef: '', reducedRef: '', requestsNumb: 0, userId: 0},
            isDialogDisplay: true
        });
    }

    onRowEdit(selectedRef) {
        this.setState({
            selectedRef: selectedRef,
            isDialogDisplay: true
        });
    }

    onRowDelete(selectedRef) {
        UserWebService.deleteUser(selectedRef).then(response => {
            let index = this.state.refs.indexOf(selectedRef);
            this.setState({refs: this.state.refs.filter((val, i) => i !== index)});
            this.props.growl.show({severity: 'success', summary: response.status, detail: response.data});
        }, error => {
            this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    handleRefFromDialog = (user, isNew) => {
        if (user) {
            if (isNew) {
                UserWebService.registerUser(user).then(response => {
                    let refs = [...this.state.refs];
                    refs.push(response.data);
                    this.setState({
                        refs: refs,
                        isDialogDisplay: false
                    });
                    this.props.growl.show({severity: 'success', summary: 'Success', detail: 'User is registered'});
                }, error => {
                    this.setState({isDialogDisplay: false});
                    this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
                });
            } else {
                UserWebService.updateUser(user).then(response => {
                    let refs = [...this.state.refs];
                    let index = refs.findIndex((item) => item.id === response.data.id);
                    refs[index] = Object.assign({}, response.data);
                    this.setState({
                        isDialogDisplay: false,
                        refs: refs});
                    this.props.growl.show({severity: 'success', summary: 'Success', detail: 'User is updated'});
                }, error => {
                    this.setState({isDialogDisplay: false});
                    this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
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
                <ContextMenu model={this.menu} ref={el => this.cm = el} />

                <DataTable value={this.state.refs} editMode="row"
                           contextMenuSelection={(e) => this.state.selectedRef}
                           onContextMenuSelectionChange={e => this.setState({selectedRef: e.value})}
                           onContextMenu={e => this.cm.show(e.originalEvent)}>
                    <Column field="id" header="ReferenceId" style={{height: '3.5em'}}/>
                    <Column field="fullRef" header="FullRef" style={{height: '3.5em'}}/>
                    <Column field="reducedRef" header="ReducedRef" style={{height: '3.5em'}}/>
                    <Column field="requestsNumb" header="RequestsNumb" style={{height: '3.5em'}}/>
                    <Column field="userId" header="UserId" style={{height: '3.5em'}}/>
                    {/*<Column body={this.renderRoles} field="roles" header="Roles" style={{height: '3.5em'}}/>*/}
                </DataTable>
                <RefDialog isDialogDisplay={this.state.isDialogDisplay}
                            onChangeFinish={this.handleRefFromDialog}
                            user={Object.assign({}, this.state.selectedRef)} />
            </div>
        )
    }
};