import RequestService from "../../services/RequestService";
import * as React from "react";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {FormClose} from "../form/form-close";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import Validator from "../../services/Validator";
import validation from "../../services/validation.json";

export class CountryCreator extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            title: '',
            errors: {
                title: null
            }
        }
    }

    handleClose() {
        document.body.style.overflow = 'auto';
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        if (this.validate() === 0) {
            RequestService
                .getAxios()
                .post(RequestService.ADMIN_URL + "/countries", {
                    title: this.state.title
                })
                .then(() => this.handleClose())
                .catch(error => this.setState({
                    errors: {
                        title: error.response.data.errors.title
                    }
                }));
        }
    }

    validate() {
        let size = 0;
        let errors = {...this.state.errors};
        const titleError = Validator.validate('Title', this.state.title, validation.country.title.params);
        if (titleError) {
            errors = {...errors, title: titleError};
            size++;
        }

        console.log(errors)

        this.setState({errors: errors}, () => console.log(this.state.errors));
        return size;
    }

    handleChangeTitle(value) {
        this.setState({title: value, errors: {title: null}});
    }

    render() {

        return (
            <div className={"attention_center"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Creating country</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput value={this.state.title}
                                   handleChange={this.handleChangeTitle.bind(this)}
                                   title={"title"}
                                   error={this.state.errors.title}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Save</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}