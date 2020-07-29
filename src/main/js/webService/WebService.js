import client from '../webService/client';

export default class WebService {

    static getUsers() {
        return client({method: 'GET', path: '/user'}).then(response => {
            return {status: response.entity.status, data: response.entity.data.sort((a, b) => a.id - b.id)};
        }, err => {
            throw WebService.getError(err);
        });
    }

    static deleteUser(user) {
        return client({method: 'DELETE', path: '/user?id=' + user.id}).then(response => {
            return response.entity;
        }, err => {
            throw WebService.getError(err);
        });
    }

    static registerUser(user) {
        return client({method: 'POST', path: '/user', entity: user}).then(response => {
            return response.entity;
        }, err => {
            throw WebService.getError(err);
        });
    }

    static updateUser(user) {
        return client({method: 'PUT', path: '/user', entity: user}).then(response => {
            return response.entity;
        }, err => {
            throw WebService.getError(err);
        });
    }

    static login(credentials) {
        return client({method: 'POST', path: '/login', entity: credentials}).then(response => {
            return response.entity;
        }, err => {
            throw WebService.getError(err);
        });
    }

    static getError(err) {
        let error = {};
        if (err.hasOwnProperty('entity')) {
            error = {status: err.entity.status, message: err.entity.data}
        } else {
            error = {status: 'Error', message: err.cause.message}
        }
        return error;
    }
}