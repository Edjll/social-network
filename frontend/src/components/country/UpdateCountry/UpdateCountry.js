import * as React from "react";
import '../CountryForm.css';
import {Input} from "../../form/input/Input";
import RequestService from "../../../services/RequestService";
import {Button} from "../../form/button/Button";

export class UpdateCountry extends React.Component {

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
            <div className={"country-form"}>
                {
                    this.state.loadQueue === 0
                        ? <form className={"country-form__form"} onSubmit={this.handleSubmit.bind(this)}>
                            <h1 className={"country-form__title"}>Update country</h1>
                            <Input value={this.state.country.id} label={"id"} disabled={true}/>
                            <Input value={this.state.country.title} handleChange={this.handleChangeTitle.bind(this)}
                                   label={"title"} error={this.state.errors.title}/>
                            <Button text={"Update"} className={"country-form__button"}/>
                        </form>
                        : ''
                }
                <div className={"country-form__button-close"} onClick={this.handleClose.bind(this)}>×</div>
            </div>
        );
    }
}