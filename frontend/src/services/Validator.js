import validation from './validation.json';

const validate = (title, value, params = {
    notNull: false,
    minLength: null,
    maxLength: null,
    pattern: null
}, message = null) => {
    if (params.notNull && (value === null || value === '')) {
        return message ? message : `${title} ${message ? message : validation.defaultMessage.notNull}`;
    }
    if (params.minLength && value.length < params.minLength) {
        return message ? message : `${title} ${message ? message : validation.defaultMessage.minLength} ${params.minLength}`;
    }
    if (params.maxLength && value.length > params.maxLength) {
        return message ? message : `${title} ${message ? message : validation.defaultMessage.maxLength} ${params.maxLength}`;
    }
    if (params.pattern && !new RegExp(params.pattern).test(value)) {
        return message ? message : `${title} ${message ? message : validation.defaultMessage.pattern}`;
    }
    return null;
}

const Validator = {
    validate
}

export default Validator;