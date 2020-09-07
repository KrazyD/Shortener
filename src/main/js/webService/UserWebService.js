import client from '../webService/client';

export default class UserWebService {

    static registerUser(user) {
        return client({method: 'POST', path: '/register', entity: user}).then(response => {
            return response.entity;
        }, err => {
            throw UserWebService.getError(err);
        });
    }

    static getUsers() {
        return client({method: 'GET', path: '/user'}).then(response => {
            return {status: response.entity.status, data: response.entity.data.sort((a, b) => a.id - b.id)};
        }, err => {
            throw UserWebService.getError(err);
        });
    }

    static deleteUser(user) {
        return client({method: 'DELETE', path: '/user?id=' + user.id}).then(response => {
            return response.entity;
        }, err => {
            throw UserWebService.getError(err);
        });
    }

    static updateUser(user) {
        return client({method: 'PUT', path: '/user', entity: user}).then(response => {
            return response.entity;
        }, err => {
            throw UserWebService.getError(err);
        });
    }

    static getCurrentLogin() {
        return client({method: 'GET', path: '/login'}).then(response => {
            return response.entity;
        }, err => {
            throw UserWebService.getError(err);
        });
    }

    static getError(err) {
        let error = {};
        console.log('UserWebService.getError err=');
        console.log(err);
        if (err.hasOwnProperty('entity') && err.entity.hasOwnProperty('status')) {
            error = {status: err.entity.status, message: err.entity.data}
        } else {
            error = {status: 'Error', message: err.cause.message}
        }
        return error;
    }
}