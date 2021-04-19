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
import {UserCardMini} from "../user/user-card/user-card-mini";
import IntersectionObserverService from "../../services/IntersectionObserverService";
import PostType from "../../services/PostType";

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
            subscribed: null,
            page: 0,
            pageSize: 5,
            totalPages: 0,
            totalUsers: 0,
            posts: [],
            users: []
        };
    }

    componentDidMount() {
        this.loadInfo();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.location !== prevProps.location) {
            this.componentDidMount();
        }
    }

    loadInfo() {
        RequestService.getAxios()
            .get(RequestService.URL + "/groups/" + this.props.match.params.address)
            .then(response => this.setState({info: response.data}, () => {
                this.loadPosts(() => IntersectionObserverService.create('post', this));
                this.loadUsers();
            }));
    }

    loadPosts(callback) {
        RequestService
            .getAxios()
            .get(RequestService.URL + `/groups/${this.state.info.id}/posts`, {
                params: {
                    page: this.state.page,
                    pageSize: this.state.pageSize
                }
            })
            .then(response => this.setState({
                posts: [...this.state.posts, ...response.data.content.map(post => { return {...post, type: PostType.GROUP}})],
                totalPages: response.data.totalPages
            }, () => {
                if (callback) callback();
            }));
    }

    loadUsers() {
        let url = '';

        if (AuthService.isAuthenticated()) {
            url = RequestService.URL + `/groups/${this.state.info.id}/users/${AuthService.getId()}`;
        } else {
            url = RequestService.URL + `/groups/${this.state.info.id}/users`;
        }

        RequestService
            .getAxios()
            .get(url, {
                params: {
                    page: 0,
                    size: 9
                }
            })
            .then(response => {
                if (AuthService.isAuthenticated()) {
                    const user = response.data.content.find(u => u.username === AuthService.getUsername());
                    if (user !== undefined) {
                        this.setState({
                            users: [user, ...response.data.content.filter(u => u.username !== AuthService.getUsername())],
                            totalUsers: response.data.totalElements,
                            subscribed: true
                        });
                        return;
                    }
                }
                this.setState({
                    users: response.data.content,
                    totalUsers: response.data.totalElements,
                    subscribed: false
                })
            });
    }

    handleCreatedGroupPost(post) {
        this.setState({posts: [{...post, type: PostType.GROUP}, ...this.state.posts]});
    }

    handleDeletePost(id) {
        this.setState({posts: this.state.posts.filter(post => post.id !== id)});
    }

    handleSubscribe() {
        if (!AuthService.isAuthenticated()) AuthService.login();
        RequestService
            .getAxios()
            .post(RequestService.URL + `/groups/${this.state.info.id}/users`)
            .then(() => this.setState({subscribed: true}, this.loadUsers.bind(this)));
    }

    handleUnsubscribe() {
        RequestService
            .getAxios()
            .delete(RequestService.URL + `/groups/${this.state.info.id}/users`)
            .then(() => this.setState({subscribed: false}, this.loadUsers.bind(this)));
    }

    render() {
        return (
            <div className={"group"}>
                <div className={"left_side"}>
                    <Card>
                        <CardHeader>
                            <h3>Actions</h3>
                        </CardHeader>
                        <CardBody>
                            {
                                this.state.info.creatorId === AuthService.getId()
                                    ? <Link to={`/group/${this.state.info.address}/update`}
                                            className={"form__button"}>Edit</Link>
                                    : ''
                            }
                            {
                                this.state.info.creatorId === AuthService.getId()
                                    ? <Link to={`/group/${this.state.info.address}/delete`}
                                            className={"form__button"}>Delete</Link>
                                    : ''
                            }
                            {
                                this.state.subscribed !== null
                                    ?   this.state.subscribed
                                        ? <FormButton
                                            handleClick={this.handleUnsubscribe.bind(this)}>Unsubscribe</FormButton>
                                        : <FormButton
                                            handleClick={this.handleSubscribe.bind(this)}>Subscribe</FormButton>
                                    :   ''
                            }
                        </CardBody>
                    </Card>
                    {
                        this.state.totalUsers > 0
                            ?   <Card>
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
                            :   ''
                    }
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
                    {
                        this.state.posts.map(post =>
                            <GroupPost key={post.id}
                                       data={post}
                                       handleDelete={this.handleDeletePost.bind(this)}
                            />)
                    }
                </div>
                <Switch>
                    <Route path={"/group/:address/update"} component={GroupUpdater}/>
                    <Route path={"/group/:address/delete"} component={GroupRemover}/>
                </Switch>
            </div>
        );
    }
}