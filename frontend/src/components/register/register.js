import * as React from "react";
import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import {FormInput} from "../form/form-input";
import {FormSelect} from "../form/form-select";
import {CardBody} from "../card/card-body";
import {Redirect} from "react-router-dom";
import Validator from "../../services/Validator";
import validation from '../../services/validation.json';

export class Register extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            username: null,
            email: null,
            firstName: null,
            lastName: null,
            birthday: null,
            password: null,
            country: null,
            countries: [],
            city: null,
            cities: [],
            errors: {
                username: null,
                email: null,
                firstName: null,
                lastName: null,
                password: null
            },
            loadQueue: 1
        }
    }

    componentDidMount() {
        RequestService.getAxios().get(RequestService.URL + "/countries")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    })
                        .sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });
    }

    handleChangeUsername(value) {
        this.setState({username: value, errors: {...this.state.errors, username: null}});
    }

    handleChangeEmail(value) {
        this.setState({email: value, errors: {...this.state.errors, email: null}});
    }

    handleChangeFirstName(value) {
        this.setState({firstName: value, errors: {...this.state.errors, firstName: null}});
    }

    handleChangeLastName(value) {
        this.setState({lastName: value, errors: {...this.state.errors, lastName: null}});
    }

    handleChangeBirthday(value) {
        this.setState({birthday: value, errors: {...this.state.errors, birthday: null}});
    }

    handleChangePassword(value) {
        this.setState({password: value, errors: {...this.state.errors, password: null}});
    }

    handleChangeCountry(value) {
        if (value === null) this.setState({country: null, city: null})
        else {
            RequestService.getAxios().get(RequestService.URL + `/cities`, {params: {countryId: value.key}})
                .then(response => this.setState({
                        country: value.key,
                        cities: response.data.map(option => {
                            return {key: option.id, text: option.title}
                        })
                    })
                );
        }
    }

    handleChangeCity(value) {
        if (value === null) this.setState({city: null});
        else this.setState({city: value.key});
    }

    handleSubmit(e) {
        e.preventDefault();
        if (this.validate() === 0) {
            RequestService.getAxios().post(RequestService.URL + '/users', {
                username: this.state.username,
                email: this.state.email,
                firstName: this.state.firstName,
                lastName: this.state.lastName,
                birthday: this.state.birthday,
                credentials: [{value: this.state.password ? this.state.password : ''}],
                cityId: this.state.city
            })
                .then(() => AuthService.login(this.state.username, this.state.password))
                .catch(error => this.setState({
                    errors: {
                        ...this.state.errors,
                        ...error.response.data.errors,
                        password: error.response.data.errors ? error.response.data.errors['credentials[0].value'] : null
                    }
                }))
        }
    }

    validate() {
        let size = 0;
        let errors = {...this.state.errors};
        const usernameError = Validator.validate('Username', this.state.username, validation.user.username.params);
        if (usernameError) {
            errors = {...errors, username: usernameError};
            size++;
        }

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

        const passwordError = Validator.validate('Password', this.state.password, validation.user.password.params);
        if (passwordError) {
            errors = {...errors, password: passwordError};
            size++;
        }

        this.setState({errors: errors});
        return size;
    }

    render() {
        if (AuthService.isAuthenticated()) return (<Redirect to={'/'}/>);
        return (
            <div className={"login_form"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1 className={"register__title"}>Registration</h1>
                    </CardHeader>
                    <CardBody>
                        <FormInput value={this.state.username} title={"username*"} error={this.state.errors.username}
                                   handleChange={this.handleChangeUsername.bind(this)}/>
                        <FormInput value={this.state.email} title={"email*"} error={this.state.errors.email}
                                   handleChange={this.handleChangeEmail.bind(this)}/>
                        <FormInput value={this.state.firstName} title={"first name*"} error={this.state.errors.firstName}
                                   handleChange={this.handleChangeFirstName.bind(this)}/>
                        <FormInput value={this.state.lastName} title={"last name*"} error={this.state.errors.lastName}
                                   handleChange={this.handleChangeLastName.bind(this)}/>
                        {
                            this.state.loadQueue === 0
                                ? <FormSelect title={"country"} options={this.state.countries}
                                              handleChange={this.handleChangeCountry.bind(this)}/>
                                : ''
                        }
                        {
                            this.state.country
                                ? <FormSelect title={"city"} handleChange={this.handleChangeCity.bind(this)}
                                              options={this.state.cities}/>
                                : ''
                        }
                        <FormInput value={this.state.birthday} title={"birthday"} handleChange={this.handleChangeBirthday.bind(this)} type={"date"}/>
                        <FormInput value={this.state.password} title={"password*"} error={this.state.errors.password}
                                   handleChange={this.handleChangePassword.bind(this)} type={"password"}/>
                    </CardBody>
                    <CardFooter>
                        <FormButton>Register</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}