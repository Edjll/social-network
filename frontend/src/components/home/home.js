import * as React from "react";
import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import {Post} from "../post/post";
import './home.css';
import IntersectionObserverService from "../../services/IntersectionObserverService";

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
        this.loadPosts(() => IntersectionObserverService.create('post', this));
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
                totalPages: response.data.totalPages,
                posts: [...this.state.posts, ...response.data.content]
            }, () => {
                if (callback) callback()
            }))
    }

    render() {
        return (
            <div className={"home"}>
                {
                    this.state.posts.map(post => <Post key={post.id} data={post}/>)
                }
            </div>
        );
    }
}