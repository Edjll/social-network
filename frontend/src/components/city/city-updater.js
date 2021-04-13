import RequestService from "../../services/RequestService";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {FormClose} from "../form/form-close";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {FormSelect} from "../form/form-select";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import * as React from "react";

export class CityUpdater extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            loadQueue: 2,
            title: '',
            countryId: null,
            city: {
                id: null,
                title: null,
                country: {
                    id: null,
                    title: null
                }
            },
            errors: {
                title: null,
                country: null
            }
        }
    }

    componentDidMount() {
        document.body.style.overflow = 'hidden';
        RequestService.getAxios().get(RequestService.URL + "/country/all")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    }).sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });
        RequestService.getAxios().get(RequestService.URL + "/city/" + this.props.match.params.id)
            .then(response => {
                this.setState({
                    city: response.data,
                    countryId: response.data.country.id,
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
        RequestService.getAxios().put(RequestService.URL + "/city/update", {
            id: this.state.city.id,
            title: this.state.title,
            countryId: this.state.countryId
        })
            .then(() => this.handleClose())
            .catch(error => this.setState({
                errors: {
                    title: error.response.data.errors.title,
                    country: error.response.data.errors.countryId
                }
            }));
    }

    handleChangeTitle(value) {
        this.setState({title: value, errors: {...this.state.errors, title: null}});
    }

    handleSelectCountry(value) {
        if (value === null) this.setState({countryId: null, errors: {...this.state.errors, country: null}});
        else this.setState({countryId: value.key, errors: {...this.state.errors, country: null}});
    }

    render() {

        return (
            <div className={"attention_center"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Updating city</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput value={this.state.city.id} title={"id"} disabled={true}/>
                        <FormInput value={this.state.city.title} handleChange={this.handleChangeTitle.bind(this)}
                                   title={"title"}
                                   error={this.state.errors.title}/>
                        {
                            this.state.countries.length > 0 && this.state.city.country.id !== null
                                ? <FormSelect
                                    value={{key: this.state.city.country.id, text: this.state.city.country.title}}
                                    options={this.state.countries} title={"country"}
                                    handleChange={this.handleSelectCountry.bind(this)}
                                    error={this.state.errors.country}/>
                                : ''
                        }
                    </CardBody>
                    <CardFooter>
                        <FormButton>Update</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}