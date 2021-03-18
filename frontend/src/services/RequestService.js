import axios from "axios";
import AuthService from "./AuthService";

const axi = axios.create();
const URL = "http://localhost:8085/";

const configure = () => {
    axi.interceptors.request.use((config) => {
        if (AuthService.authenticated()) {
            const cb = () => {
                config.headers.Authorization = `Bearer ${AuthService.token()}`;
                return Promise.resolve(config);
            };
            return AuthService.updateToken(cb);
        }
        return config;
    });
};

const getAxios = () => axi;

const RequestService = {
    URL,
    configure,
    getAxios
};

export default RequestService;