import './hidden-info.css';
import * as React from "react";
import {Link} from "react-router-dom";

export const HiddenInfo = (props) => {
    
    return (
        <div className={`hidden_info ${props.className ? props.className : ''}`}>
            {
                props.link
                    ?   <Link className={"hidden_info__text hidden_info__link"} to={props.link}>{props.text}</Link>
                    :   <div className={"hidden_info__text"}>{props.text}</div>
            }
            <div className={"hidden_info__hidden"}>
                <span className={"hidden_info__hidden__text"}>{props.hidden}</span>
                <div className={"hidden_info__hidden__icon"}/>
            </div>
        </div>
    );
}