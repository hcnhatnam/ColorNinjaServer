/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.entity;

/**
 *
 * @author namhcn
 */
public class ResultObjectInstance {

    public static ResultObject UNKNOW_ERROR = new ResultObject(-1, "unknow error");
    public static ResultObject EXCEPTION = new ResultObject(-2, "exception");
    public static ResultObject MISSING_PARAM = new ResultObject(-3, "param missing");
    public static ResultObject USER_EXSISTED = new ResultObject(-4, "user exsisted");
    public static ResultObject USER_NOT_EXSISTED = new ResultObject(-5, "user not exsist");

}
