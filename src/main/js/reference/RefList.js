import React, {Component} from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import UserWebService from '../webService/UserWebService';

import {ContextMenu} from 'primereact/contextmenu';
import {Button} from 'primereact/button';
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
            {label: 'Add', icon: 'pi pi-fw pi-plus', command: () => this.onRowAdd()},
            {label: 'Edit', icon: 'pi pi-fw pi-pencil', command: () => this.onRowEdit(this.state.selectedRef)},
            {label: 'Delete', icon: 'pi pi-fw pi-trash', command: () => this.onRowDelete(this.state.selectedRef)}
        ];

        this.onRowAdd = this.onRowAdd.bind(this);
        this.onRowEdit = this.onRowEdit.bind(this);
        this.onRowDelete = this.onRowDelete.bind(this);
    }

    componentDidMount() {
        RefWebService.getRefs().then(response => {
            this.setState({refs: response.data});
        }, error => {
            this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    onRowAdd() {
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
        UserWebService.deleteRef(selectedRef).then(response => {
            let index = this.state.refs.indexOf(selectedRef);
            this.setState({refs: this.state.refs.filter((val, i) => i !== index)});
            this.props.growl.show({severity: 'success', summary: response.status, detail: response.data});
        }, error => {
            this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
        });
    }

    handleRefFromDialog = (ref, isNew) => {
        if (ref) {
            if (isNew) {
                let userId = 0;
                try {
                    userId = this.props.location.state.currentUser.id;
                } catch (e) {
                    userId = 0;
                    this.props.growl.show({severity: 'error', summary: 'Error', detail: 'ID of current user undefined'});
                }
                if (userId && userId !== 0) {
                    RefWebService.createRef(ref, userId).then(response => {
                        let refs = [...this.state.refs];
                        refs.push(response.data);
                        this.setState({
                            refs: refs,
                            isDialogDisplay: false
                        });
                        this.props.growl.show({severity: 'success', summary: 'Success', detail: 'Reference is created'});
                    }, error => {
                        this.setState({isDialogDisplay: false});
                        this.props.growl.show({severity: 'error', summary: error.status, detail: error.message});
                    });
                } else {
                    this.setState({isDialogDisplay: false});
                }
            } else {
                RefWebService.updateRef(ref).then(response => {
                    let refs = [...this.state.refs];
                    let index = refs.findIndex((item) => item.id === response.data.id);
                    refs[index] = Object.assign({}, response.data);
                    this.setState({
                        isDialogDisplay: false,
                        refs: refs});
                    this.props.growl.show({severity: 'success', summary: 'Success', detail: 'Reference is updated'});
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

    render() {
        let footer = <div className="p-clearfix" >
                         <Button label="Add" icon="pi pi-plus" onClick={this.onRowAdd}/>
                     </div>;

        return (
            <div>
                <ContextMenu model={this.menu} ref={el => this.cm = el} />

                <DataTable value={this.state.refs} editMode="row" footer={footer}
                           contextMenuSelection={(e) => this.state.selectedRef}
                           onContextMenuSelectionChange={e => this.setState({selectedRef: e.value})}
                           onContextMenu={e => this.cm.show(e.originalEvent)}>
                    <Column field="id" header="ReferenceId" style={{height: '3.5em', width: '8%'}}/>
                    <Column field="fullRef" header="FullRef" style={{height: '3.5em', width: '60%'}}/>
                    <Column field="reducedRef" header="ReducedRef" style={{height: '3.5em', width: '14%'}}/>
                    <Column field="requestsNumb" header="RequestsNumb" style={{height: '3.5em', width: '10%'}}/>
                    <Column field="userId" header="UserId" style={{height: '3.5em', width: '8%'}}/>
                </DataTable>
                <RefDialog isDialogDisplay={this.state.isDialogDisplay}
                            onChangeFinish={this.handleRefFromDialog}
                            reference={Object.assign({}, this.state.selectedRef)} />
            </div>
        )
    }
};