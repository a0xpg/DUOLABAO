package com.hualong.duolabao.aop;

import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2018-08-14.
 * Spring boot Aop  切面
 */
@Aspect
@Component   //声明组件
public class LogAspect implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.hualong.duolabao.conntroller.*.*(..))")
    public void webLog(){
        System.out.println("LogAspect : " +Thread.currentThread().getId());
    }

    //第二步
    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        System.out.println("URL : " + request.getRequestURL().toString());
        System.out.println("HTTP_METHOD : " + request.getMethod());
        System.out.println("IP : " + request.getRemoteAddr());
        System.out.println("CLASS_METHOD CX: " + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        System.out.println("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        System.out.println("joinPoint.toString() :"+joinPoint.toString());
        //joinPoint.toString() :execution(String com.hlyf.controller.ScanConntroller.getToken(String,String)) 答案
        //日志打印  全局处理
        logger.info(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()+":"+Arrays.toString(joinPoint.getArgs()));

//        HttpServletResponse response = attributes.getResponse();
//        String classType = joinPoint.getTarget().getClass().getName();
//        Class<?> clazz = Class.forName(classType);
//        String clazzName = clazz.getName();
//        String methodName = joinPoint.getSignature().getName(); //获取方法名称
//        Object[] args = joinPoint.getArgs();//参数
//        Map<String, Object> nameAndArgs = getFieldsName(this.getClass(), clazzName, methodName, args);//获取被切参数名称及参数值
//        System.out.println(nameAndArgs.toString());




    }

    //最后执行
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("方法的返回值 : " +(String) ret);
        System.out.println("方法的返回值 : " + ret);
    }
    //后置异常通知
    @AfterThrowing("webLog()")
    public void throwss(JoinPoint jp){
        System.out.println("方法异常时执行.....");
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @After("webLog()")
    public void after(JoinPoint jp){
        System.out.println("方法最后执行.....");//第四步
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("webLog()")
    public Object arround(ProceedingJoinPoint pjp) {
        System.out.println("方法环绕start.....");//第一步
        try {
            Object o =  pjp.proceed();
            System.out.println("方法环绕proceed，结果是 :" + o);//第三步
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }




    /**
     * 通过反射机制 获取被切参数名以及参数值
     *
     * @param cls
     * @param clazzName
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    private Map<String, Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(this.getClass());
       // ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        // String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attr.variableName(i + pos), args[i]);//paramNames即参数名
        }
        return map;
    }

}
