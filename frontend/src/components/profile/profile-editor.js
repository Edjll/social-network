import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import * as React from "react";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {FormClose} from "../form/form-close";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import {FormSelect} from "../form/form-select";
import './profile-editor.css';
import Validator from "../../services/Validator";
import validation from "../../services/validation.json";

export default class ProfileEditor extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedCountry: false,
            countries: [],
            cities: [],
            city: {
                id: null,
                title: null
            },
            country: {
                id: null,
                title: null
            },
            birthday: null,
            loadQueue: 2,
            errors: {
                email: null,
                firstName: null,
                lastName: null
            }
        }
    }

    handleChangeCountry(value) {
        if (value != null) {
            RequestService.getAxios().get(RequestService.URL + `/cities`, {params: {countryId: value.key}})
                .then(response =>
                    this.setState({
                        selectedCountry: true, cities: response.data.map(option => {
                            return {key: option.id, text: option.title}
                        })
                    })
                );
            this.setState({
                country: {id: value.key, title: value.text}
            })
        } else if (this.state.selectedCountry === true) {
            this.setState({
                selectedCountry: false,
                city: {id: null, title: null},
                country: {id: null, title: null}
            });
        }
    }

    handleChangeCity(value) {
        if (value !== null) {
            this.setState({
                city: {
                    id: value.key,
                    title: value.text,
                }
            });
        } else {
            this.setState({
                city: {
                    id: null,
                    title: null,
                }
            });
        }
    }

    componentDidMount() {
        RequestService.getAxios().get(RequestService.URL + "/countries")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    }).sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });

        RequestService.getAxios()
            .get(RequestService.URL + `/users/${AuthService.getId()}/details`)
            .then(response => {
                this.setState({
                    ...response.data,
                    country: response.data.city ? response.data.city.country : null
                });
                if (this.state.country.id !== null) {
                    this.handleChangeCountry({key: this.state.country.id, text: this.state.country.title});
                }
                this.setState({loadQueue: this.state.loadQueue - 1});
            });
    }

    handleSubmit() {
        if (this.validate() === 0) {
            RequestService
                .getAxios()
                .put(RequestService.URL + `/users`, {
                    email: this.state.email,
                    firstName: this.state.firstName,
                    lastName: this.state.lastName,
                    cityId: this.state.city.id,
                    birthday: this.state.birthday
                })
                .then(() => {
                    AuthService.forceUpdateToken(() => window.location = `/user/${AuthService.getUsername()}`);
                })
                .catch(error => this.setState({
                    errors: {
                        ...this.state.errors,
                        ...error.response.data.errors,
                        password: error.response.data.errors ? error.response.data.errors['credentials[0].value'] : null
                    }
                }));
        }
    }

    validate() {
        let size = 0;
        let errors = {...this.state.errors};

        const emailError = Validator.validate('Email', this.state.email, validation.user.email.params);
        if (emailError) {
            errors = {...errors, email: emailError};
            size++;
        }

        const firstNameError = Validator.validate('First name', this.state.firstName, validation.user.firstName.params);
        if (firstNameError) {
            errors = {...errors, firstName: firstNameError};
            size++;
        }

        const lastNameError = Validator.validate('Last name', this.state.lastName, validation.user.lastName.params);
        if (lastNameError) {
            errors = {...errors, lastName: lastNameError};
            size++;
        }

        this.setState({errors: errors});
        return size;
    }

    handleChangeBirthday(value) {
        this.setState({birthday: value})
    }

    handleChangeEmail(value) {
        this.setState({email: value, errors: {...this.state.errors, email: null}})
    }

    handleChangeFirstName(value) {
        this.setState({firstName: value, errors: {...this.state.errors, firstName: null}})
    }

    handleChangeLastName(value) {
        this.setState({lastName: value, errors: {...this.state.errors, lastName: null}})
    }

    handleClose() {
        this.props.history.goBack();
    }

    render() {

        return (
            <div className={"profile_editor"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Editing profile</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput clearable={true}
                                   value={this.state.email}
                                   title={"email"}
                                   handleChange={this.handleChangeEmail.bind(this)}
                                   error={this.state.errors.email}/>
                        <FormInput clearable={true}
                                   value={this.state.firstName}
                                   title={"first name"}
                                   handleChange={this.handleChangeFirstName.bind(this)}
                                   error={this.state.errors.firstName}/>
                        <FormInput clearable={true}
                                   value={this.state.lastName}
                                   title={"last name"}
                                   handleChange={this.handleChangeLastName.bind(this)}
                                   error={this.state.errors.lastName}/>
                        <FormInput value={this.state.birthday} type={"date"} title={"birthday"}
                                   handleChange={this.handleChangeBirthday.bind(this)}/>
                        {
                            this.state.loadQueue === 0
                                ? <FormSelect
                                    value={{key: this.state.country.id, text: this.state.country.title}}
                                    options={this.state.countries}
                                    title={"country"}
                                    handleChange={this.handleChangeCountry.bind(this)}/>
                                : ''
                        }
                        {
                            this.state.country.id && this.state.cities.length > 0
                                ? <FormSelect
                                    value={{key: this.state.city.id, text: this.state.city.title}}
                                    title={"city"}
                                    options={this.state.cities}
                                    handleChange={this.handleChangeCity.bind(this)}
                                />
                                : ''
                        }
                    </CardBody>
                    <CardFooter>
                        <FormButton>Save</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}