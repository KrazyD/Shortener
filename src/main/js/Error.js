import React, {Component} from 'react';
import { Button } from 'primereact/button';
import {Redirect} from 'react-router-dom';

export default class Error extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isButtonBackPressed: false
        };

        this.onButtonBackClick = this.onButtonBackClick.bind(this);
    }

    onButtonBackClick(event) {
        this.setState({isButtonBackPressed: true})
    }

    render() {
        return ( this.state.isButtonBackPressed ?
                <Redirect to={{pathname: this.props.location.state.from,  state:{...this.props.location.state} }} /> :
                <div className='p-grid p-fluid'>
                    <h2>{this.props.message}</h2>
                    <Button label="Back" className="p-button-raised" onClick={this.onButtonBackClick} />
                </div>
        )
    }
}