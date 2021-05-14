import './table-page-size.css';
import * as React from "react";
import {FormInput} from "../form/form-input";

export const TablePageSize = (props) => {

    if (props.changed)
        return (
            <FormInput
                className={`table__page-size ${props.className ? props.className : ''}`}
                value={props.value}
                handleChange={props.handleChange}
                handleSubmit={props.handleButton}
                handleButton={props.handleButton}
                button={'↻'}
                default={'1'}
                pattern={'\\d'}
                removeDefault={false}
            />
        );
    else
        return (
            <FormInput
                className={`table__page-size ${props.className ? props.className : ''}`}
                value={props.value}
                handleChange={props.handleChange}
                default={'1'}
                pattern={'\\d'}
                removeDefault={false}
            />
        );
}