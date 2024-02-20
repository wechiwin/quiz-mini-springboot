// package com.moggi.quizmini.servlet;
//
// import com.atguigu.bean.Book;
// import com.atguigu.service.BookService;
// import com.atguigu.service.impl.BookServiceImpl;
// import com.atguigu.servlet.base.ViewBaseServlet;
//
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.util.List;
//
// /**
//  * @author Leevi
//  * 日期2021-05-14  09:03
//  * 该Servlet只需要处理访问首页
//  */
// public class PortalServlet extends ViewBaseServlet {
//     private BookService bookService = new BookServiceImpl();
//     @Override
//     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//         doGet(request, response);
//     }
//
//     @Override
//     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//         try {
//             //查询动态数据
//             List<Book> bookList = bookService.getBookList();
//             //将动态数据存储到请求域
//             request.setAttribute("bookList",bookList);
//             processTemplate("index",request,response);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
