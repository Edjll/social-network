import './form-textarea.css';
import * as React from "react";
import {HiddenInfo} from "../hidden-info/hidden-info";

export const FormTextarea = (props) => {

    const handleChange = (e) => {
        if (props.handleChange) props.handleChange(e.target.value);
    }

    const handleKeyDown = (e) => {
        if (e.code === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            if (props.handleSubmit) props.handleSubmit();
        }
    }
    
    return (
        <label className={`form__textarea ` + props.className}>
            {
                props.title
                    ?   <span className={"form__textarea__title"}>{props.title}</span>
                    :   ''
            }
            <div className={'form__textarea__wrapper'}>
                <textarea className={"form__textarea__value"}
                          onChange={handleChange.bind(this)}
                          value={props.value}
                          disabled={props.disabled}
                          onKeyDown={handleKeyDown.bind(this)}
                          placeholder={props.placeholder ? props.placeholder : ''}
                />
                {
                    props.error
                        ?   <HiddenInfo className={'form__textarea__error'} text={'❌'} hidden={props.error}/>
                        :   ''
                }
            </div>
        </label>
    );
}