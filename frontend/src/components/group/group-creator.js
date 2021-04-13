import * as React from "react";
import './group-creator.css';
import {Form} from "../form/form";
import {FormInput} from "../form/form-input";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import {FormTextarea} from "../form/form-textarea";
import {FormClose} from "../form/form-close";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import RequestService from "../../services/RequestService";

export class GroupCreator extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title: '',
            description: '',
            address: ''
        }
    }

    handleTitle(value) {
        this.setState({title: value});
    }

    handleDescription(value) {
        this.setState({description: value});
    }

    handleAddress(value) {
        this.setState({address: value});
    }

    handleClose() {
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().post(RequestService.URL + '/group/save', {
            title: this.state.title,
            description: this.state.description,
            address: this.state.address
        })
            .then(() => this.props.history.push(`/group/${this.state.address}`))
    }

    render() {
        return (
            <div className={"group_creator"}>
                <Form className={"group_creator__form"} handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Creating group</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput title={"Title"} handleChange={this.handleTitle.bind(this)}/>
                        <FormInput title={"Address"} handleChange={this.handleAddress.bind(this)}/>
                        <FormTextarea title={"Description"} handleChange={this.handleDescription.bind(this)}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Create</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}