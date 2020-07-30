import client from '../webService/client';

export default class RefWebService {

    static getRefs() {
        return client({method: 'GET', path: '/ref'}).then(response => {
            return {status: response.entity.status, data: response.entity.data.sort((a, b) => a.id - b.id)};
        }, err => {
            throw RefWebService.getError(err);
        });
    }

    static deleteRef(ref) {
        return client({method: 'DELETE', path: '/ref?reducedRef=' + ref.reducedRef}).then(response => {
            return response.entity;
        }, err => {
            throw RefWebService.getError(err);
        });
    }

    static createRef(ref, userId) {
        let form = {fullRef: ref.fullRef, userId: userId};
        return client({method: 'POST', path: '/ref', entity: form}).then(response => {
            return response.entity;
        }, err => {
            throw RefWebService.getError(err);
        });
    }

    static updateRef(ref) {
        let form = {refId: ref.id, fullRef: ref.fullRef};
        return client({method: 'PUT', path: '/ref', entity: form}).then(response => {
            return response.entity;
        }, err => {
            throw RefWebService.getError(err);
        });
    }

    static getError(err) {
        let error = {};
        if (err.hasOwnProperty('entity') && err.entity.hasOwnProperty('status')) {
            error = {status: err.entity.status, message: err.entity.data}
        } else {
            error = {status: 'Error', message: err.cause.message}
        }
        return error;
    }
}