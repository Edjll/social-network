import * as React from "react";
import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import './post-creator.css';
import {PostForm} from "./post-form";

export const PostCreator = (props) => {

    const handleSubmit = (text) => {
        RequestService.getAxios().post(RequestService.URL + "/post/save", {
            userId: AuthService.getId(),
            text: text
        }).then(response => {
            if (props.handleSubmit) props.handleSubmit(response.data);
        });
    }

    return (
        <div className={`post_creator ${props.className ? props.className : ''}`}>
            <PostForm handleSubmit={handleSubmit.bind(this)}/>
        </div>
    );
}