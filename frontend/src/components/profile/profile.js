import AuthService from "../../services/AuthService";
import './profile.css'
import React from "react";
import RequestService from "../../services/RequestService";
import {Link} from "react-router-dom";
import {Spinner} from "../spinner/spinner";
import {Card} from "../card/card";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import {UserCardMini} from "../user/user-card/user-card-mini";
import {UserGroupCard} from "../user/user-card/user-group-card";
import {UserPost} from "../user/user-post/user-post";
import {UserPostCreator} from "../user/user-post/user-post-creator";
import IntersectionObserverService from "../../services/IntersectionObserverService";
import PostType from "../../services/PostType";

export class Profile extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            user: null,
            posts: [],
            page: 0,
            size: 10,
            totalPages: 0,
            friends: [],
            groups: [],
            subscribers: [],
            loadQueue: 1,
            error: null,
            totalGroups: 0,
            totalFriends: 0,
            totalSubscribers: 0
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.location !== prevProps.location) {
            this.setState({loadQueue: this.state.loadQueue + 1});
            this.componentDidMount();
        }
    }

    componentDidMount() {
        this.loadUserInfo();
    }

    loadUserInfo() {
        RequestService.getAxios()
            .get(RequestService.URL + `/users/username/${this.props.match.params.username}`)
            .then(response => {
                if (response.data) {
                    this.setState({
                        user: response.data,
                        loadQueue: this.state.loadQueue - 1,
                        posts: []
                    }, () => {
                        this.loadPosts(() => IntersectionObserverService.create('post', this));
                        this.loadFriends();
                        this.loadGroups();
                        this.loadSubscribers();
                    })
                } else {
                    this.setState({loadQueue: this.state.loadQueue - 1, error: "User not found"})
                }
            })
            .catch(error => {
                const errors = [];
                for (let k in error.response.data.errors) {
                    errors.push(error.response.data.errors[k]);
                }
                this.setState({error: errors, loadQueue: this.state.loadQueue - 1})
            })
    }

    loadPosts(callback) {
        RequestService
            .getAxios()
            .get(RequestService.URL + `/users/${this.state.user.id}/posts`, {
                params: {
                    page: this.state.page,
                    size: this.state.size
                }
            })
            .then(response => this.setState({
                totalPages: response.data.totalPages,
                posts: [...this.state.posts, ...response.data.content.map(post => { return { ...post, type: PostType.USER } })]
            }, () => { if (callback) callback() }));
    }

    loadFriends() {
        RequestService.getAxios()
            .get(RequestService.URL + `/users/${this.state.user.id}/friends`, {
                params: {
                    page: 0,
                    size: 9
                }
            })
            .then(response => this.setState({
                friends: response.data.content,
                totalFriends: response.data.totalElements
            }));
    }

    loadSubscribers() {
        RequestService.getAxios()
            .get(RequestService.URL + `/users/${this.state.user.id}/subscribers`, {
                params: {
                    page: 0,
                    size: 9
                }
            })
            .then(response => this.setState({
                subscribers: response.data.content,
                totalSubscribers: response.data.totalElements
            }));
    }

    loadGroups() {
        RequestService.getAxios().get(RequestService.URL + `/users/${this.state.user.id}/groups`, {
            params: {
                page: 0,
                pageSize: 9
            }
        })
            .then(response => this.setState({
                groups: response.data.content,
                totalGroups: response.data.totalElements
            }));
    }

    handleDeletePost(id) {
        this.setState({posts: this.state.posts.filter(post => post.id !== id)});
    }

    handleCreatePost(post) {
        this.setState({posts: [{...post, type: PostType.USER}, ...this.state.posts]});
    }

    render() {
        if (this.state.error !== null) return (<div>{this.state.error}</div>);
        if (this.state.loadQueue > 0) return (<Spinner/>);
        return (
            <div className="profile">
                <div className={"left_side"}>
                    <Card className={"profile__actions"}>
                        <CardHeader>
                            <h3>Actions</h3>
                        </CardHeader>
                        <CardBody>
                            {
                                AuthService.isAuthenticated() && AuthService.getUsername() === this.state.user.username
                                    ? <Link to={`/profile/edit`} className="form__button profile__actions__item">Edit
                                        info</Link>
                                    : ''
                            }
                            <Link to={`/user/${this.state.user.username}/message`}
                                  className="form__button profile__actions__item">Send message</Link>
                        </CardBody>
                    </Card>
                    {
                        this.state.friends.length > 0
                            ? <Card className={"profile__friends"}>
                                <CardHeader>
                                    <Link
                                        to={{pathname: `/user/friends`, search: `?id=${this.state.user.id}`}}>Friends</Link>
                                    <span>{this.state.totalFriends}</span>
                                </CardHeader>
                                <CardBody className={"profile__friends__items"}>
                                    {
                                        this.state.friends.map(friend => <UserCardMini key={friend.id} info={friend}/>)
                                    }
                                </CardBody>
                            </Card>
                            : ''
                    }
                    {
                        this.state.groups.length > 0
                            ? <Card className={"profile__friends"}>
                                <CardHeader>
                                    <Link to={{pathname: `/user/groups`, search: `?id=${this.state.user.id}`}}>Groups</Link>
                                    <span>{this.state.totalGroups}</span>
                                </CardHeader>
                                <CardBody className={"profile__friends__items"}>
                                    {this.state.groups.map(group => <UserGroupCard key={group.address} title={group.title}
                                                                                   address={group.address}/>)}
                                </CardBody>
                            </Card>
                            : ''
                    }
                    {
                        this.state.subscribers.length > 0
                            ? <Card className={"profile__friends"}>
                                <CardHeader>
                                    <Link to={{
                                        pathname: `/user/subscribers`,
                                        search: `?id=${this.state.user.id}`
                                    }}>Subscribers</Link>
                                    <span>{this.state.totalSubscribers}</span>
                                </CardHeader>
                                <CardBody className={"profile__friends__items"}>
                                    {
                                        this.state.subscribers.map(subscriber => <UserCardMini key={subscriber.id}
                                                                                               info={subscriber}/>)
                                    }
                                </CardBody>
                            </Card>
                            : ''
                    }
                </div>
                <div className={"right_side"}>
                    <Card className="profile__info">
                        <CardHeader>
                            <h1>{`${this.state.user.firstName} ${this.state.user.lastName}`}</h1>
                        </CardHeader>
                        <CardBody>
                            <div className="profile__info__block">
                                <span>Birthday:</span>
                                <span>{this.state.user.birthday ? new Date(this.state.user.birthday).toLocaleDateString() : "not specified"}</span>
                            </div>
                            <div className="profile__info__block">
                                <span>City:</span>
                                <span>{this.state.user.city ? this.state.user.city : "not specified"}</span>
                            </div>
                        </CardBody>
                    </Card>
                    {
                        AuthService.isAuthenticated() && AuthService.getUsername() === this.state.user.username
                            ? <UserPostCreator handleSubmit={this.handleCreatePost.bind(this)}
                                           className={"profile__create-post"}/>
                            : ''
                    }
                    {
                        this.state.posts.map(post => <UserPost key={post.id} data={post}
                                                           handleDelete={this.handleDeletePost.bind(this)}/>)
                    }
                </div>
            </div>
        );
    }
}