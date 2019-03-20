package com.vtyc.controller;

import com.vtyc.dao.CqProductDao;
import com.vtyc.dao.CqProductionDao;
import com.vtyc.dao.CqWorkcenterDao;
import com.vtyc.entity.Workcenter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping(value = "CQ")
public class CQProlinkSQLController {

    @Autowired
    CqProductDao cqProductDao;

    @Autowired
    CqWorkcenterDao cqWorkcenterDao;

    @Autowired
    CqProductionDao cqProductionDao;

    @RequestMapping(value="api",method = RequestMethod.GET)
    public String getAllApi(){
        JSONObject result = new JSONObject();
        result.put("/CQ/count_pt","Count the number of records in table pt_mstr");
        result.put("/CQ/pt/{id}", "Check partnumber existing in table pt_mstr");
        result.put("/CQ/wc_wcd", "Get all workcenters and workcenter descriptions array");
        result.put("/CQ/wc/{wc_id}", "Get workcenter description by workcenter id");
        result.put("/CQ/wcs", "Get all workcenter array");

        return result.toJSONString();
    }

    @RequestMapping(value = "count_pt", method = RequestMethod.GET)
    public String getCountPt(){
        JSONObject result = new JSONObject();
        String status = "true";

        int num = cqProductDao.getCountOfPt();

        result.put("count", num);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "pt/{id}/**", method = RequestMethod.GET)
    public String checkPartNum(
            @PathVariable("id") String id,
            HttpServletRequest request){

        String requestURL = request.getRequestURL().toString();
        // logger.info("-----------------requestURL:    " + requestURL);
        String id_real = requestURL.split("/pt/")[1];

        String id_real_decode = "";
        try{

            id_real_decode = URLDecoder.decode(id_real, "UTF-8");
        }catch (UnsupportedEncodingException e){

        }

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        String status = "true";

        boolean exist = cqProductDao.isPartNumExist(id_real_decode);

        JSONObject obj = new JSONObject();
        obj.put("exist", exist);
        data.add(obj);

        result.put("data", data);
        result.put("status", status);

        return result.toJSONString();
    }

    @RequestMapping(value = "wc_wcd", method = RequestMethod.GET)
    public String getAllWorkcenterWorkcenterDecs(){
        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        List<Workcenter> workcenters = cqWorkcenterDao.getAllWorkcenterWorkcenterDesc();

        for (Workcenter wc : workcenters) {
            JSONObject obj = new JSONObject();
            obj.put("wc_wkctr", wc.getWc_wkctr());
            obj.put("wc_desc", wc.getWc_desc());

            data.add(obj);
        }

        if (workcenters != null){
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "wc/{wc_id}", method = RequestMethod.GET)
    public String getWorkcenterDesc(@PathVariable("wc_id") String wc_id){
        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        String wc_desc = cqWorkcenterDao.getWcDesc(wc_id);
        JSONObject obj = new JSONObject();
        obj.put("wc_desc", wc_desc);

        data.add(obj);

        if(wc_desc != null){
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "wcs", method = RequestMethod.GET)
    public String getAllWorkcenters(){
        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        List<Workcenter> workcenters = cqWorkcenterDao.getAllWorkcenterWorkcenterDesc();

        for (Workcenter wc : workcenters) {
            JSONObject obj = new JSONObject();
            obj.put("wc_wkctr", wc.getWc_wkctr());
            data.add(obj);
        }

        if(workcenters.size() > 0){
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "productionDayBy2Shift/{workcenter}/{s_date}", method = RequestMethod.GET)
    public String getProductionByDay(@PathVariable("workcenter") String workcenter,
                                     @PathVariable("s_date") String s_date){

        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        SqlRowSet workhour = cqProductionDao.getDayShiftProductionBy2Shift(workcenter,s_date);

        while (workhour.next()) {

            JSONObject obj = new JSONObject();
            obj.put("op_wo_op", workhour.getString("op_wo_op"));
            obj.put("op_emp", workhour.getString("op_emp"));
            obj.put("op_line", workhour.getString("op_line"));
            obj.put("op_qty_comp", workhour.getString("op_qty_comp"));
            obj.put("op_part", workhour.getString("op_part"));
            obj.put("op_dept", workhour.getString("op_dept"));

            data.add(obj);
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "productionByDate/{s_date}", method = RequestMethod.GET)
    public String getProductionByDate(@PathVariable("s_date") String s_date){
        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        SqlRowSet production = cqProductionDao.getProductionByDate(s_date);

        while (production.next()) {

            JSONObject obj = new JSONObject();
            obj.put("op_wo_op", production.getString("op_wo_op"));
            obj.put("op_emp", production.getString("op_emp"));
            obj.put("op_line", production.getString("op_line"));
            obj.put("op_qty_comp", production.getString("op_qty_comp"));
            obj.put("op_part", production.getString("op_part"));
            obj.put("shift", "day");
            obj.put("op_dept",  production.getString("op_dept"));
            obj.put("op_date", production.getString("op_date"));
            obj.put("op_time", production.getString("op_time"));

            data.add(obj);
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "productionDayAllBy2Shift/{s_date}", method = RequestMethod.GET)
    public String getProductionByDayAll(@PathVariable("s_date") String s_date){
        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        SqlRowSet production = cqProductionDao.getAllDayShiftProductionBy2Shift(s_date);

        while (production.next()) {

            JSONObject obj = new JSONObject();
            obj.put("op_wo_op", production.getString("op_wo_op"));
            obj.put("op_emp", production.getString("op_emp"));
            obj.put("op_line", production.getString("op_line"));
            obj.put("op_qty_comp", production.getString("op_qty_comp"));
            obj.put("op_part", production.getString("op_part"));
            obj.put("shift", "day");
            obj.put("op_dept",  production.getString("op_dept"));

            data.add(obj);
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "productionNightBy2Shift/{workcenter}/{s_date}", method = RequestMethod.GET)
    public String getProductionByNight(@PathVariable("workcenter") String workcenter,
                                       @PathVariable("s_date") String s_date){

        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        SqlRowSet production = cqProductionDao.getNightShiftProductionBy2Shift(workcenter,s_date);

        while (production.next()) {

            JSONObject obj = new JSONObject();
            obj.put("op_wo_op", production.getString("op_wo_op"));
            obj.put("op_emp", production.getString("op_emp"));
            obj.put("op_line", production.getString("op_line"));
            obj.put("op_qty_comp", production.getString("op_qty_comp"));
            obj.put("op_part", production.getString("op_part"));
            obj.put("shift", "night");
            obj.put("op_dept", production.getString("op_dept"));

            data.add(obj);
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "productionNightAllBy2Shift/{s_date}", method = RequestMethod.GET)
    public String getProductionByNightAll(@PathVariable("s_date") String s_date){
        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        SqlRowSet production = cqProductionDao.getAllNightShiftProductionBy2Shift(s_date);

        while (production.next()) {

            JSONObject obj = new JSONObject();
            obj.put("op_wo_op", production.getString("op_wo_op"));
            obj.put("op_emp", production.getString("op_emp"));
            obj.put("op_line", production.getString("op_line"));
            obj.put("op_qty_comp", production.getString("op_qty_comp"));
            obj.put("op_part", production.getString("op_part"));
            obj.put("shift", "night");
            obj.put("op_dept", production.getString("op_dept"));

            data.add(obj);
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "costcenterToWorkcenters/{costcenter}", method = RequestMethod.GET)
    public String costcenterToWorkcenters(@PathVariable("costcenter") String costcenter){
        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        List<Workcenter> workcenters = cqWorkcenterDao.constcenterToWorkcenter(costcenter);

        for (Workcenter workcenter: workcenters
                ) {
            JSONObject obj = new JSONObject();
            obj.put("workcenter",workcenter.getWc_wkctr());
            obj.put("workcenter_desc",workcenter.getWc_desc());

            data.add(obj);
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }

    @RequestMapping(value = "workcenterToCostcenter/{workcenter}", method = RequestMethod.GET)
    public String workcenterToCostcenter(@PathVariable("workcenter") String workcenter){
        String costcenter = cqWorkcenterDao.workcenterToConstcenter(workcenter);

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        JSONObject obj = new JSONObject();
        obj.put("costcenter",costcenter);

        data.add(obj);

        result.put("data", data);

        return result.toJSONString();
    }

    @RequestMapping(value = "ccs", method = RequestMethod.GET)
    public String getAllCostCenter(){
        SqlRowSet costCenters = cqWorkcenterDao.getAllConstcenter();
        String status = "false";
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        while (costCenters.next()) {

            JSONObject obj = new JSONObject();
            obj.put("cost_center", costCenters.getString("cost_center"));

            data.add(obj);
            status = "true";
        }

        result.put("data", data);
        result.put("status", status);
        return result.toJSONString();
    }
}
