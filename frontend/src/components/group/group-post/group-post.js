import {Card} from "../../card/card";
import {CardHeader} from "../../card/card-header";
import {CardBody} from "../../card/card-body";
import './group-post.css';
import {HiddenInfo} from "../../hiddenInfo/HiddenInfo";
import * as React from "react";
import {FormTextarea} from "../../form/form-textarea";
import {CardFooter} from "../../card/card-footer";
import {FormButton} from "../../form/form-button";
import RequestService from "../../../services/RequestService";
import AuthService from "../../../services/AuthService";

export class GroupPost extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            editable: false,
            text: props.text
        }
    }

    handleChangeText(value) {
        this.setState({text: value});
    }

    handleChangeEditable() {
        this.setState({editable: !this.state.editable});
    }

    handleDelete() {
        RequestService.getAxios().delete(RequestService.URL + "/group/post/delete", {
            params: {
                id: this.props.id
            }
        })
            .then(() => {
                if (this.props.handleDeleted) this.props.handleDeleted(this.props.id);
            })
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().put(RequestService.URL + "/group/post/update", {
            id: this.props.id,
            text: this.state.text
        })
            .then((response) => {
                if (this.props.handleUpdated) this.props.handleUpdated(response.data);
                this.setState({editable: false});
            })
    }

    render() {
        return (
            <Card className={"group_post"}>
                <CardHeader>
                    <div className={"group_post__header__info"}>
                        <h3>{this.props.title}</h3>
                        <span>{new Date(this.props.createdDate).toLocaleString()}</span>
                        {
                            this.props.modifiedDate
                                ? <HiddenInfo text={"(edit)"} hidden={new Date(this.props.modifiedDate).toLocaleString()}/>
                                : ""
                        }
                    </div>
                    <div className={"group_post__header__actions"}>
                        {
                            AuthService.getId() === this.props.creatorId
                                ?   <div className={"group_post__header__actions__edit"} onClick={this.handleChangeEditable.bind(this)}>
                                        {
                                            this.state.editable
                                                ? '✖'
                                                : '✎'
                                        }
                                    </div>
                                :   ''
                        }
                        {
                            this.props.creatorId === AuthService.getId() || AuthService.hasRole([AuthService.Role.ADMIN])
                                ?   <div className={"post__header__actions__edit"} onClick={this.handleDelete.bind(this)}>🗑</div>
                                :   ''
                        }
                    </div>
                </CardHeader>
                {
                    this.state.editable
                        ? <form onSubmit={this.handleSubmit.bind(this)}>
                            <CardBody>
                                <FormTextarea value={this.state.text} handleChange={this.handleChangeText.bind(this)}/>
                            </CardBody>
                            <CardFooter>
                                <FormButton>Save</FormButton>
                            </CardFooter>
                        </form>
                        : <CardBody>
                            <pre>{this.props.text}</pre>
                        </CardBody>
                }
            </Card>
        );
    }
}