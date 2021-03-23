import React from "react";
import './Input.css'

export class Input extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            active: false,
            value: this.props.value ? this.props.value : ""
        };
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.value !== this.props.value) {
            this.setState({value: this.props.value});
        }
    }

    handleFocus() {
        this.setState({active: true});
    }

    handleBlur() {
        this.setState({active: false});
    }

    handleChange(value) {
        this.setState({value: value})
        if (this.props.handleChange) this.props.handleChange(value);
    }

    render() {

        return (
            <label className="input">
                <span className="input__label">{this.props.label}</span>
                <input
                    className="input__value"
                    value={this.state.value}
                    name={this.props.name}
                    type={this.props.type}
                    onChange={(e) => this.handleChange(e.target.value)}
                    onFocus={this.handleFocus.bind(this)}
                    onBlur={this.handleBlur.bind(this)}
                />
            </label>
        )
    }
}