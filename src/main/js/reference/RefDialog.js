import React, {Component} from 'react';
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';
import {Dialog} from 'primereact/dialog';

export default class RefDialog extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reference: {fullRef: '', reducedRef: '', requestsNumb: 0, userId: 0},
            onChangeFinish: props.onChangeFinish,
            isNewRef: true
        };

        this.onSave = this.onSave.bind(this);
        this.onShow = this.onShow.bind(this);
        this.onCancelAdding = this.onCancelAdding.bind(this);
        this.updateProperty = this.updateProperty.bind(this);
    }

    componentDidMount() { }

    onSave() {
        this.props.onChangeFinish(this.state.reference, this.state.isNewRef);
    }

    onShow() {
        this.setState({
            reference: this.props.reference || {fullRef: '', reducedRef: '', requestsNumb: 0, userId: 0},
            isNewRef: !this.props.reference.hasOwnProperty('id')
        });
    }

    onCancelAdding() {
        this.props.onChangeFinish(null, this.state.isNewRef);
    }

    updateProperty(property, value) {
        let ref = this.state.reference;
        ref[property] = value;
        this.setState({reference: ref});
    }

    render() {
        let dialogFooter = <div className='ui-dialog-buttonpane p-clearfix'>
            <Button label='Save' icon='pi pi-check' onClick={this.onSave}/>
            <Button label='Cancel' icon='pi pi-times' onClick={this.onCancelAdding}/>
        </div>;

        return (
            <Dialog visible={this.props.isDialogDisplay}
                    style={{width: '300px'}} header={this.state.isNewRef ? 'New reference' : 'Modify reference'}
                    modal={true} footer={dialogFooter}
                    blockScroll={false}
                    closable={false}
                    onHide={() => {this.setState({reference: null})}}
                    onShow={this.onShow}>
                {
                    this.state.reference &&

                    <div className='p-grid p-fluid input-fields'>
                        <div className='p-col-4'><label htmlFor='reference'>Reference</label></div>
                        <div className='p-col-8'>
                            <InputText id='reference' onChange={(e) => this.updateProperty('fullRef', e.target.value)} value={this.state.reference.fullRef}/>
                        </div>

                    </div>
                }
            </Dialog>
        )
    }
}