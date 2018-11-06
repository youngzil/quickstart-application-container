/**
 * 项目名称：quickstart-jersey 
 * 文件名：MyApplication.java
 * 版本信息：
 * 日期：2018年11月5日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.container.jersey.http;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * MyApplication
 * 
 * @author：yangzl@asiainfo.com
 * @2018年11月5日 下午9:56:37
 * @since 1.0
 */
@ApplicationPath("/")
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        // register(LoggingFilter.class);
    }

}
