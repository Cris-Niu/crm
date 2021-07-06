package com.xxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.exception.NoLoginException;
import com.xxx.crm.exception.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理
 */
@Component
public class GlobalExceptionHandler extends ExceptionHandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception e) {
        //判断是否登录,未登录的话返回登录页面
        if(e instanceof NoLoginException){
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }



        ModelAndView mv = new ModelAndView();

        //设置默认异常处理
        mv.addObject("msg","系统正忙,请重试");
        mv.addObject("code","400");

        //
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            ResponseBody responseBody = hm.getMethod().getAnnotation(ResponseBody.class);

            //存在返回的结果:视图
            if(null == responseBody){
                //参数异常
                if(e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    mv.addObject("code",pe.getCode());
                    mv.addObject("msg",pe.getMsg());
                }
                return mv;

                //返回json,就没法用ModelAndView
            }else{

                //用resultInfo设置默认异常
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(400);
                resultInfo.setMsg("系统正忙,请重试");

                //参数异常
                if(e instanceof  ParamsException){
                    ParamsException pe = (ParamsException) e;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }

                resp.setContentType("application/json;charset=utf-8");
                PrintWriter writer = null;
                try {
                    writer = resp.getWriter();
                    writer.write(JSON.toJSONString(resultInfo));
                    writer.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    if(writer != null){
                        writer.close();
                    }
                }
            }
            return null;
        }
        return mv;
    }
}
