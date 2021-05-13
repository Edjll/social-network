import axios from "axios";
import AuthService from "./AuthService";
import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';

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

const StompInstance = class {
    static stompClient = null;

    static connect(onConnected, onError) {
        const sockJS = new SockJS(URL + "/ws");
        StompInstance.stompClient = Stomp.over(sockJS);
        StompInstance.stompClient.connect({ "Authorization": `Bearer ${AuthService.getToken()}` } , onConnected, onError);
    }

    static subscribe(url, messageReceived) {
        StompInstance.stompClient.subscribe(url, messageReceived);
    }

    static send(url, body) {
        StompInstance.stompClient.send(url, {}, JSON.stringify(body));
    }

    static disconnect() {
        StompInstance.stompClient.disconnect();
        StompInstance.stompClient = null;
    }
}

const RequestService = {
    URL,
    ADMIN_URL,
    configure,
    getAxios,
    StompInstance
};

export default RequestService;