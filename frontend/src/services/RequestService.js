import axios from "axios";
import AuthService from "./AuthService";

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

const RequestService = {
    URL,
    ADMIN_URL,
    configure,
    getAxios
};

export default RequestService;