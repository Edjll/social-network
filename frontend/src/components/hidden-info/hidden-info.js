import './hidden-info.css';
import * as React from "react";
import {Link} from "react-router-dom";

export const HiddenInfo = (props) => {

    let text = <div className={"hidden_info__text"}>{props.text}</div>;

    if (props.link) {
        text = <Link className={"hidden_info__text hidden_info__link"} to={props.link}>{props.text}</Link>;
    } else if (props.button) {
        text = <div className={"hidden_info__text"} onClick={props.button}>{props.text}</div>;
    }
    
    return (
        <div className={`hidden_info ${props.className ? props.className : ''}`}>
            { text }
            <div className={"hidden_info__hidden"}>
                <span className={"hidden_info__hidden__text"}>{props.hidden}</span>
                <div className={"hidden_info__hidden__icon"}/>
            </div>
        </div>
    );
}