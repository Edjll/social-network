import * as React from "react";
import {Card} from "../card/card";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import './group.css'
import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import {Link, Route, Switch} from "react-router-dom";
import {GroupUpdater} from "./group-updater";
import {GroupRemover} from "./group-remover";
import {GroupPostCreator} from "./group-post/group-post-creator";
import {GroupPost} from "./group-post/group-post";
import {FormButton} from "../form/form-button";
import {UserFriendCard} from "../user/user-card/user-friend-card";
import {UserCardMini} from "../user/user-card/user-card-mini";

export class Group extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            info: {
                title: '',
                description: '',
                id: null,
                address: '',
                creatorId: ''
            },
            subscribed: false,
            page: 0,
            pageSize: 5,
            totalPages: 0,
            totalUsers: 0,
            posts: [],
            users: []
        };
        this.postsRef = React.createRef();
    }

    componentDidMount() {
        this.loadInfo();
    }

    createIntersectionObserver() {
        if (this.state.totalPages > 1) {
            this.observer = new IntersectionObserver(
                entries => entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        this.observer.unobserve(entry.target);
                        if (this.state.page + 1 < this.state.totalPages) {
                            this.setState({page: this.state.page + 1}, () =>
                                this.loadPosts(() => {
                                    this.observer.observe(document.querySelector('.group_post:last-child'));
                                })
                            );
                        }
                    }
                }),
                {
                    threshold: 0.75
                }
            );
            this.observer.observe(document.querySelector('.group_post:last-child'));
        }
    }

    loadInfo() {
        RequestService.getAxios()
            .get(RequestService.URL + "/group/" + this.props.match.params.address)
            .then(response => this.setState({info: response.data}, () => {
                this.loadPosts(this.createIntersectionObserver.bind(this));
                this.loadUsers();
            }));
    }

    loadPosts(callback) {
        RequestService.getAxios()
            .get(RequestService.URL + "/group/post", {
                params: {
                    groupId: this.state.info.id,
                    page: this.state.page,
                    pageSize: this.state.pageSize
                }
            })
            .then(response => this.setState({
                posts: [...this.state.posts, ...response.data.content],
                totalPages: response.data.totalPages
            }, () => {
                if (callback) callback();
            }));
    }

    loadUsers() {
        RequestService.getAxios()
            .get(RequestService.URL + "/group/users", {
                params: {
                    groupId: this.state.info.id,
                    page: 0,
                    pageSize: 9
                }
            })
            .then(response => this.setState({
                users: response.data.content,
                totalUsers: response.data.totalElements
            }));
    }

    handleCreatedGroupPost(post) {
        this.setState({posts: [post, ...this.state.posts]});
    }

    handleUpdatedGroupPost(post) {
        const posts = [...this.state.posts];
        const index = posts.findIndex(value => value.id === post.id);
        posts[index] = post;
        this.setState({posts: posts});
    }

    handleDeletedGroupPost(id) {
        this.setState({posts: this.state.posts.filter(post => post.id !== id)});
    }

    handleSubscribe() {
        if (!AuthService.isAuthenticated()) AuthService.login();
        RequestService.getAxios().post(RequestService.URL + "/group/subscribe", {
            groupId: this.state.info.id
        })
            .then(() => this.setState({subscribed: true}));
    }

    handleUnsubscribe() {
        RequestService.getAxios().delete(RequestService.URL + "/group/unsubscribe", {
            params: {
                groupId: this.state.info.id
            }
        })
            .then(() => this.setState({subscribed: false}));
    }

    render() {

        return (
            <div className={"group"}>
                <div className={"left_side"}>
                    <Card>
                        <CardHeader>
                            <h3>Actions</h3>
                        </CardHeader>
                        {
                            this.state.info.creatorId === AuthService.getId()
                                ? <CardBody>
                                    <Link to={`/group/${this.state.info.address}/update`}
                                          className={"form__button"}>Edit</Link>
                                    <Link to={`/group/${this.state.info.address}/delete`}
                                          className={"form__button"}>Delete</Link>
                                </CardBody>
                                : <CardBody>
                                    {
                                        this.state.subscribed
                                            ? <FormButton
                                                handleClick={this.handleUnsubscribe.bind(this)}>Unsubscribe</FormButton>
                                            :
                                            <FormButton handleClick={this.handleSubscribe.bind(this)}>Subscribe</FormButton>
                                    }
                                </CardBody>
                        }
                    </Card>
                    <Card>
                        <CardHeader>
                            <Link to={{
                                pathname: `/group/subscribers`,
                                search: `?id=${this.state.info.id}`
                            }}>Subscribers</Link>
                            <span>{this.state.totalUsers}</span>
                        </CardHeader>
                        <CardBody className={"group__users"}>
                            {
                                this.state.users.map(user => <UserCardMini key={user.username} info={user}/>)
                            }
                        </CardBody>
                    </Card>
                </div>
                <div className={"right_side"}>
                    <Card className={"group__info"}>
                        <CardHeader className={"group__info__title"}>
                            <h1>{this.state.info.title}</h1>
                        </CardHeader>
                        <CardBody className={"group__info__description"}>
                            <pre>{this.state.info.description}</pre>
                        </CardBody>
                    </Card>
                    {
                        AuthService.getId() === this.state.info.creatorId
                            ? <GroupPostCreator groupId={this.state.info.id}
                                                handleSubmit={this.handleCreatedGroupPost.bind(this)}/>
                            : ''
                    }
                    <div className={"group__posts"} ref={this.postsRef}>
                        {
                            this.state.posts.length > 0
                                ? <div className={"group__posts"}>
                                    {
                                        this.state.posts.map(post =>
                                            <GroupPost key={post.id}
                                                       id={post.id}
                                                       title={this.state.info.title}
                                                       text={post.text}
                                                       createdDate={post.createdDate}
                                                       modifiedDate={post.modifiedDate}
                                                       creatorId={post.creatorId}
                                                       handleDeleted={this.handleDeletedGroupPost.bind(this)}
                                                       handleUpdated={this.handleUpdatedGroupPost.bind(this)}
                                            />)
                                    }
                                </div>
                                : ''
                        }
                    </div>
                </div>
                <Switch>
                    <Route path={"/group/:address/update"} component={GroupUpdater}/>
                    <Route path={"/group/:address/delete"} component={GroupRemover}/>
                </Switch>
            </div>
        );
    }
}