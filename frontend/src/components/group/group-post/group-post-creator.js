import * as React from "react";
import RequestService from "../../../services/RequestService";
import {GroupPostForm} from "./group-post-form";

export const GroupPostCreator = (props) => {

    const handleSubmit = (text) => {
        RequestService.getAxios().post(RequestService.URL + "/group/post/save", {
            text: text,
            groupId: props.groupId
        }).then(response => {
            if (props.handleSubmit) props.handleSubmit(response.data);
        });
    }

    return (
        <div className={`group_post_creator ${props.className ? props.className : ''}`}>
            <GroupPostForm handleSubmit={handleSubmit.bind(this)}>
                <h1>Creating group post</h1>
            </GroupPostForm>
        </div>
    );
}