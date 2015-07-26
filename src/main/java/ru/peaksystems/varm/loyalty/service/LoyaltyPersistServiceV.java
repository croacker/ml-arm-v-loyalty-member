package ru.peaksystems.varm.loyalty.service;

import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import ru.ml.core.common.guice.GuiceConfigSingleton;
import ru.peak.ml.core.dao.CommonDao;
import ru.peak.ml.core.model.MlDynamicEntityImpl;

@Slf4j
public class LoyaltyPersistServiceV {

    CommonDao commonDao;

    public CommonDao getCommonDao() {
        if (commonDao == null){
            commonDao = GuiceConfigSingleton.inject(CommonDao.class);
        }
        return commonDao;
    }

    @Transactional
    public void persist(MlDynamicEntityImpl entity){
        try{
            getCommonDao().persistWithoutSecurityCheck(entity);
        }catch (Exception e){
//            log.error("Error persist entity:[" + entity + "]");
//            log.error(e.getMessage(), e);
        }
    }

    @Transactional
    public void merge(MlDynamicEntityImpl entity){
        try{
            getCommonDao().mergeWithSecurityCheck(entity);
        }catch (Exception e){
//            log.error("Error persist entity:[" + entity + "]");
//            log.error(e.getMessage(), e);
        }
    }

}
