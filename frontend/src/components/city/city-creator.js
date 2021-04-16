import RequestService from "../../services/RequestService";
import * as React from "react";
import {FormSelect} from "../form/form-select";
import {FormInput} from "../form/form-input";
import {FormButton} from "../form/form-button";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import {CardFooter} from "../card/card-footer";
import {FormClose} from "../form/form-close";

export class CityCreator extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            loadQueue: 1,
            title: '',
            countryId: null,
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
    }

    handleClose() {
        document.body.style.overflow = 'auto';
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().post(RequestService.ADMIN_URL + '/cities', {
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
        if (value === null) this.setState({countryId: null});
        else this.setState({countryId: value.key, errors: {...this.state.errors, country: null}});
    }

    render() {

        return (
            <div className={"attention_center"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Creating city</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput value={this.state.title}
                                   handleChange={this.handleChangeTitle.bind(this)}
                                   title={"title"}
                                   error={this.state.errors.title}/>
                        {
                            this.state.countries.length > 0
                                ?   <FormSelect options={this.state.countries} title={"country"}
                                                handleChange={this.handleSelectCountry.bind(this)}
                                                error={this.state.errors.country}/>
                                :   ''
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