import './Post.css';
import {Link} from "react-router-dom";
import * as React from "react";
import {PostForm} from "../postForm/PostForm";
import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import {HiddenInfo} from "../hiddenInfo/HiddenInfo";

export class Post extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            editable: false,
            post: props.data
        };
    }

    handleSubmit(text) {
        RequestService.getAxios().put(RequestService.URL + "/post/edit", {
            id: this.state.post.id,
            user: {
                id: AuthService.getId()
            },
            text: text,
            createdDate: this.state.post.createdDate
        }).then(response => {
            this.setState({post: response.data});
        });
        this.setState({editable: false});
    }

    handleClickEdit() {
        this.setState({editable: !this.state.editable});
    }

    handleClickDelete() {
        RequestService.getAxios().delete(RequestService.URL + "/post/delete", {
            data: {
                id: this.state.post.id,
                user: {
                    id: AuthService.getId()
                }
            }
        }).then(() => {
            if (this.props.handleDelete) this.props.handleDelete(this.state.post.id);
        });
    }

    render() {
        return (
            <div className={"post"}>
                <div className={"post__header"}>
                    <div className={"post__header__info"}>
                        <Link to={`/user/${this.state.post.user.username}`}
                              className={"post__header__info__link"}>{this.state.post.user.firstName} {this.state.post.user.lastName}</Link>
                        <span
                            className={"post__header__info__date"}>{new Date(this.state.post.createdDate).toLocaleString()}</span>
                        {
                            this.state.post.modifiedDate
                                ?   <HiddenInfo text={"(edit)"} hidden={new Date(this.state.post.modifiedDate).toLocaleString()}/>
                                :   ""
                        }
                    </div>
                    {
                        this.state.post.user.id === AuthService.getId()
                            ?   <div className={"post__header__actions"}>
                                    <div className={"post__header__actions__edit"} onClick={this.handleClickEdit.bind(this)}>
                                        {
                                            this.state.editable
                                                ? '✖'
                                                : '✎'
                                        }
                                    </div>
                                    <div className={"post__header__actions__edit"}
                                         onClick={this.handleClickDelete.bind(this)}>🗑
                                    </div>
                                </div>
                            :   ""
                    }
                </div>
                {
                    this.state.editable
                        ? <PostForm handleSubmit={this.handleSubmit.bind(this)} text={this.state.post.text}/>
                        : <div className={"post__text"}>{this.state.post.text}</div>
                }
            </div>
        );
    }
}