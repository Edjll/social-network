import * as React from "react";
import {Message} from "../message/message";
import RequestService from "../../services/RequestService";
import {Spinner} from "../spinner/spinner";
import './dialog.css';
import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";
import Validator from "../../services/Validator";
import validation from "../../services/validation.json";
import {Toast} from "../toast/toast";

export class Dialog extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: null,
            messages: [],
            interlocutor: null,
            loadQueue: 2,
            text: "",
            editing: false,
            createdDate: null,
            tmpText: "",
            errors: {
                text: null
            }
        }
        this.messagesRef = React.createRef();
    }

    componentDidMount() {
        RequestService.getAxios()
            .get(RequestService.URL + `/users/username/${this.props.match.params.username}`)
            .then(response => {
                this.setState({interlocutor: response.data, loadQueue: this.state.loadQueue - 1});
                return RequestService.getAxios()
                    .get(RequestService.URL + `/users/${this.state.interlocutor.id}/messages`)
            })
            .then(response => {
                this.setState({messages: response.data, loadQueue: this.state.loadQueue - 1});
                this.messagesRef.current.scrollTop = this.messagesRef.current.scrollHeight;
            })
    }

    handleSubmit() {
        if (this.validate() === 0) {
            if (this.state.editing === true) {
                this.editMessage();
                this.handleCancelEdit();
            } else {
                this.saveMessage();
            }
        }
    }

    saveMessage() {
        RequestService
            .getAxios()
            .post(RequestService.URL + `/users/${this.state.interlocutor.id}/messages`, {
                text: this.state.text.replace(/\n\n+/g, '\n')
            }).then(response => {
                this.setState({messages: [...this.state.messages, response.data]});
                this.messagesRef.current.scrollTop = this.messagesRef.current.scrollHeight;
            });
    }

    editMessage() {
        RequestService
            .getAxios()
            .put(RequestService.URL + `/users/messages/${this.state.id}`, {
                text: this.state.text.replace(/\n\n/g, '\n')
            }).then(response => {
                const newMessages = [...this.state.messages];
                const index = newMessages.findIndex(value => value.id === response.data.id);
                newMessages[index] = response.data;

                this.setState({messages: newMessages});
            });
    }

    validate() {
        let size = 0;
        let errors = {...this.state.errors};
        const textError = Validator.validate('Text', this.state.text.replace(/\n\n/g, '\n'), validation.message.text.params);
        if (textError) {
            errors = {...errors, text: textError};
            size++;
        }

        this.setState({errors: errors});
        return size;
    }

    handleDelete(value) {
        RequestService.getAxios().delete(RequestService.URL + `/users/messages/${value.id}`, {
            data: {
                id: value.id,
                senderId: AuthService.getId()
            }
        }).then(() => {
            const newMessages = [...this.state.messages];
            const index = newMessages.findIndex(message => message.id === value.id);
            newMessages.splice(index, 1);
            this.setState({messages: newMessages});
        });
    }

    handleChange(e) {
        this.setState({text: e.target.value, errors: {text: null}})
    }

    handleEdit(value) {
        this.setState({
            editing: true,
            text: value.text,
            id: value.id,
            createdDate: value.createdDate,
            tmpText: this.state.text,
            errors: {text: null}
        });
    }

    handleCancelEdit() {
        this.setState({
            text: this.state.tmpText,
            id: null,
            editing: false,
            createdDate: null,
            tmpText: "",
            errors: {text: null}
        });
    }

    handleKeyDown(e) {
        if (e.code === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            this.handleSubmit();
        }
    }

    handleClose() {
        this.setState({errors: {text: null}});
    }

    render() {

        return (
            <div className={"dialog"}>
                <div className={"dialog__interlocutor-info"}>
                    {this.state.loadQueue === 0
                        ? <Link to={`/user/${this.props.match.params.username}`}
                                className={"dialog__interlocutor-info__name"}>{this.state.interlocutor.firstName} {this.state.interlocutor.lastName}</Link>
                        : ""
                    }
                </div>
                <div className={"dialog__messages"} ref={this.messagesRef}>
                    {this.state.loadQueue === 0
                        ? this.state.messages.map(message => <Message key={message.id} data={message}
                                                                      handleEdit={this.handleEdit.bind(this)}
                                                                      handleDelete={this.handleDelete.bind(this)}/>)
                        : <Spinner/>
                    }
                </div>
                <form className={"dialog__form"} onSubmit={(e) => {
                    e.preventDefault();
                    this.handleSubmit()
                }}>
                    {
                        this.state.editing
                            ? <div className={"dialog__form__message-editing"}>
                                <span className={"dialog__form__message-editing__text"}>Editing message</span>
                                <div className={"dialog__form__message-editing__button"}
                                     onClick={this.handleCancelEdit.bind(this)}>✖
                                </div>
                            </div>
                            : ""
                    }
                    <textarea className={"dialog__form__input"} placeholder={"Enter your message"}
                              value={this.state.text} onChange={this.handleChange.bind(this)}
                              onKeyDown={this.handleKeyDown.bind(this)}/>
                    <button className={"dialog__form__button"}>✉</button>
                </form>
                {
                    this.state.errors.text
                        ?   <Toast header={"Error"} body={this.state.errors.text} handleClose={this.handleClose.bind(this)} time={2}/>
                        :   ''
                }
            </div>
        );
    }
}