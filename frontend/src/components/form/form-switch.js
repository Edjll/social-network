import * as React from "react";
import './form-switch.css';

export class FormSwitch extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            enabled: this.props.enabled
        }
    }

    handleClick() {
        this.setState({enabled: !this.state.enabled})
    }

    render() {
        return (
            <div className={`form__switch ${this.state.enabled ? 'form__switch-enabled': ''}`} onClick={() => this.props.handleClick(!this.state.enabled, this.handleClick.bind(this))}>
                <div className={"form__switch__point"}/>
            </div>
        );
    }
}