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
        <PostForm handleSubmit={handleSubmit}>
            <CardHeader>
                <h1>Creating post</h1>
                <div><button className={"post_form__save"}>💾</button></div>
            </CardHeader>
        </PostForm>
    );
}