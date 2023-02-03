package com.wm.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.wm.file.entity.UserEntity;
import com.wm.file.listen.EasyExceGeneralDatalListener;
import com.wm.file.mapper.UserMapper;
import com.wm.file.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName:FileController
 * @Description:
 * @Author:xiaomage
 * @Date:2020/6/15 22:42
 **/
@RestController
@Slf4j
public class FileController {


    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 多线程实现百万导出到excel 50s优化到11s
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        long start = System.currentTimeMillis();
        System.out.println("-----------任务执行开始-----------");
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");// 防止下载的文件名字乱码
        try {
            // 文件以附件形式下载
            response.setHeader("Content-disposition",
                    "attachment;filename=down_" + URLEncoder.encode(System.currentTimeMillis() + ".xlsx", "utf-8"));
            //获取总数据量
            int count = userService.count();
            //每页多少个
            int pageSize = 10000;
            //必须放到循环外，否则会刷新流
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
            List<CompletableFuture> completableFutures = new ArrayList<>();
            for (int i = 0; i < (count / pageSize) + 1; i++) {
                int finalI = i;
                // 使用CompletableFuture可完成的未来runAsync运行异步任务，参数threadPoolExecutor线程池装配进来
                CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                    List<UserEntity> exportList = userService.findLimit(finalI * pageSize, pageSize);
                    if (!CollectionUtils.isEmpty(exportList)) {
                        WriteSheet writeSheet = EasyExcel.
                                writerSheet(finalI, "用户" + (finalI + 1)).head(UserEntity.class)
                                //                        最长匹配列宽度样式策略（样式）
                                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
                        // 多线程分页查询数据库，单线程写入磁盘（多线程访问共享变量excelWriter加锁）
                        synchronized (excelWriter) {
                            excelWriter.write(exportList, writeSheet);
                        }
                    }
                }, threadPoolExecutor);
                completableFutures.add(completableFuture);
            }
            // 全部执行
//            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()])).join();
            for (CompletableFuture completableFuture : completableFutures) {
                completableFuture.join();
            }
            //刷新流
            excelWriter.finish();

            System.out.println("本次导出execl任务执行完毕,共消耗：  " + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        outputStream.flush();
        response.getOutputStream().close();
    }

    /**
     *
     * 单线程实现百万导出到excel 50s优化到11s
     * @param response
     * @throws IOException
     */
    @GetMapping("/newExport")
    public void newExport(HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        long start = System.currentTimeMillis();
        System.out.println("-----------任务执行开始-----------");
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");// 防止下载的文件名字乱码
        try {
            // 文件以附件形式下载
            response.setHeader("Content-disposition",
                    "attachment;filename=down_" + URLEncoder.encode(System.currentTimeMillis() + ".xlsx", "utf-8"));
            //获取总数据量（myBatiesPlus查询表总行数）
            int count = userService.count();
            //每页多少个
            int pageSize = 1000000;
            //必须放到循环外，否则会刷新流
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
            //先查询第一页（没有id则不加条件实现查询第一页（限制查询条数为pageSize））
            List<UserEntity> exportList = userService.findNewPage(null, pageSize);
            if (CollectionUtils.isEmpty(exportList)) {
                throw new RuntimeException("数据库没有数据");
            }
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "用户" + 1).head(UserEntity.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            // 阿里巴巴easyexcel的ExcelWriter写入查询出来的一页数据作为sheet页到磁盘
            excelWriter.write(exportList, writeSheet);

            UserEntity userEntity = exportList.get(exportList.size() - 1);
            // 已写入第一页        // 总行数/一页行数+1多循环一次（最后一次可能没有数据）
            for (int i = 1; i < (count / pageSize) + 1; i++) {
                // 因为这里的id有序，拿到最后一个id用于查询，查询id后的数据（一页数据）WHERE id > #{id}
                exportList = userService.findNewPage(userEntity.getId(), pageSize);
                if (CollectionUtils.isEmpty(exportList)) {
                    break;
                }
                // 索引i从1开始
                writeSheet = EasyExcel.writerSheet(i, "用户" + (i + 1)).head(UserEntity.class)
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
                excelWriter.write(exportList, writeSheet);
                // 重复设置循环再取id
                userEntity = exportList.get(exportList.size() - 1);
            }
            //刷新流
            excelWriter.finish();

            System.out.println("本次导出execl任务执行完毕,共消耗：  " + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        outputStream.flush();
        response.getOutputStream().close();
    }


    @Autowired
    private UserService userService;



    /**
     * 从excel导入100万数据到mysql
     *
     * 1、首先是分批读取Excel中的100w数据，
     * 这一点EasyExcel有自己的解决方案 我是用的20w；
     *
     * 2、其次就是往DB里插入，怎么去插入这20w条数据，
     * 当然不能一条一条的循环，应该批量插入这20w条数据，
     * 同样也不能使用Mybatis的批量插入，因为效率也低。
     *
     * 3、使用JDBC+事务的批量操作将数据插入到数据库。
     *
     * （分批读取+JDBC分批插入+手动事务控制）
     *
     *
     */
    @GetMapping("/importExecel")
    public void importExecel() throws IOException {
        String fileName = "D:\\down_1674181635510.xlsx";
        //记录开始读取Excel时间,也是导入程序开始时间
        long startReadTime = System.currentTimeMillis();
        log.info("------开始读取Excel的Sheet时间(包括导入数据过程):" + startReadTime + "ms------");
        //读取所有Sheet的数据.每次读完一个Sheet就会调用这个方法
        EasyExcel.read(fileName, new EasyExceGeneralDatalListener(userService)).doReadAll();
        long endReadTime = System.currentTimeMillis();
        log.info("------结束读取耗时" + (endReadTime-startReadTime) + "ms------");
    }






}
