package com.vtyc.dao;

import com.vtyc.entity.Workcenter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Repository
public class CqWorkcenterDao {

    private static final Logger logger = LoggerFactory.getLogger(CzWorkcenterDao.class);

    @Qualifier("jdbcSecondaryTemplate")
    @Autowired
    JdbcTemplate jdbcSecondaryTemplate;

    public List<Workcenter> getAllWorkcenterWorkcenterDesc(){

        String sql = "select wc_wkctr,wc_desc from wc_mstr " +
                " where upper(left(wc_dept,2))='CQ' and wc_dept<>'' " +
                " order by wc_wkctr";
        List<Workcenter> workcenters = jdbcSecondaryTemplate.query(sql,
                new BeanPropertyRowMapper(Workcenter.class));

        return workcenters;
    }


    public String getWcDesc(String workcenter){
        String sql_count = "select count(wc_desc) from wc_mstr where wc_wkctr=?";
        String sql = "select top 1 wc_desc from wc_mstr where wc_wkctr=?";
        String wc_desc = null;

        try {
            Integer count = jdbcSecondaryTemplate.queryForObject(
                    sql_count,
                    new Object[]{workcenter},
                    Integer.class);

            if (count > 0){
                Object o = jdbcSecondaryTemplate.queryForObject(
                        sql,
                        new Object[]{workcenter},
                        String.class);
                wc_desc = (String) o;
            }else {
                logger.warn("----LOG----wc_desc: " + "workcenter: " + workcenter + " doesn't exist.");
            }
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
        List<Workcenter> workcenters = jdbcSecondaryTemplate.query(sql,new Object[]{costcenter},
                new BeanPropertyRowMapper(Workcenter.class));

        return workcenters;

    }

    public String workcenterToConstcenter(String workcenter){
        String sql = "select wc_dept from wc_mstr where wc_wkctr=?";
        SqlRowSet costcenter = jdbcSecondaryTemplate.queryForRowSet(sql,workcenter);

        while (costcenter.next()){
            return costcenter.getString("wc_dept");
        }
        return "";

    }

    public SqlRowSet getAllConstcenter(){
        String sql =
                " select distinct(wc_dept) cost_center " +
                " from wc_mstr " +
                " where wc_dept <>'' and lower(left(wc_dept,1))='c'" ;
        SqlRowSet costcenters = jdbcSecondaryTemplate.queryForRowSet(sql);

        return costcenters;
    }

}
