import * as React from "react";
import '../CountryForm.css';
import {Input} from "../../form/input/Input";
import RequestService from "../../../services/RequestService";
import {Button} from "../../form/button/Button";

export class DeleteCountry extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loadQueue: 1,
            country: {
                id: null,
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
        RequestService.getAxios().delete(RequestService.URL + "/country/delete", {
            params: {
                id: this.state.country.id
            }
        }).then(() => this.handleClose());
    }

    render() {

        return (
            <div className={"country-form"}>
                {
                    this.state.loadQueue === 0
                        ? <form className={"country-form__form"} onSubmit={this.handleSubmit.bind(this)}>
                            <h1 className={"country-form__title"}>Delete country</h1>
                            <Input value={this.state.country.id} label={"id"} disabled={true}/>
                            <Input value={this.state.country.title} label={"title"} disabled={true}/>
                            <Button text={"Delete"} className={"country-form__button"}/>
                        </form>
                        : ''
                }
                <div className={"country-form__button-close"} onClick={this.handleClose.bind(this)}>×</div>
            </div>
        );
    }
}