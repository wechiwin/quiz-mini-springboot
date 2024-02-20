package com.moggi.quizmini.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 将数据以Excel的格式写入输出流
 * EasyExcel参考文档：https://easyexcel.opensource.alibaba.com/docs/current/quickstart/write
 *
 * @author wangrubin
 * @date 2022-08-02
 */
@Slf4j
@Component
public class ExcelExportHandler {

    /**
     * 下载导入的模板
     *
     * @param response response
     * @param fileName 文件名（支持中文）
     * @param clazz    封装数据的POJO
     * @param <T>      数据泛型
     */
    public <T> void downloadTemplate(HttpServletResponse response, String fileName, Class<T> clazz) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet("Sheet1")
                    // 设置单元格宽度自适应
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    // 设置单元格高度和字体
                    .registerWriteHandler(getHeightAndFontStrategy())
                    .doWrite(new ArrayList<>(0));
        } catch (Exception e) {
            // 重置response
            log.error("模板下载失败" + e.getMessage());
            throw new RuntimeException("模板下载失败", e);
        }
    }

    /**
     * 下载Excel格式的数据
     *
     * @param response response
     * @param fileName 文件名（支持中文）
     * @param data     待下载的数据
     * @param clazz    封装数据的POJO
     * @param <T>      数据泛型
     */
    public <T> void export(HttpServletResponse response, String fileName,
                           List<T> data, Class<T> clazz) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet("Sheet1")
                    // 设置单元格宽度自适应
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    // 设置单元格高度和字体
                    .registerWriteHandler(getHeightAndFontStrategy())
                    .doWrite(data);
            log.info("下载{}条记录到文件{}", data.size(), fileName);
        } catch (Exception e) {
            // 重置response
            log.error("文件下载失败" + e.getMessage());
            throw new RuntimeException("下载文件失败", e);
        }
    }

    /**
     * 自定义Excel导出策略，设置表头和数据行的字体和高度
     *
     * @return Excel导出策略
     */
    private HorizontalCellStyleStrategy getHeightAndFontStrategy() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
}