import * as React from "react";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {Form} from "../form/form";
import RequestService from "../../services/RequestService";
import './search-page.css';
import {Card} from "../card/card";
import {CardHeader} from "../card/card-header";
import {FormSelect} from "../form/form-select";
import {FormButton} from "../form/form-button";
import IntersectionObserverService from "../../services/IntersectionObserverService";
import {LoadingAnimation} from "../loading-animation/loading-animation";

export class SearchPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            countries: [],
            cities: [],
            page: 0,
            size: 20,
            totalPages: 0,
            id: props.id,
            firstName: null,
            lastName: null,
            countryId: null,
            cityId: null,
            loadingUsers: false
        }

        this.loadUsers = props.loadUsers.bind(this);
    }

    componentDidMount() {
        this.loadUsers(() => IntersectionObserverService.create('.user_card:last-child', this, this.loadUsers));
        this.loadCountries();
    }

    loadCountries() {
        RequestService.getAxios()
            .get(RequestService.URL + "/countries")
            .then(response => this.setState({countries: response.data}));
    }

    loadCities() {
        RequestService.getAxios()
            .get(RequestService.URL + "/cities", {
                params: {
                    countryId: this.state.countryId
                }
            })
            .then(response => this.setState({cities: response.data}));
    }

    handleChangeFirstName(value) {
        this.setState({firstName: value});
    }

    handleChangeLastName(value) {
        this.setState({lastName: value});
    }

    handleChangeCountry(option) {
        if (option == null) {
            this.setState({
                countryId: null,
                cityId: null
            });
        } else {
            this.setState({countryId: option.key}, this.loadCities);
        }
    }

    handleChangeCity(option) {
        if (option == null) this.setState({cityId: null});
        else this.setState({cityId: option.key});
    }

    handleSearch() {
        this.setState({users: [], page: 0}, this.loadUsers(() => IntersectionObserverService.create('.user_card:last-child', this, this.loadUsers)));
    }

    render() {
        let contentInfo = '';
        if (this.state.loadingUsers) {
            contentInfo = <CardBody><LoadingAnimation/></CardBody>;
        } else if (this.state.users.length === 0) {
            contentInfo = <CardBody><p>Not found</p></CardBody>;
        }

        return (
            <div className={"search_page"}>
                <div className={"left_side"}>
                    <Form handleSubmit={this.handleSearch.bind(this)} className={'search_page__form'}>
                        <CardBody>
                            <FormInput
                                value={this.state.firstName}
                                title={"first name"}
                                handleChange={this.handleChangeFirstName.bind(this)}
                                pattern={"[a-zA-Zа-яА-Я]"}
                                clearable={true}
                            />
                            <FormInput
                                value={this.state.lastName}
                                title={"last name"}
                                handleChange={this.handleChangeLastName.bind(this)}
                                pattern={"[a-zA-Zа-яА-Я]"}
                                clearable={true}
                            />
                            {
                                this.state.countries.length > 0
                                    ? <FormSelect
                                        title={"country"}
                                        options={this.state.countries.map(country => {
                                            return {key: country.id, text: country.title}
                                        })}
                                        handleChange={this.handleChangeCountry.bind(this)}
                                    />
                                    : ''
                            }
                            {
                                this.state.countryId && this.state.cities.length > 0
                                    ? <FormSelect
                                        title={"city"}
                                        options={this.state.cities.map(city => {
                                            return {key: city.id, text: city.title}
                                        })}
                                        handleChange={this.handleChangeCity.bind(this)}
                                    />
                                    : ''
                            }
                            <FormButton>Search</FormButton>
                        </CardBody>
                    </Form>
                </div>
                <div className={"right_side"}>
                    <Card>
                        <CardHeader>
                            {this.props.children}
                        </CardHeader>
                        <CardBody>
                            { this.state.users.map(user => this.props.card(user.id, user)) }
                            { contentInfo }
                        </CardBody>
                    </Card>
                </div>
            </div>
        );
    }
}