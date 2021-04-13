import './form-input.css';
import * as React from "react";

export class FormInput extends React.Component {

    constructor(props) {
        super(props);
    }

    handleChange(e) {
        if (this.props.handleChange) this.props.handleChange(e.target.value);
    }

    render() {
        return (
            <label className={`form__input ${this.props.className ? this.props.className : ''}`}>
                <span className={"form__input__title"}>{this.props.title}</span>
                <input
                    className={"form__input__value"}
                    type={this.props.type ? this.props.type : 'text'}
                    onChange={this.handleChange.bind(this)}
                    value={this.props.value ? this.props.value : ''}
                    disabled={this.props.disabled}
                />
            </label>
        );
    }
}