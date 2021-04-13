import './form-textarea.css';
import * as React from "react";

export class FormTextarea extends React.Component {

    handleChange(e) {
        if (this.props.handleChange) this.props.handleChange(e.target.value);
    }

    render() {
        return (
            <label className={"form__textarea"}>
                <span className={"form__textarea__title"}>{this.props.title}</span>
                <textarea className={"form__textarea__value"} onChange={this.handleChange.bind(this)} value={this.props.value} disabled={this.props.disabled}/>
            </label>
        );
    }
}