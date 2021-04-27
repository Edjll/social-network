import RequestService from "../../services/RequestService";
import * as React from "react";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {FormClose} from "../form/form-close";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";

export class CityRemover extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loadQueue: 1,
            city: {
                id: null,
                title: null,
                country: {
                    id: null,
                    title: null
                }
            }
        }
    }

    componentDidMount() {
        document.body.style.overflow = 'hidden';
        RequestService.getAxios().get(RequestService.URL + "/city/" + this.props.match.params.id)
            .then(response => {
                this.setState({
                    city: response.data,
                    loadQueue: this.state.loadQueue - 1
                });
            });
    }

    componentWillUnmount() {
        document.body.style.overflow = 'auto';
    }

    handleClose() {
        this.props.history.push("/admin/cities");
    }

    handleSubmit() {
        RequestService
            .getAxios()
            .delete(RequestService.ADMIN_URL + `/cities/${this.state.city.id}`)
            .then(() => this.props.history.push("/admin/cities", { update: true }));
    }

    render() {
        return (
            <div className={"attention_center"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Deleting city</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput value={this.state.city.id} title={"id"} disabled={true}/>
                        <FormInput value={this.state.city.title} title={"title"} disabled={true}/>
                        <FormInput value={this.state.city.country.title} title={"country"} disabled={true}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Delete</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}