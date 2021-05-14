import RequestService from "../../services/RequestService";
import * as React from "react";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {FormClose} from "../form/form-close";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import {Error} from "../error/error";
import {Toast} from "../toast/toast";

export class CountryRemover extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loadQueue: 1,
            country: {
                id: null,
                title: null
            },
            error: false
        }
    }

    componentDidMount() {
        document.body.style.overflow = 'hidden';
        RequestService
            .getAxios()
            .get(RequestService.URL + "/countries/" + this.props.match.params.id)
            .then(response => {
                this.setState({
                    country: response.data,
                    loadQueue: this.state.loadQueue - 1
                });
            });
    }

    componentWillUnmount() {
        document.body.style.overflow = 'auto';
    }

    handleClose() {
        this.props.history.push("/admin/countries");
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService
            .getAxios()
            .delete(RequestService.ADMIN_URL + `/countries/${this.state.country.id}`)
            .then(() => this.props.history.push("/admin/countries", { update: true }))
            .catch(() => this.setState({error: true}));
    }

    handleCloseToast() {
        this.setState({error: false});
    }

    render() {

        return (
            <div className={"attention_center"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Deleting country</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput value={this.state.country.id} title={"id"} disabled={true}/>
                        <FormInput value={this.state.country.title} title={"title"} disabled={true}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Delete</FormButton>
                    </CardFooter>
                </Form>
                {
                    this.state.error
                        ?   <Toast header={"Error"} body={"Country cannot be deleted"} handleClose={this.handleCloseToast.bind(this)} time={100}/>
                        :   ''
                }
            </div>
        );
    }
}