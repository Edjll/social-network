import RequestService from "../../services/RequestService";
import * as React from "react";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {FormClose} from "../form/form-close";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";

export class CountryUpdater extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            loadQueue: 1,
            title: '',
            country: {
                id: null,
                title: null
            },
            errors: {
                title: null
            }
        }
    }

    componentDidMount() {
        document.body.style.overflow = 'hidden';
        RequestService.getAxios().get(RequestService.URL + "/country/" + this.props.match.params.id)
            .then(response => {
                this.setState({
                    country: response.data,
                    title: response.data.title,
                    loadQueue: this.state.loadQueue - 1
                });
            });
    }

    handleClose() {
        document.body.style.overflow = 'auto';
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().put(RequestService.URL + "/country/update", {
            id: this.state.country.id,
            title: this.state.title
        })
            .then(() => this.handleClose())
            .catch(error => this.setState({
                errors: {
                    title: error.response.data.errors.title
                }
            }));
    }

    handleChangeTitle(value) {
        this.setState({title: value, errors: {title: null}});
    }

    render() {
        return (
            <div className={"attention_center"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Updating country</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput value={this.state.country.id} title={"id"} disabled={true}/>
                        <FormInput value={this.state.country.title} handleChange={this.handleChangeTitle.bind(this)}
                                   title={"title"} error={this.state.errors.title}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Update</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}