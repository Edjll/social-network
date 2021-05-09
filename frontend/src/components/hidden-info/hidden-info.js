import './hidden-info.css';
import * as React from "react";

export const HiddenInfo = (props) => {
    
    return (
        <div className={`hidden_info ${props.className ? props.className : ''}`}>
            <div className={"hidden_info__text"}>{props.text}</div>
            <div className={"hidden_info__hidden"}>
                <span className={"hidden_info__hidden__text"}>{props.hidden}</span>
                <div className={"hidden_info__hidden__icon"}/>
            </div>
        </div>
    );
}