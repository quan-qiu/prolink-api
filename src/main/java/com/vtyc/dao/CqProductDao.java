package com.vtyc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CqProductDao {
    private static final Logger logger = LoggerFactory.getLogger(CzProductDao.class);

    @Qualifier("jdbcSecondaryTemplate")
    @Autowired
    JdbcTemplate jdbcSecondaryTemplate;

    public int getCountOfPt(){
        return jdbcSecondaryTemplate.queryForObject("select count(*) from pt_mstr", Integer.class);
    }

    public boolean isPartNumExist(String partNumber){

        String sql = "SELECT count(*) FROM pt_mstr WHERE pt_part=? ";

        int num = jdbcSecondaryTemplate.queryForObject(sql,
                new Object[] {partNumber},
                Integer.class);

        if (num > 0){
            return true;
        }else {
            return false;
        }
    }
}
