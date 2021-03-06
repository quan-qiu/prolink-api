package com.vtyc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class CqProductionDao {

    private static final Logger logger = LoggerFactory.getLogger(CzProductionDao.class);

    @Qualifier("jdbcSecondaryTemplate")
    @Autowired
    JdbcTemplate jdbcSecondaryTemplate;


    public SqlRowSet getProductionByDate(String s_date){

        String sql = "select op_wo_op,op_emp,op_wkctr as op_line,op_qty_wip as op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_date=?";

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {s_date}
        );

        return production;
    }

    public SqlRowSet getDayShiftProduction(String workcenter, String s_date){

        String sql = "select op_wo_op,op_emp,op_wkctr as op_line,cast(round(sum(op_qty_wip),0,1) as int) op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_line=? and op_date=?" +
                " and (op_time>=28800 and op_time<=72000)" +
                " group by op_wo_op,op_emp,op_line,op_part,op_date,op_dept";

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {workcenter, s_date}
        );

        return production;
    }

    public SqlRowSet getDayShiftProductionBy2Shift(String workcenter, String s_date){

        String sql = "select op_wo_op,op_emp,op_wkctr as op_line,cast(round(sum(op_qty_wip),0,1) as int) as op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_wkctr=? and op_date=?" +
                " and (op_time>=30600 and op_time<=73800)" +
                " group by op_wo_op,op_emp,op_wkctr,op_part,op_date,op_dept";

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {workcenter, s_date}
        );

        return production;
    }

    public SqlRowSet getAllDayShiftProduction(String s_date){

        String sql = "select op_wo_op,op_emp,op_wkctr as op_line,cast(round(sum(op_qty_wip),0,1) as int) as op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_date=? and op_emp<>'' and op_wkctr<>''" +
                " and (op_time>=28800 and op_time<=72000)" +
                " group by op_wo_op,op_emp,op_wkctr,op_part,op_date,op_dept";

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {s_date}
        );

        return production;
    }

    public SqlRowSet getAllDayShiftProductionBy2Shift(String s_date){

        String sql = "select op_wo_op,op_emp,op_wkctr as op_line,cast(round(sum(op_qty_wip),0,1) as int) as op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_date=? and op_emp<>'' and op_wkctr<>''" +
                " and (op_time>=30600 and op_time<=73800)" +
                " group by op_wo_op,op_emp,op_wkctr,op_part,op_date,op_dept";

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {s_date}
        );

        return production;
    }

    public SqlRowSet getNightShiftProduction(String workcenter, String s_date){

        String sql = " select op_wo_op,op_emp,op_wkctr as op_line,cast(round(sum(op_qty_wip),0,1) as int) as op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_wkctr=? and op_date=?" +
                " and ((op_time>72000 and op_time<=86400) or (op_time<28800))" +
                " group by op_wo_op,op_emp,op_wkctr,op_part,op_date,op_dept"
                ;

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {workcenter, s_date}
        );

        return production;
    }

    public SqlRowSet getNightShiftProductionBy2Shift(String workcenter, String s_date){

        String sql = " select op_wo_op,op_emp,op_wkctr as op_line,cast(round(sum(op_qty_wip),0,1) as int) as op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_wkctr=? and op_date=?" +
                " and ((op_time>=73800 and op_time<=86400) or (op_time<30600))" +
                " group by op_wo_op,op_emp,op_wkctr,op_part,op_date,op_dept"
                ;

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {workcenter, s_date}
        );

        return production;
    }

    public SqlRowSet getAllNightShiftProduction(String s_date){

        String sql = " select op_wo_op,op_emp,op_wkctr as op_line,cast(round(sum(op_qty_wip),0,1) as int) as op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_date=? and op_emp<>'' and op_wkctr<>''" +
                " and ((op_time>72000 and op_time<=86400) or (op_time<28800))" +
                " group by op_wo_op,op_emp,op_wkctr,op_part,op_date,op_dept"
                ;

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {s_date}
        );

        return production;
    }

    public SqlRowSet getAllNightShiftProductionBy2Shift(String s_date){

        String sql = " select op_wo_op,op_emp,op_wkctr as op_line,cast(round(sum(op_qty_wip),0,1) as int) as op_qty_comp,op_part,op_date,op_dept,op_time" +
                " from op_hist" +
                " where op_qty_wip<>0 and op_date=? and op_emp<>'' and op_wkctr<>''" +
                " and ((op_time>73800 and op_time<=86400) or (op_time<30600))" +
                " group by op_wo_op,op_emp,op_wkctr,op_part,op_date,op_dept"
                ;

        SqlRowSet production = jdbcSecondaryTemplate.queryForRowSet(sql,
                new Object[] {s_date}
        );

        return production;
    }
}
