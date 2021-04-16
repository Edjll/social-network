import * as React from "react";
import RequestService from "../../../services/RequestService";
import {CardHeader} from "../../card/card-header";
import {PostForm} from "../../post/post-form";

export const GroupPostCreator = (props) => {

    const handleSubmit = (text) => {
        RequestService
            .getAxios()
            .post(RequestService.URL + `/groups/${props.groupId}/posts`, {
                text: text
            }).then(response => {
                if (props.handleSubmit) props.handleSubmit(response.data);
            });
    }

    return (
        <div className={`${props.className ? props.className : ''}`}>
            <PostForm handleSubmit={handleSubmit}>
                <CardHeader>
                    <h1>Creating group post</h1>
                </CardHeader>
            </PostForm>
        </div>
    );
}