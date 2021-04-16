import * as React from "react";
import {Card} from "../card/card";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import RequestService from "../../services/RequestService";
import {GroupCard} from "./group-card";
import {Link} from "react-router-dom";
import './group-search.css';
import AuthService from "../../services/AuthService";

export class GroupSearch extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            groups: [],
            page: 0,
            pageSize: 10,
            totalPages: 0
        }
    }

    componentDidMount() {
        this.loadGroups(this.createIntersectionObserver.bind(this));
    }

    loadGroups(callback) {
        RequestService.getAxios().get(RequestService.URL + "/groups", {
            params: {
                page: this.state.page,
                pageSize: this.state.pageSize
            }
        })
            .then(response => this.setState({
                groups: [...this.state.groups, ...response.data.content],
                totalPages: response.data.totalPages
            }, () => {
                if (callback) callback();
            }))
    }

    createIntersectionObserver() {
        if (this.state.totalPages > 1) {
            this.observer = new IntersectionObserver(
                entries => entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        this.observer.unobserve(entry.target);
                        if (this.state.page + 1 < this.state.totalPages) {
                            this.setState({page: this.state.page + 1}, () =>
                                this.loadGroups(() => {
                                    this.observer.observe(document.querySelector('.group_card:last-child'));
                                })
                            );
                        }
                    }
                }),
                {
                    threshold: 0.75
                }
            );
            this.observer.observe(document.querySelector('.group_card:last-child'));
        }
    }

    render() {
        return (
            <div>
                <Card>
                    <CardHeader>
                        <h1>Groups</h1>
                        <Link to={"/group/create"} className={"create_group_button"}>Create group</Link>
                    </CardHeader>
                    <CardBody>
                        { this.state.groups.map((group, index) => <GroupCard key={group.id}
                                                                             id={group.id}
                                                                             address={group.address}
                                                                             title={group.title}
                                                                             description={group.description}
                                                                             subscribed={group.subscribed}
                                                                             userId={AuthService.getId()}
                        />) }
                    </CardBody>
                </Card>
            </div>
        );
    }
}