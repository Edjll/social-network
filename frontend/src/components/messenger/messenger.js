import * as React from "react";
import RequestService from "../../services/RequestService";
import {MessengerDialog} from "./messenger-dialog";
import {MessengerInterlocutorCard} from "./messenger-interlocutor-card";
import './messenger.css';
import AuthService from "../../services/AuthService";

export class Messenger extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            interlocutors: [],
            notification: null,
            currentInterlocutor: null
        }
    }

    componentDidMount() {
        this.loadInterlocutors();
        RequestService.stompConnect(
            () => RequestService.stompSubscribe(`/users/${AuthService.getId()}/queue/messages`, this.handleNotification.bind(this))
        );
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.location.search !== this.props.location.search) {
            const id = new URLSearchParams(this.props.location.search).get("id");
            const currentInterlocutor = this.state.interlocutors.find(inter => inter.id === id);
            currentInterlocutor.newMessages = 0;
            this.setState({interlocutors: this.state.interlocutors, currentInterlocutor: currentInterlocutor});
        }
    }

    loadInterlocutors() {
        RequestService.getAxios()
            .get(RequestService.URL + `/users/interlocutors`, {
                params: {
                    page: 0,
                    size: 10
                }
            })
            .then(response => {
                const id = new URLSearchParams(this.props.location.search).get("id");
                this.setState({
                    interlocutors: response.data.content.map(data => { return {...data, newMessages: 0} })},
                    () => {
                        const currentInterlocutor = this.state.interlocutors.find(inter => inter.id === id);
                        if (currentInterlocutor) {
                            this.setState({currentInterlocutor: currentInterlocutor});
                        } else {
                            this.loadInterlocutor(id)
                                .then(response => {
                                    this.setState({
                                        currentInterlocutor: {
                                            id: response.data.id,
                                            username: response.data.username,
                                            firstName: response.data.firstName,
                                            lastName: response.data.lastName,
                                            newMessages: 0
                                        }
                                    });
                                });
                        }
                    }
                );
            })
    }

    loadInterlocutor(id) {
        return RequestService.getAxios()
            .get(RequestService.URL + `/users/${id}`);
    }

    handleNotification(message) {
        const notification = JSON.parse(message.body);
        const interlocutors = [...this.state.interlocutors];
        const searchId = notification.senderId === AuthService.getId() ? notification.recipientId : notification.senderId;
        const interlocutorIndex = interlocutors.findIndex(interlocutor => interlocutor.id === searchId);
        const authIsNotSender = notification.senderId !== AuthService.getId() && notification.senderId !== this.state.currentInterlocutor.id;
        if (notification.action === 'CREATED') {
            if (interlocutorIndex > -1) {
                const interlocutor = interlocutors.splice(interlocutorIndex, 1);
                if (authIsNotSender) interlocutor[0].newMessages++;
                this.setState({interlocutors: [...interlocutor, ...interlocutors]});
            } else {
                this.loadInterlocutor(searchId)
                    .then(response => {
                        this.setState({interlocutors: [
                                {
                                    id: response.data.id,
                                    username: response.data.username,
                                    firstName: response.data.firstName,
                                    lastName: response.data.lastName,
                                    newMessages: authIsNotSender ? 1 : 0
                                },
                                ...this.state.interlocutors
                            ]}
                        );
                    })
            }
        } else if (notification.action === 'DELETED' && interlocutorIndex > -1) {
            interlocutors.splice(interlocutorIndex, 1);
            this.setState({interlocutors: interlocutors});
        }
        if (!authIsNotSender) {
            this.setState({notification: notification});
        }
    }

    render() {
        return (
            <div className={"messenger"}>
                <div className={"messenger__interlocutors"}>
                    <div className={"messenger__interlocutors__header"}>
                        <span>Interlocutors</span>
                    </div>
                    {
                        this.state.interlocutors.map(interlocutor => <MessengerInterlocutorCard key={interlocutor.id}
                                                                                                active={this.state.currentInterlocutor && interlocutor.id === this.state.currentInterlocutor.id}
                                                                                                info={interlocutor}/>)
                    }
                </div>
                {
                    this.state.currentInterlocutor
                        ? <MessengerDialog info={this.state.currentInterlocutor}
                                           notification={this.state.notification}/>
                        : ''
                }
            </div>
        );
    }
}