import './form-input.css';
import * as React from "react";
import {HiddenInfo} from "../hidden-info/hidden-info";

export const FormInput = (props) => {

    const handleChange = (e) => {
        let value = e.target.value;
        if (value.slice(0, -1) === props.default && e.nativeEvent.data !== '') value = e.nativeEvent.data;
        else if (value === '' && props.default) value = props.default;
        if (e.nativeEvent.data !== null && props.pattern && !new RegExp(props.pattern).test(e.nativeEvent.data)) return;
        if (props.handleChange) props.handleChange(value);
    }

    const handleKeyDown = (e) => {
        if (e.code === 'Enter') {
            if (props.handleSubmit) props.handleSubmit();
        }
    }

    const clear = () => {
        if (props.handleChange) props.handleChange(props.default ? props.default : '');
    }

    return (
        <div className={`form__input ${props.className ? props.className : ''}`}>
            {
                props.title
                    ?   <span className={"form__input__title"}>{props.title}</span>
                    :   ''
            }
            <div className={'form__input__wrapper'}>
                {
                    props.handleButton
                        ?   <div className={'form__input__button'} onClick={props.handleButton}>{props.button ? props.button : ''}</div>
                        :   ''
                }
                <input
                    className={"form__input__value"}
                    type={props.type ? props.type : 'text'}
                    onChange={(e) => handleChange(e)}
                    value={props.value ? props.value : ''}
                    disabled={props.disabled}
                    onBlur={props.handleBlur}
                    onKeyDown={handleKeyDown.bind(this)}
                />
                {
                    props.clearable && props.value
                        ?   <div className={'form__input__clear'} onClick={clear.bind(this)}>✕</div>
                        :   ''
                }
                {
                    props.error
                        ?   <HiddenInfo className={'form__input__error'} text={'⛔'} hidden={props.error}/>
                        :   ''
                }
            </div>
        </div>
    );
}