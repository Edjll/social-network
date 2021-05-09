import * as React from "react";
import RequestService from "../../services/RequestService";
import {MessengerDialog} from "./messenger-dialog";
import {MessengerInterlocutorCard} from "./messenger-interlocutor-card";
import './messenger.css';
import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";
import IntersectionObserverService from "../../services/IntersectionObserverService";

export class Messenger extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            interlocutors: [],
            notification: null,
            currentInterlocutor: null,
            page: 0,
            size: 15,
            totalPages: 0
        }
    }

    componentDidMount() {
        this.loadInterlocutors(() => IntersectionObserverService.create('.messenger_interlocutor_card:last-child', this, this.loadMessages));
        RequestService.StompInstance.connect(
            () => RequestService.StompInstance.subscribe(`/users/${AuthService.getId()}/queue/messages`, this.handleNotification.bind(this))
        );
    }

    componentWillUnmount() {
        RequestService.StompInstance.disconnect();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.location.search !== this.props.location.search) {
            const id = new URLSearchParams(this.props.location.search).get("id");
            if (id !== null) {
                const currentInterlocutor = this.state.interlocutors.find(inter => inter.id === id);
                if (currentInterlocutor.newMessages !== 0) {
                    RequestService
                        .getAxios()
                        .put(RequestService.URL + `/users/${currentInterlocutor.id}/messages`)
                }
                currentInterlocutor.newMessages = 0;
                this.setState({interlocutors: this.state.interlocutors, currentInterlocutor: currentInterlocutor});
            } else {
                this.setState({currentInterlocutor: null})
            }
        }
    }

    loadInterlocutors(callback) {
        RequestService.getAxios()
            .get(RequestService.URL + `/users/interlocutors`, {
                params: {
                    page: this.state.page,
                    size: this.state.size
                }
            })
            .then(response => {
                const id = new URLSearchParams(this.props.location.search).get("id");
                this.setState(
                    {
                        lastSize: response.data.length,
                        interlocutors: response.data
                    },
                    () => {
                        if (new URLSearchParams(this.props.location.search).get("id")) {
                            const currentInterlocutor = this.state.interlocutors.find(inter => inter.id === id);
                            if (currentInterlocutor) {
                                this.setState({currentInterlocutor: currentInterlocutor});
                            } else {
                                this.loadInterlocutorInfo(id)
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
                        if (callback) callback();
                    }
                );
            })
    }

    loadInterlocutor(id) {
        return RequestService.getAxios()
            .get(RequestService.URL + `/users/interlocutors/${id}`);
    }

    loadInterlocutorInfo(id) {
        return RequestService.getAxios()
            .get(RequestService.URL + `/users/${id}`);
    }

    handleNotification(message) {
        const notification = JSON.parse(message.body);
        const interlocutors = [...this.state.interlocutors];
        const searchId = notification.senderId === AuthService.getId() ? notification.recipientId : notification.senderId;
        const interlocutorIndex = interlocutors.findIndex(interlocutor => interlocutor.id === searchId);
        const authIsNotSender = notification.senderId !== AuthService.getId() && (this.state.currentInterlocutor === null || notification.senderId !== this.state.currentInterlocutor.id);
        if (notification.action === 'CREATED') {
            if (interlocutorIndex > -1) {
                const interlocutor = interlocutors.splice(interlocutorIndex, 1);
                if (authIsNotSender) interlocutor[0].newMessages++;
                else if (notification.senderId !== AuthService.getId()) RequestService.getAxios().put(RequestService.URL + `/users/messages/${notification.id}/viewed`);
                this.setState({interlocutors: [...interlocutor, ...interlocutors]});
            } else {
                this.loadInterlocutorInfo(searchId)
                    .then(response => {
                        if (AuthService.getId() !== notification.senderId && this.state.currentInterlocutor && response.data.id === this.state.currentInterlocutor.id) {
                            RequestService.getAxios().put(RequestService.URL + `/users/messages/${notification.id}/viewed`);
                        }
                        this.setState({interlocutors: [
                                {
                                    id: response.data.id,
                                    username: response.data.username,
                                    firstName: response.data.firstName,
                                    lastName: response.data.lastName,
                                    newMessages: 0
                                },
                                ...this.state.interlocutors
                            ]}
                        );
                    })
            }
        } else if (notification.action === 'DELETED') {
            RequestService
                .getAxios()
                .get(RequestService.URL + `/users/interlocutors/${searchId}`)
                .then(response => {
                    if (interlocutorIndex > -1) {
                        if (response.data.position - 1 !== interlocutorIndex) {
                            interlocutors.splice(interlocutorIndex, 1);
                            if (response.data.position - 1 <= interlocutors.length) {
                                interlocutors.splice(response.data.position - 1, 0, response.data);
                            }
                        } else {
                            interlocutors[interlocutorIndex].newMessages = response.data.newMessages;
                        }
                        this.setState({interlocutors: interlocutors});
                    }
                });
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
                        : <div className={"messenger__unselected_interlocutor"}>
                            <div>Select interlocutor in the column or <Link to={"/users"}>find</Link> him</div>
                        </div>
                }
            </div>
        );
    }
}