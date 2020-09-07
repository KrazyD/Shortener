package shortener.service;

import shortener.entity.User;

public interface IUserService extends BaseService {

    User findByLogin(String login);

}
