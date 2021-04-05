import Keycloak from "keycloak-js";
import keycloakConfig from "../keycloak.json";
import axios from "axios";

const keycloak = new Keycloak(keycloakConfig);

const initKeycloak = (onAuthenticatedCallback, token = null, refreshToken = null) => {
    let initOptions = {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri : window.location.origin  +  '/silent-check-sso.html',
        pkceMethod: 'S256',
        checkLoginIframe: true
    }

    if (token && refreshToken) {
        initOptions = {
            ...initOptions,
            token: token,
            refreshToken: refreshToken
        }
    }

    keycloak.init(initOptions)
        .then(() => {
            if (onAuthenticatedCallback) onAuthenticatedCallback();
        })
};

const login = keycloak.login;

// const login = (username, password) => {
//     const params = new URLSearchParams();
//
//     params.append("grant_type", "password");
//     params.append("client_id", "spring-boot");
//     params.append("username", username);
//     params.append("password", password);
//
//     axios.post(
//         'http://keycloak:8080/auth/realms/social-network/protocol/openid-connect/token',
//         params,
//         { headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, withCredentials: true }
//     )
//         .then(response => {
//             localStorage.setItem("token", response.data.access_token);
//             localStorage.setItem("refreshToken", response.data.refresh_token);
//             window.location.reload();
//         })
// };

const logout = keycloak.logout;

const getToken = () => keycloak.token;

const isAuthenticated = () => keycloak.authenticated;

const updateToken = (successCallback) =>
    keycloak.updateToken(5)
        .then(successCallback)
        .catch(login);

const getUsername = () => keycloak.tokenParsed.preferred_username;

const getId = () => keycloak.tokenParsed.sub;

const getFirstName = () => keycloak.tokenParsed.given_name;

const getLastName = () => keycloak.tokenParsed.family_name;

const getFullName = () => keycloak.tokenParsed.name;

const Role = {
    USER: 'USER',
    ADMIN: 'ADMIN'
}

const hasRole = (roles) => {
    if (roles === undefined || roles === null) return true;
    return roles.some((role) => keycloak.hasRealmRole(role));
}

const getRealm = keycloak.realm;

const AuthService = {
    initKeycloak,
    login,
    logout,
    isAuthenticated,
    getToken,
    updateToken,
    getUsername,
    getId,
    getFirstName,
    getLastName,
    getFullName,
    hasRole,
    Role,
    getRealm
};

export default AuthService;