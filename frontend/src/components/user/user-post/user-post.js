import RequestService from "../../../services/RequestService";
import {Post} from "../../post/post";
import PostType from "../../../services/PostType";

export const UserPost = (props) => {

    const handleSubmit = (context, text) => {
        RequestService
            .getAxios()
            .put(RequestService.URL + `/users/posts/${context.state.post.id}`, {
                text: text
            }).then(response => {
            context.setState({post: {...response.data, type: PostType.USER}});
        });
        context.setState({editable: false});
    }

    const handleDelete = (context) => {
        RequestService
            .getAxios()
            .delete(RequestService.URL + `/users/posts/${context.state.post.id}`)
            .then(() => {
                if (props.handleDelete) props.handleDelete(props.id);
            });
    }

    return (
        <Post data={props.data} handleSubmit={handleSubmit} handleDelete={handleDelete}/>
    );
}