import './post.css';
import {Link} from "react-router-dom";
import * as React from "react";
import {PostForm} from "./post-form";
import AuthService from "../../services/AuthService";
import {HiddenInfo} from "../hidden-info/hidden-info";
import {Card} from "../card/card";
import {CardHeader} from "../card/card-header";

export class Post extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            post: props.data,
            editable: false
        };
    }

    handleClickEdit() {
        this.setState({editable: !this.state.editable});
    }

    render() {
        return (
            <Card className={"post"}>
                <CardHeader>
                    <div className={"post__header__info"}>
                        <Link to={`/user/${this.state.post.address}`}
                              className={"post__header__info__link"}>{this.state.post.name}</Link>
                        <span
                            className={"post__header__info__date"}>{new Date(this.state.post.createdDate).toLocaleString()}</span>
                        {
                            this.state.post.modifiedDate
                                ? <HiddenInfo text={"(edit)"}
                                              hidden={new Date(this.state.post.modifiedDate).toLocaleString()}/>
                                : ""
                        }
                    </div>
                    {
                        AuthService.isAuthenticated()
                            ? <div className={"post__header__actions"}>
                                {
                                    this.state.post.creatorId === AuthService.getId()
                                        ? <div className={"post__header__actions__edit"}
                                               onClick={this.handleClickEdit.bind(this)}>
                                            {
                                                this.state.editable
                                                    ? '✖'
                                                    : '✎'
                                            }
                                        </div>
                                        : ''
                                }
                                {
                                    this.state.post.creatorId === AuthService.getId() || AuthService.hasRole([AuthService.Role.ADMIN])
                                        ? <div className={"post__header__actions__edit"}
                                               onClick={() => this.props.handleDelete(this)}>🗑
                                        </div>
                                        : ''
                                }
                            </div>
                            : ""
                    }
                </CardHeader>
                {
                    this.state.editable
                        ? <PostForm handleSubmit={(text) => this.props.handleSubmit(this, text)} text={this.state.post.text}/>
                        : <div className={"post__text"}>{this.state.post.text}</div>
                }
            </Card>
        );
    }
}