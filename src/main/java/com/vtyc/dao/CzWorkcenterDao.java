package com.vtyc.dao;

import com.vtyc.entity.Workcenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Repository
public class CzWorkcenterDao {

    private static final Logger logger = LoggerFactory.getLogger(CzWorkcenterDao.class);

    @Qualifier("jdbcPrimaryTemplate")
    @Autowired
    JdbcTemplate jdbcPrimaryTemplate;

    public List<Workcenter> getAllWorkcenterWorkcenterDesc(){

        String sql = "select wc_wkctr,wc_desc from wc_mstr " +
                " where upper(left(wc_wkctr,2))<>'CQ'" +
                " order by wc_wkctr";
        List<Workcenter> workcenters = jdbcPrimaryTemplate.query(sql,
                new BeanPropertyRowMapper(Workcenter.class));

        return workcenters;
    }

    public String getWcDesc(String workcenter){
        String sql = "select top 1 wc_desc from wc_mstr where wc_wkctr=?";
        String wc_desc = null;

        try {
            Object o = jdbcPrimaryTemplate.queryForObject(
                    sql,
                    new Object[]{workcenter},
                    String.class);
            wc_desc = (String) o;

        }catch (EmptyResultDataAccessException e){
            logger.warn("----LOG----wc_desc: " + "workcenter: " + workcenter + " doesn't exist.");
            e.printStackTrace();
        }

        if (wc_desc != null){
            return wc_desc;
        }else{
            return "";
        }
    }

    public List<Workcenter> constcenterToWorkcenter(String costcenter){
        String sql = "select wc_wkctr,wc_desc from wc_mstr where wc_dept=? and wc_desc not like '%无此线%' order by wc_wkctr";
        List<Workcenter> workcenters = jdbcPrimaryTemplate.query(sql,new Object[]{costcenter},
                new BeanPropertyRowMapper(Workcenter.class));

        return workcenters;

    }

    public String workcenterToConstcenter(String workcenter){
        String sql = "select wc_dept from wc_mstr where wc_wkctr=?";
        SqlRowSet costcenter = jdbcPrimaryTemplate.queryForRowSet(sql,workcenter);

        while (costcenter.next()){
            return costcenter.getString("wc_dept");
        }
        return "";
    }

    public SqlRowSet getAllConstcenter(){
        String sql =
                " select distinct(wc_dept) cost_center " +
                " from wc_mstr " +
                " where wc_dept <>'' and lower(left(wc_dept,1))<>'c'" ;
        SqlRowSet costcenters = jdbcPrimaryTemplate.queryForRowSet(sql);

        return costcenters;
    }
}
