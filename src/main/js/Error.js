import React, {Component} from 'react';
import { Button } from 'primereact/button';
import {Redirect} from 'react-router-dom';

export default class Error extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isButtonBackPressed: false,
            message: this.props?.location?.search ? this.props.location.search.slice(1) : 'Error!'
        };

        this.redirectPath = this.props?.location?.state?.from ? this.props?.location?.state?.from : '/';

        this.onButtonBackClick = this.onButtonBackClick.bind(this);
    }

    onButtonBackClick(event) {
        this.setState({isButtonBackPressed: true})
    }

    render() {
        return ( this.state.isButtonBackPressed ?
                <Redirect to={{pathname: this.redirectPath, state:{...this.props?.location?.state} }} /> :
                <div className='p-grid p-fluid center-container'>
                    <h2>{this.state?.message?.replace(/_/g, ' ')}</h2>
                    <div>
                        <Button label="Back" className="p-button-raised" onClick={this.onButtonBackClick} />
                    </div>
                </div>
        )
    }
}