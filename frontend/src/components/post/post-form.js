import * as React from "react";
import './post-form.css';
import {Form} from "../form/form";
import {CardBody} from "../card/card-body";
import {FormTextarea} from "../form/form-textarea";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";

export class PostForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            text: this.props.text ? this.props.text : ""
        };
    }

    handleChangeText(value) {
        this.setState({text: value});
    }

    handleSubmit(e) {
        e.preventDefault();
        this.props.handleSubmit(this.state.text);
        this.setState({text: ''})
    }

    render() {
        return (
            <Form className={"post_form"} handleSubmit={this.handleSubmit.bind(this)}>
                {this.props.children}
                <CardBody>
                    <FormTextarea handleChange={this.handleChangeText.bind(this)} value={this.state.text}/>
                </CardBody>
                <CardFooter>
                    <FormButton>Save</FormButton>
                </CardFooter>
            </Form>
        );
    }
}