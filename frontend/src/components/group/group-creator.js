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
import Validator from "../../services/Validator";
import validation from "../../services/validation.json";

export class GroupCreator extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title: '',
            description: '',
            address: '',
            errors: null
        }
    }

    handleTitle(value) {
        this.setState({title: value, errors: {...this.state.errors, title: null}});
    }

    handleDescription(value) {
        this.setState({description: value, errors: {...this.state.errors, description: null}});
    }

    handleAddress(value) {
        this.setState({address: value, errors: {...this.state.errors, address: null}});
    }

    handleClose() {
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        if (this.validate() === 0) {
            RequestService.getAxios().post(RequestService.URL + '/groups', {
                title: this.state.title,
                description: this.state.description,
                address: this.state.address
            })
                .then(() => this.props.history.push(`/group/${this.state.address}`))
                .catch(error => this.setState({errors: error.response.data.errors}))
        }
    }

    validate() {
        let size = 0;
        let errors = {...this.state.errors};
        const titleError = Validator.validate('Title', this.state.title, validation.group.title.params);
        if (titleError) {
            errors = {...errors, title: titleError};
            size++;
        }

        const addressError = Validator.validate('Address', this.state.address, validation.group.address.params);
        if (addressError) {
            errors = {...errors, address: addressError};
            size++;
        }

        const descriptionError = Validator.validate('Description', this.state.description, validation.group.description.params);
        if (descriptionError) {
            errors = {...errors, description: descriptionError};
            size++;
        }

        this.setState({errors: errors});
        return size;
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
                        <FormInput error={this.state.errors ? this.state.errors.title : null} value={this.state.title} title={"Title"} handleChange={this.handleTitle.bind(this)}/>
                        <FormInput error={this.state.errors ? this.state.errors.address : null} value={this.state.address} title={"Address"} handleChange={this.handleAddress.bind(this)}/>
                        <FormTextarea error={this.state.errors ? this.state.errors.description : null} value={this.state.description} title={"Description"} handleChange={this.handleDescription.bind(this)}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Create</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}