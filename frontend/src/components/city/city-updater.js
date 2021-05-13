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
import Validator from "../../services/Validator";
import validation from "../../services/validation.json";

export class CityUpdater extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: null,
            countries: [],
            loadQueue: 2,
            title: '',
            countryId: null,
            country: {
                id: null,
                title: null
            },
            errors: {
                title: null,
                country: null
            }
        }
    }

    componentDidMount() {
        document.body.style.overflow = 'hidden';
        RequestService.getAxios().get(RequestService.URL + "/countries")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    }).sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });
        RequestService.getAxios().get(RequestService.URL + "/cities/" + this.props.match.params.id)
            .then(response => {
                this.setState({
                    id: response.data.id,
                    countryId: response.data.country.id,
                    country: response.data.country,
                    title: response.data.title,
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
        if (this.validate() === 0) {
            RequestService
                .getAxios()
                .put(RequestService.ADMIN_URL + `/cities/${this.state.id}`, {
                    title: this.state.title,
                    countryId: this.state.countryId
                })
                .then(() => this.props.history.push("/admin/cities", { update: true }))
                .catch(error => this.setState({
                    errors: {
                        title: error.response.data.errors.title,
                        country: error.response.data.errors.countryId
                    }
                }))
        }
    }

    validate() {
        let size = 0;
        let errors = { };
        const titleError = Validator.validate('Title', this.state.title, validation.city.title.params);
        if (titleError) {
            errors = {...errors, title: titleError};
            size++;
        }

        const countryError = Validator.validate('Country', this.state.countryId, validation.city.country.params);
        if (countryError) {
            errors = {...errors, country: countryError};
            size++;
        }

        if (size > 0) {
            this.setState({errors: {...this.state.errors, ...errors}});
        }
        return size;
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
                        <FormInput value={this.state.id} title={"id"} disabled={true}/>
                        <FormInput
                            clearable={true}
                            value={this.state.title}
                            handleChange={this.handleChangeTitle.bind(this)}
                            title={"title"}
                            error={this.state.errors.title}
                            pattern={"[a-zA-Z ]"}
                        />
                        {
                            this.state.countries.length > 0 && this.state.country.id
                                ? <FormSelect
                                    value={{key: this.state.country.id, text: this.state.country.title}}
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