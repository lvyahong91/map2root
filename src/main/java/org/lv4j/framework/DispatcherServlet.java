package org.lv4j.framework;

import org.apache.commons.lang3.ArrayUtils;
import org.lv4j.framework.bean.Data;
import org.lv4j.framework.bean.Handler;
import org.lv4j.framework.bean.Param;
import org.lv4j.framework.bean.View;
import org.lv4j.framework.helper.BeanHelper;
import org.lv4j.framework.helper.ConfigHelper;
import org.lv4j.framework.helper.ControllerHelper;
import org.lv4j.framework.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //1.1 初始化相关的Helper类
        HelperLoader.init();
        //1.2 获取ServletContext对象，用于对策Servlet对象
        ServletContext servletContext=config.getServletContext();
        //1.3 用于注册JSP的Servlet
        ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //1.4 注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet=servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        super.service(request, response);
        //2.1 获取请求方法与请求路径
        String requestMethod=request.getMethod().toLowerCase();
        String requestPath=request.getPathInfo();
        //2.2 获取Action处理器
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if (handler!=null){
            //2.3 获取Controller类及其Bean实例
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);
            //2.4 创建请求参数对象
            Map<String,Object> paramMap=new HashMap<String, Object>();
            Enumeration<String> paramNames=request.getParameterNames();
            while (paramNames.hasMoreElements()){
                //2.5 通过参数名取得参数值
                String paramName=paramNames.nextElement();
                String paramValue=request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            String body= CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String[] params=StringUtil.splitString(body,"&");
                if (ArrayUtils.isNotEmpty(params)){
                    for (String param:params){
                        String[] array=StringUtil.splitString(param,"=");
                        if (ArrayUtils.isNotEmpty(array) && array.length==2){
                            String paramName=array[0];
                            String paramValue=array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param=new Param(paramMap);
            //2.6 调用Action信息
            Method method=handler.getActionMethod();
            Object result= ReflectionUtil.invokeMethod(controllerBean,method,param);
            //2.7 处理Action信息返回值
            if (result instanceof View){
                //2.7.1 返回的是JSP页面
                View view= (View) result;
                String path=view.getPath();
                if (StringUtil.isNotEmpty(path)){
                    if (path.startsWith("/")){
                        response.sendRedirect(request.getContextPath()+path);
                    }else {
                        Map<String,Object> model=view.getModel();
                        for (Map.Entry<String,Object> entry:model.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }
                        request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
                    }
                }
            }else if (result instanceof Data){
                //2.8返回Json数据 ,用于ajax的异步刷新
                Data data= (Data) result;
                Object model=data.getModel();
                if (model!=null){
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer=response.getWriter();
                    String json= JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }

        }
    }
}
