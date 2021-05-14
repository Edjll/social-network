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

export class GroupUpdater extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            title: '',
            description: '',
            address: this.props.match.params.address,
            errors: null
        }
    }

    loadInfo() {
        RequestService.getAxios()
            .get(RequestService.URL + "/groups/" + this.props.match.params.address)
            .then(response => this.setState({...response.data}));
    }

    componentDidMount() {
        this.loadInfo();
        document.body.style.overflow = 'hidden';
    }

    componentWillUnmount() {
        document.body.style.overflow = 'auto';
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

    handleSubmit() {
        if (this.validate() === 0) {
            RequestService.getAxios().put(RequestService.URL + `/groups/${this.state.id}`, {
                title: this.state.title,
                description: this.state.description,
                address: this.state.address
            })
                .then(() => {
                    if (this.state.address === this.props.match.params.address) {
                        this.props.history.push(`/group/${this.state.address}`, { update: true });
                    } else {
                        this.props.history.push(`/group/${this.state.address}`);
                    }
                })
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
                        <h1>Updating group</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput
                            clearable={true}
                            error={this.state.errors ? this.state.errors.title : null}
                            title={"Title"}
                            handleChange={this.handleTitle.bind(this)}
                            value={this.state.title}
                            pattern={'[a-zA-Zа-яА-Я0-9_\\- ]'}
                        />
                        <FormInput
                            clearable={true}
                            error={this.state.errors ? this.state.errors.address : null}
                            title={"Address"}
                            handleChange={this.handleAddress.bind(this)}
                            value={this.state.address}
                            pattern={'[a-z0-9_]'}
                        />
                        <FormTextarea error={this.state.errors ? this.state.errors.description : null} title={"Description"} handleChange={this.handleDescription.bind(this)} value={this.state.description}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Update</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}