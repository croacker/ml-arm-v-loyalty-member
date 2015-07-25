package ru.peaksystems.varm.loyalty.service;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import ru.peak.ml.loyalty.core.data.MlUser;
import ru.peak.security.dao.MlUserDao;

/**
 *
 */
@Slf4j
public class SecurityServiceV {

  @Inject
  protected MlUserDao mlUserDao;

  /**
   * Только для участников
   *
   * @param login
   * @param password
   * @return
   */
  public MlUser authenticateMember(String login, String password) {
    MlUser result = null;
    MlUser mlUser = (MlUser) mlUserDao.getUserByLogin(login);
    if (mlUser == null) {
//      log.info(String.format("Auth - FAIL login = %s, wrong login", new Object[]{login}));
    } else {
      if (mlUser.getHash().equals(DigestUtils.sha512Hex(password))) {
//        log.info(String.format("Auth - SUCCESS login = %s", new Object[]{mlUser.getLogin()}));
        result = mlUser;
      } else {
//        log.info(String.format("Auth - FAIL login = %s, wrong password", new Object[]{mlUser.getLogin()}));
        result = null;
      }
    }

    return result;
  }

}
