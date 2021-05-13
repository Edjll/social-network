import * as React from "react";
import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import './home.css';
import IntersectionObserverService from "../../services/IntersectionObserverService";
import {GroupPost} from "../group/group-post/group-post";
import {UserPost} from "../user/user-post/user-post";

export class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            page: 0,
            size: 10,
            totalPages: 0,
            posts: []
        }
    }

    componentDidMount() {
        this.loadPosts(() => IntersectionObserverService.create('.post:last-child', this));
    }

    loadPosts(callback) {
        RequestService
            .getAxios()
            .get(RequestService.URL + `/users/${AuthService.getId()}/feed`, {
                params: {
                    page: this.state.page,
                    size: this.state.size
                }
            })
            .then(response => this.setState({
                lastSize: response.data.length,
                posts: [...this.state.posts, ...response.data]
            }, () => {
                if (callback) callback()
            }))
    }

    handleDelete(key) {
        this.setState({posts: this.state.posts.filter(post => (post.id + post.type) !== key)});
    }

    render() {
        return (
            <div className={"home"}>
                {
                    this.state.posts.map(post => {
                        return post.type === 'GROUP'
                            ? <GroupPost key={post.id + post.type} id={post.id + post.type} data={post} handleDelete={this.handleDelete.bind(this)}/>
                            : <UserPost key={post.id + post.type} id={post.id + post.type} data={post} handleDelete={this.handleDelete.bind(this)}/>
                    })
                }
            </div>
        );
    }
}