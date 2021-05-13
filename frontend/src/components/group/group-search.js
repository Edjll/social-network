import * as React from "react";
import {Card} from "../card/card";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import RequestService from "../../services/RequestService";
import {GroupCard} from "./group-card";
import './group-search.css';
import AuthService from "../../services/AuthService";
import IntersectionObserverService from "../../services/IntersectionObserverService";
import {PrivateRoute} from "../security/private-route";
import {GroupCreator} from "./group-creator";
import {HiddenInfo} from "../hidden-info/hidden-info";

export class GroupSearch extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            groups: [],
            page: 0,
            size: 10,
            totalPages: 0
        }
    }

    componentDidMount() {
        this.loadGroups(() => IntersectionObserverService.create('.group_card:last-child', this, this.loadGroups));
    }

    loadGroups(callback) {
        RequestService.getAxios().get(RequestService.URL + "/groups", {
            params: {
                page: this.state.page,
                size: this.state.size
            }
        })
            .then(response => this.setState({
                groups: [...this.state.groups, ...response.data],
                lastSize: response.data.length
            }, () => {
                if (callback) callback();
            }))
    }

    render() {
        return (
            <div>
                <Card>
                    <CardHeader>
                        <h1>Groups</h1>
                        <HiddenInfo text={"✎"}
                                    hidden={"Create group"}
                                    link={"/groups/create"}/>
                    </CardHeader>
                    <CardBody>
                        {this.state.groups.map(group => <GroupCard key={group.id}
                                                                    id={group.id}
                                                                    address={group.address}
                                                                    title={group.title}
                                                                    description={group.description}
                                                                    subscribed={group.subscribed}
                                                                    userId={AuthService.getId()}
                        />)}
                    </CardBody>
                </Card>
                <PrivateRoute path={"/groups/create"} component={GroupCreator}/>
            </div>
        );
    }
}