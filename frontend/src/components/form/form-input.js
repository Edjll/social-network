import './form-input.css';
import * as React from "react";
import {HiddenInfo} from "../hidden-info/hidden-info";

export const FormInput = (props) => {

    const handleChange = (e) => {
        if (props.handleChange) props.handleChange(e.target.value);
    }

    return (
        <label className={`form__input ${props.className ? props.className : ''}`}>
            <span className={"form__input__title"}>{props.title}</span>
            <div className={'form__input__wrapper'}>
                <input
                    className={"form__input__value"}
                    type={props.type ? props.type : 'text'}
                    onChange={(e) => handleChange(e)}
                    value={props.value ? props.value : ''}
                    disabled={props.disabled}
                />
                {
                    props.error
                        ?   <HiddenInfo className={'form__input__error'} text={'❌'} hidden={props.error}/>
                        :   ''
                }
            </div>
        </label>
    );
}