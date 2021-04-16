import * as React from "react";
import './group-creator.css';
import {Form} from "../form/form";
import {FormInput} from "../form/form-input";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import {FormClose} from "../form/form-close";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import RequestService from "../../services/RequestService";

export class GroupRemover extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: '',
            title: '',
            description: '',
            address: this.props.match.params.address
        }
    }

    loadInfo() {
        RequestService.getAxios()
            .get(RequestService.URL + "/groups/" + this.props.match.params.address)
            .then(response => this.setState({...response.data}));
    }

    componentDidMount() {
        this.loadInfo();
    }

    handleTitle(value) {
        this.setState({title: value});
    }

    handleAddress(value) {
        this.setState({address: value});
    }

    handleClose() {
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService
            .getAxios()
            .delete(RequestService.URL + `/groups/${this.state.id}`)
            .then(() => this.props.history.push(`/`))
    }

    render() {
        return (
            <div className={"group_creator"}>
                <Form className={"group_creator__form"} handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Deleting group</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput title={"Title"} handleChange={this.handleTitle.bind(this)} value={this.state.title}
                                   disabled={true}/>
                        <FormInput title={"Address"} handleChange={this.handleAddress.bind(this)}
                                   value={this.state.address} disabled={true}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Delete</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}