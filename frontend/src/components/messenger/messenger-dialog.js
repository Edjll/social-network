import * as React from "react";
import RequestService from "../../services/RequestService";
import './messenger-dialog.css';
import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";
import Validator from "../../services/Validator";
import validation from "../../services/validation.json";
import {Toast} from "../toast/toast";
import IntersectionObserverService from "../../services/IntersectionObserverService";
import {MessengerMessage} from "./messenger-message";
import {FormTextarea} from "../form/form-textarea";

export class MessengerDialog extends React.Component {

    constructor(props) {
        super(props);
        this.defaultState = {
            id: null,
            messages: [],
            interlocutor: null,
            text: "",
            editing: false,
            createdDate: null,
            tmpText: "",
            errors: {
                text: null
            },
            page: 0,
            size: 10,
            totalPages: 0
        }
        this.state = {...this.defaultState};
        this.messagesRef = React.createRef();
    }

    componentDidMount() {
        if (this.props.info) {
            this.setState({interlocutor: this.props.info}, () => this.loadMessages(() => {
                IntersectionObserverService.create('.messenger_message:last-child', this, this.loadMessages);
            }));
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.info !== this.props.info) {
            this.setState({...this.defaultState}, () => this.componentDidMount());
        }
        if (prevProps.notification !== this.props.notification && this.props.notification !== null) {
            switch (this.props.notification.action) {
                case 'CREATED':
                    this.createMessage(this.props.notification.id);
                    break;
                case 'UPDATED':
                    this.updateMessage(this.props.notification.id);
                    break;
                case 'DELETED':
                    this.deleteMessage(this.props.notification.id);
                    break;
                default:
            }
        }
    }

    loadMessages(callback) {
        RequestService.getAxios()
            .get(RequestService.URL + `/users/${this.state.interlocutor.id}/messages`, {
                params: {
                    page: this.state.page,
                    size: this.state.size
                }
            })
            .then(response => {
                this.setState({
                    messages: [...this.state.messages, ...response.data],
                    lastSize: response.data.length
                }, () => {
                    if (callback) callback();
                });
            })
    }

    createMessage(id) {
        this.loadMessage(id)
            .then(response => {
                this.setState({messages: [response.data, ...this.state.messages]});
                this.messagesRef.current.scrollTop = this.messagesRef.current.scrollHeight;
            })
    }

    updateMessage(id) {
        this.loadMessage(id)
            .then(response => {
                const messageIndex = this.state.messages.findIndex(message => message.id === response.data.id);
                if (messageIndex > -1) {
                    const messages = [...this.state.messages];
                    messages[messageIndex] = response.data;
                    this.setState({messages: messages});
                }
            })
    }

    deleteMessage(id) {
        const messageIndex = this.state.messages.findIndex(message => message.id === id);
        if (messageIndex > -1) {
            const messages = [...this.state.messages];
            messages.splice(messageIndex, 1);
            this.setState({messages: messages});
        }
    }

    loadMessage(id) {
        return RequestService.getAxios()
            .get(RequestService.URL + `/users/messages/${id}`);
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
            });
        this.setState({text: ''});
    }

    editMessage() {
        RequestService
            .getAxios()
            .put(RequestService.URL + `/users/messages/${this.state.id}`, {
                text: this.state.text.replace(/\n\n/g, '\n')
            })
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
        RequestService
            .getAxios()
            .delete(RequestService.URL + `/users/messages/${value.id}`, {
                data: {
                    id: value.id,
                    senderId: AuthService.getId()
                }
            })
    }

    handleChange(value) {
        this.setState({text: value, errors: {text: null}})
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

    handleClose() {
        this.setState({errors: {text: null}});
    }

    render() {

        return (
            <div className={"dialog"}>
                <div className={"dialog__interlocutor-info"}>
                    {this.state.interlocutor
                        ? <Link to={`/user/${this.state.interlocutor.username}`}
                                className={"dialog__interlocutor-info__name"}>{this.state.interlocutor.firstName} {this.state.interlocutor.lastName}</Link>
                        : ""
                    }
                </div>
                <div className={"dialog__messages"} ref={this.messagesRef}>
                    {
                        this.state.messages.map(message => <MessengerMessage key={message.id} data={message}
                                                                             handleEdit={this.handleEdit.bind(this)}
                                                                             handleDelete={this.handleDelete.bind(this)}/>)
                    }
                    <div className={'dialog__messages__space'}/>
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
                    <FormTextarea className={'dialog__form__input'}
                                  value={this.state.text}
                                  handleChange={this.handleChange.bind(this)}
                                  handleSubmit={this.handleSubmit.bind(this)}
                                  placeholder={'Enter your message'}
                    />
                    <button className={"dialog__form__button"}>✉</button>
                </form>
                {
                    this.state.errors.text
                        ?
                        <Toast header={"Error"} body={this.state.errors.text} handleClose={this.handleClose.bind(this)}
                               time={2}/>
                        : ''
                }
            </div>
        );
    }
}