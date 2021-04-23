import axios from "axios";
import AuthService from "./AuthService";
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const axi = axios.create();
const URL = 'http://localhost:8085';
const ADMIN_URL = 'http://localhost:8085/admin';

const configure = () => {
    axi.interceptors.request.use((config) => {
        if (AuthService.isAuthenticated()) {
            const cb = () => {
                config.headers.Authorization = `Bearer ${AuthService.getToken()}`;
                return Promise.resolve(config);
            };
            return AuthService.updateToken(cb);
        }
        return config;
    });
};

const getAxios = () => axi;

const sockJS = new SockJS(URL + "/ws");
const stompClient = Stomp.over(sockJS);

const stompConnect = (onConnected, onError) => {
    stompClient.connect({ "Authorization": `Bearer ${AuthService.getToken()}` } , onConnected, onError);
}

const stompSubscribe = (url, messageReceived) => {
    stompClient.subscribe(url, messageReceived);
}

const stompSend = (url, body) => {
    stompClient.send(url, {}, JSON.stringify(body));
}

const RequestService = {
    URL,
    ADMIN_URL,
    configure,
    getAxios,
    stompConnect,
    stompSubscribe,
    stompSend
};

export default RequestService;