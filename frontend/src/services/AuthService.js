import Keycloak from "keycloak-js";
import keycloakConfig from "../keycloak.json"

const keycloak = new Keycloak(keycloakConfig);

const initKeycloak = (onAuthenticatedCallback) => {
    keycloak.init({
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
        pkceMethod: 'S256',
        checkLoginIframe: false
    })
        .then((authenticated) => {
            onAuthenticatedCallback();
        })
};

const login = keycloak.login;

const logout = keycloak.logout;

const token = () => keycloak.token;

const authenticated = () => keycloak.authenticated;

const updateToken = (successCallback) =>
    keycloak.updateToken(5)
        .then(successCallback)
        .catch(login);

const getUsername = () => keycloak.tokenParsed.preferred_username;

const Role = {
    USER: 'USER',
    ADMIN: 'ADMIN'
}

const hasRole = (roles) => roles.some((role) => keycloak.hasRealmRole(role));

const AuthService = {
    initKeycloak,
    login,
    logout,
    authenticated,
    token,
    updateToken,
    getUsername,
    hasRole,
    Role
};

export default AuthService;