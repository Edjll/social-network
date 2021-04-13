import * as React from "react";
import {Form} from "../../form/form";
import {CardHeader} from "../../card/card-header";
import {CardBody} from "../../card/card-body";
import {FormTextarea} from "../../form/form-textarea";
import {CardFooter} from "../../card/card-footer";
import {FormButton} from "../../form/form-button";

export class GroupPostForm extends React.Component {

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
            <Form className={"group_post_form"} handleSubmit={this.handleSubmit.bind(this)}>
                <CardHeader>
                    {this.props.children}
                </CardHeader>
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