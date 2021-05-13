import keycloakConfig from "./keycloak.json";
import axios from "axios";
import jwt from 'jsonwebtoken';

export default class AuthService {

    static #instance;
    #authenticated = false;
    static #clientId = keycloakConfig.clientId;
    static #realm = keycloakConfig.realm;
    static #url = keycloakConfig.url;
    #publicKey = keycloakConfig["public-key"]
    #token = null;
    #refreshToken = null;
    #parsedToken = null;

    static GrantType = class {
        static PASSWORD = 'password';
        static REFRESH_TOKEN = 'refresh_token';
    }

    static Role = class {
        static USER = 'USER';
        static ADMIN = 'ADMIN';
    }

    constructor() {
        const cookie = this.parseCookie();
        if (cookie.token !== null && !this.validate(cookie.token)) {
            cookie.token = null;
        }
        this.setToken(cookie.token, cookie.refreshToken);
    }

    static init(callback) {
        AuthService.#instance = new AuthService();
        const refreshToken = AuthService.getRefreshToken();
        if (refreshToken !== null) {
            AuthService.#instance
                .loginByRefreshToken(refreshToken)
                .then(callback)
                .catch(() => AuthService.#instance.clearToken());
        } else {
            callback();
        }
    }

    static login(username, password) {
        const params = new URLSearchParams();

        params.append("grant_type", AuthService.GrantType.PASSWORD);
        params.append("client_id", AuthService.#clientId);
        params.append("username", username);
        params.append("password", password);

        return AuthService.#instance
            .loginRequest(params)
            .then(() => {
                window.location = document.referrer;
                return Promise.resolve();
            })
            .catch(() => {
                AuthService.#instance.clearToken();
                return Promise.reject();
            })
    }

    static toLoginPage() {
        window.location = window.location.origin + '/login';
    }

    static logout(callback) {
        const params = new URLSearchParams();

        params.append("refresh_token", this.getRefreshToken());
        params.append("client_id", AuthService.#clientId);

        return axios
            .post(
                `${AuthService.#url}/realms/${AuthService.#realm}/protocol/openid-connect/logout`,
                params,
                {
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    withCredentials: true
                }
            )
            .then(() => {
                AuthService.#instance.clearToken();
                if (callback) callback();
            })
    }

    clearToken() {
        document.cookie = `access_token=; max-age=-1`
        document.cookie = `refresh_token=; max-age=-1`;
        this.#authenticated = false;
        this.#token = null;
        this.#refreshToken = null;
        this.#parsedToken = null;
    }

    setToken(token, refreshToken, tokenAge, refreshTokenAge) {
        if (tokenAge !== undefined) document.cookie = `access_token=${token}; path=/; max-age=${tokenAge}`;
        if (refreshTokenAge !== undefined) document.cookie = `refresh_token=${refreshToken}; path=/; max-age=${refreshTokenAge}`;
        if (token !== null) {
            this.#authenticated = true;
            this.#token = token;
            this.#parsedToken = JSON.parse(decodeURIComponent(escape(atob(token.split('.')[1]))));
        }
        if (refreshToken !== undefined) this.#refreshToken = refreshToken;
    }

    static isTokenExpired() {
        return (this.getParsedToken()['exp'] - Math.ceil(new Date().getTime() / 1000) - 5) < 0;
    }

    static updateToken(callback) {
        if (AuthService.getRefreshToken() !== null && AuthService.isTokenExpired()) {
            return AuthService.#instance
                .loginByRefreshToken(AuthService.getRefreshToken())
                .then(callback)
                .catch(() => AuthService.toLoginPage());
        }
        return Promise
            .resolve()
            .then(callback);
    }

    static forceUpdateToken(callback) {
        if (AuthService.getRefreshToken() !== null) {
            return AuthService.#instance
                .loginByRefreshToken(AuthService.getRefreshToken())
                .then(callback)
                .catch(() => AuthService.toLoginPage());
        }
        return Promise
            .resolve()
            .then(callback);
    }

    loginByRefreshToken(refreshToken) {
        const params = new URLSearchParams();

        params.append("grant_type", AuthService.GrantType.REFRESH_TOKEN);
        params.append("client_id", AuthService.#clientId);
        params.append("refresh_token", refreshToken);

        return this.loginRequest(params);
    }

    loginRequest(params) {
        return axios
            .post(
                `${AuthService.#url}/realms/${AuthService.#realm}/protocol/openid-connect/token`,
                params,
                {
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    withCredentials: true
                }
            )
            .then(response => {
                this.setToken(
                    response.data['access_token'],
                    response.data['refresh_token'],
                    response.data['expires_in'],
                    response.data['refresh_expires_in']
                );
            });
    }

    validate(token) {
        try {
            jwt.verify(
                token,
                "-----BEGIN PUBLIC KEY-----\n" +
                                this.#publicKey +
                                "-----END PUBLIC KEY-----",
                {
                    algorithms: ['RS256']
                }
            );
        } catch (error) {
            document.cookie = `access_token=; max-age=-1`;
            return false;
        }
        return true;
    }

    static isAuthenticated() {
        return AuthService.#instance.#authenticated;
    }

    static getToken() {
        return AuthService.#instance.#token;
    }

    static getParsedToken() {
        return AuthService.#instance.#parsedToken;
    }

    static getRefreshToken() {
        return AuthService.#instance.#refreshToken;
    }

    static hasRole(roles) {
        if (roles === undefined || roles === null) return true;
        const realmRoles = AuthService.getParsedToken()['realm_access']['roles'];
        return roles.some((role) => realmRoles.includes(role));
    }

    static getUsername() {
        return AuthService.getParsedToken()['preferred_username'];
    }

    static getFirstName() {
        return AuthService.getParsedToken()['given_name'];
    }

    static getLastName() {
        return AuthService.getParsedToken()['family_name'];
    }

    static getId() {
        return AuthService.getParsedToken() ? AuthService.getParsedToken()['sub'] : null;
    }

    static getRealm() {
        return AuthService.#instance.#realm;
    }

    parseCookie() {
        const cookies = document.cookie.split('; ');

        let token = cookies.find(row => row.startsWith('access_token='));
        token = token ? token.split('=')[1] : null;

        let refreshToken = cookies.find(row => row.startsWith('refresh_token='));
        refreshToken = refreshToken ? refreshToken.split('=')[1] : null;

        return {token: token, refreshToken: refreshToken};
    }
}