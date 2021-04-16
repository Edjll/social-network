import RequestService from "../../../services/RequestService";
import {CardHeader} from "../../card/card-header";
import * as React from "react";
import {PostForm} from "../../post/post-form";

export const UserPostCreator = (props) => {

    const handleSubmit = (text) => {
        RequestService
            .getAxios()
            .post(RequestService.URL + `/users/posts`, {
                text: text
            }).then(response => {
            if (props.handleSubmit) props.handleSubmit(response.data);
        });
    }

    return (
        <div className={`card ${props.className ? props.className : ''}`}>
            <PostForm handleSubmit={handleSubmit}>
                <CardHeader>
                    <h1>Creating post</h1>
                </CardHeader>
            </PostForm>
        </div>
    );
}