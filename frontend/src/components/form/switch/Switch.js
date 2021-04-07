import './Switch.css';
import * as React from "react";

export class Switch extends React.Component {

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
            <div className={`switch ${this.state.enabled ? 'switch-enabled': ''}`} onClick={() => this.props.handleClick(!this.state.enabled, this.handleClick.bind(this))}>
                <div className={"switch__point"}/>
            </div>
        );
    }
}