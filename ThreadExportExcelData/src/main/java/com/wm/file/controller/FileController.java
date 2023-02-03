package com.wm.file.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.pagehelper.PageHelper;
import com.wm.file.entity.MsgClient;
import com.wm.file.entity.UserEntity;
import com.wm.file.entity.UserEntityRes;
import com.wm.file.mapper.UserMapper;
import com.wm.file.service.UserService;
import com.wm.file.util.AsynExcelExportUtil;
import com.wm.file.util.MyExcelExportUtil;
import com.wm.file.util.PoiSXSSFWorkbookExcel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:FileController
 * @Description:
 * @Author:xiaomage
 * @Date:2020/6/15 22:42
 **/
@RestController
public class FileController {

    @Resource
    private MyExcelExportUtil myExcelExportUtil;
    @Resource
    private IExcelExportServer exportBigExcel;
    @Resource
    private AsynExcelExportUtil asynExcelExportUtil;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @GetMapping(value = "/exportEasyExcel")
    public  void exportEasyExcel(HttpServletResponse response,Integer total,Integer limit) {
        long start = System.currentTimeMillis();
        System.out.println("-----------任务执行开始-----------");
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");// 防止下载的文件名字乱码
        try {
            // 文件以附件形式下载
            response.setHeader("Content-disposition",
                    "attachment;filename=down_" +URLEncoder.encode(System.currentTimeMillis() + ".xlsx", "utf-8"));
            List<UserEntity> userEntityList = userService.selectAll(total,limit);
            long start1 = System.currentTimeMillis();
            EasyExcel.write(response.getOutputStream(), UserEntity.class).excelType(ExcelTypeEnum.XLSX).sheet("文件下载")
                    .doWrite(userEntityList);
            long end = System.currentTimeMillis();
            System.out.println("导出execl文件生成总耗时：  " + (end - start1) + "ms");
            System.out.println("本次导出execl任务执行完毕,共消耗：  " + (end - start) + "ms");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    @GetMapping(value = "/exportEasyExcelV4")
    public  void exportEasyExcelV4(HttpServletResponse response,Integer total,Integer limit) throws IOException {
        long start = System.currentTimeMillis();
        System.out.println("-----------任务执行开始-----------");
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");// 防止下载的文件名字乱码
        // 文件以附件形式下载
        response.setHeader("Content-disposition",
                "attachment;filename=down_" +URLEncoder.encode(System.currentTimeMillis() + ".xlsx", "utf-8"));
        try {
            String sheetName = "业务清单";
            String[] title = new String[]{
                    "主键ID"
                    ,"姓名"
                    ,"手机"
                    ,"创建人"
                    ,"备注"
                    ,"测试数据1"
                    ,"测试数据2"
                    ,"测试数据3"
                    ,"测试数据4"
                    ,"测试数据5"
                    ,"测试数据6"
                    ,"测试数据7"
                    ,"测试数据8"
                    ,"测试数据9"
                    ,"测试数据10"
                    ,"测试数据11"
                    ,"测试数据12"
                    ,"测试数据13"
                    ,"测试数据14"
                    ,"测试数据15"
                    ,"测试数据16"
                    ,"测试数据17"
                    ,"测试数据18"
                    ,"测试数据19"
                    ,"测试数据20"
                    ,"测试数据21"
            };
            List<List<String>> data1 = new ArrayList<List<String>>();
            List<UserEntity> userEntityList = userService.selectAll(total,limit);
            for (UserEntity userEntity : userEntityList) {
                List<String> rowData = new ArrayList<>();
                rowData.add(userEntity.getId()); //归属机构
                rowData.add(userEntity.getName()); //姓名
                rowData.add(userEntity.getPhone()); //姓名
                rowData.add(userEntity.getCeateBy()); //姓名
                rowData.add(userEntity.getRemark()); //姓名
                rowData.add(userEntity.getTest1()); //姓名
                rowData.add(userEntity.getTest2()); //姓名
                rowData.add(userEntity.getTest3()); //姓名
                rowData.add(userEntity.getTest4()); //姓名
                rowData.add(userEntity.getTest5()); //姓名
                rowData.add(userEntity.getTest6()); //姓名
                rowData.add(userEntity.getTest7()); //姓名
                rowData.add(userEntity.getTest8()); //姓名
                rowData.add(userEntity.getTest9()); //姓名
                rowData.add(userEntity.getTest10()); //姓名
                rowData.add(userEntity.getTest11()); //姓名
                rowData.add(userEntity.getTest12()); //姓名
                rowData.add(userEntity.getTest13()); //姓名
                rowData.add(userEntity.getTest14()); //姓名
                rowData.add(userEntity.getTest15()); //姓名
                rowData.add(userEntity.getTest16()); //姓名
                rowData.add(userEntity.getTest17()); //姓名
                rowData.add(userEntity.getTest18()); //姓名
                rowData.add(userEntity.getTest19()); //姓名
                rowData.add(userEntity.getTest20()); //姓名
                rowData.add(userEntity.getTest21()); //姓名
                data1.add(rowData);
            }
            long start1 = System.currentTimeMillis();
            SXSSFWorkbook wb = PoiSXSSFWorkbookExcel.getSxssfwbExcel(sheetName,title, data1);
            long end = System.currentTimeMillis();
            System.out.println("导出execl文件生成总耗时：  " + (end - start1) + "ms");
            ServletOutputStream output = response.getOutputStream();
            wb.write(output);
            output.flush();
            output.close();
            System.out.println("本次导出execl任务执行完毕,共"+userEntityList.size()+"条数据,共消耗：  " + (end - start) + "ms");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @GetMapping(value = "/exportEasyExcel1")
    public  void exportEasyExcel1(HttpServletResponse response,Integer pageNum,Integer pageSize,Integer limit) {
        long start = System.currentTimeMillis();
        System.out.println("-----------任务执行开始-----------");
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");// 防止下载的文件名字乱码
        try {
            // 文件以附件形式下载
            response.setHeader("Content-disposition",
                    "attachment;filename=down_" +URLEncoder.encode(System.currentTimeMillis() + ".xlsx", "utf-8"));
            PageHelper.startPage(pageNum,pageSize);
            long start1 = System.currentTimeMillis();
            List<UserEntity> userEntityList = userMapper.selectAll();
            System.out.println("查询数据条数:"+userEntityList.size()+" 数据库查询数据总耗时：  " + (System.currentTimeMillis() - start1) + "ms");
            long start2 = System.currentTimeMillis();
            EasyExcel.write(response.getOutputStream(), UserEntity.class).sheet("文件下载")
                    .doWrite(userEntityList);
            System.out.println("execl生成总耗时：  " + (System.currentTimeMillis() - start2) + "ms");
            System.out.println("任务执行完毕共消耗：  " + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/asynExport1")
    public void asynExportData1(HttpServletResponse response) throws InterruptedException {
        System.out.println("请求成功");
    }

    @GetMapping(value = "/asynExport")
    public void asynExportData(HttpServletResponse response) throws InterruptedException {
        asynExcelExportUtil.threadExcel(response);
        //1000000-39511ms 100000-7750ms 10000-789ms
    }

    /**
     * 数据库40w数据导出execl
     * @param response
     * @throws InterruptedException
     */
    @GetMapping(value = "/asynExportDatabtase")
    public void asynExportDatabtase(HttpServletResponse response) throws InterruptedException {
        asynExcelExportUtil.threadExcelDatabase(response);
        //1000000-39511ms 100000-7750ms 10000-789ms
    }

    /**
     * 普通数据量导出(修改表格宽度测试)
     */
    @GetMapping(value = "/export2")
    public void test2() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {  //测试数据
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("小明xxxsxsxsxsxsxsxsxsxsx" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            list.add(client);
        }
        try {
            long start = System.currentTimeMillis();
            Workbook workbook = myExcelExportUtil.getWorkbook("计算机一班学生", "学生", MsgClient.class, list, ExcelType.XSSF);
            String filePath = "C:\\Users\\Administrator\\Desktop\\export\\exportAll.xlsx";
            File file = new File(filePath);
            MyExcelExportUtil.exportExcel2(workbook, file);
            long end = System.currentTimeMillis();
            System.out.println("任务执行完毕共消耗：  " + (end - start) + "ms");
            //1000000-69407ms  100000-3518ms 10000-1310ms
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 大数据量导出测试
     *
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/bigDataExport")
    public void bigDataExport(HttpServletResponse response) throws IOException {
        Date start = new Date();
        ExportParams params = new ExportParams("大数据测试", "测试");
        Workbook workbook = ExcelExportUtil.exportBigExcel(params, MsgClient.class, exportBigExcel, new Object());
        MyExcelExportUtil.exportExcel(workbook, String.valueOf(System.currentTimeMillis()), response);
        System.out.println("bigDataExport:" + (new Date().getTime() - start.getTime()));//10000-bigDataExport:2278 100000-bigDataExport:19083 1000000-bigDataExport:693672
    }




}
