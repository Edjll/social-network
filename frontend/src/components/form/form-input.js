import './form-input.css';
import * as React from "react";

export const FormInput = (props) => {

    const handleChange = (e) => {
        if (props.handleChange) props.handleChange(e.target.value);
    }

    return (
        <label className={`form__input ${props.className ? props.className : ''}`}>
            <span className={"form__input__title"}>{props.title}</span>
            <input
                className={"form__input__value"}
                type={props.type ? props.type : 'text'}
                onChange={(e) => handleChange(e)}
                value={props.value ? props.value : ''}
                disabled={props.disabled}
            />
        </label>
    );
}