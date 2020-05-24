/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.entity;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author namhcn
 */
public class HReqParam {

    private static String _getParameterAsString(HttpServletRequest req, String paramName) throws Exception {
        if (paramName == null) {
            throw new Exception("Parameter name is null");
        }
        paramName = paramName.trim();
        if (paramName.isEmpty()) {
            throw new Exception("Parameter name is empty");
        }

        String strVal = req.getParameter(paramName);
        if (strVal == null) {
            throw new Exception();
        }
        return strVal;
    }

    private static String _getParameterAsTrimString(HttpServletRequest req, String paramName) throws Exception {
        return _getParameterAsString(req, paramName).trim();
    }

    private static Boolean parseBoolean(String strVal) {
        if (strVal != null) {
            strVal = strVal.trim();
            if (strVal.equalsIgnoreCase("true")) {
                return true;
            } else if (strVal.equalsIgnoreCase("false")) {
                return false;
            } else if (strVal.equalsIgnoreCase("yes")) {
                return true;
            } else if (strVal.equalsIgnoreCase("no")) {
                return false;
            } else if (strVal.equalsIgnoreCase("on")) {
                return true;
            } else if (strVal.equalsIgnoreCase("off")) {
                return false;
            } else if (strVal.equalsIgnoreCase("1")) {
                return true;
            } else if (strVal.equalsIgnoreCase("0")) {
                return false;
            }
        }
        return null;
    }

    public static Boolean getBoolean(HttpServletRequest req, String paramName) throws Exception {
        String strVal = _getParameterAsString(req, paramName);

        Boolean ret = parseBoolean(strVal);
        if (ret != null) {
            return ret.booleanValue();
        } else {
            throw new NumberFormatException("Wrong format while parsing boolean");
        }
    }

    public static Byte getByte(HttpServletRequest req, String paramName) throws Exception {
        return Byte.parseByte(_getParameterAsTrimString(req, paramName));
    }

    public static Double getDouble(HttpServletRequest req, String paramName) throws Exception {
        return Double.parseDouble(_getParameterAsTrimString(req, paramName));
    }

    public static Float getFloat(HttpServletRequest req, String paramName) throws Exception {
        return Float.parseFloat(_getParameterAsTrimString(req, paramName));
    }

    public static Integer getInt(HttpServletRequest req, String paramName) throws Exception {
        return Integer.parseInt(_getParameterAsTrimString(req, paramName));
    }

    public static Long getLong(HttpServletRequest req, String paramName) throws Exception {
        return Long.parseLong(_getParameterAsTrimString(req, paramName));
    }

    public static Short getShort(HttpServletRequest req, String paramName) throws Exception {
        return Short.parseShort(_getParameterAsTrimString(req, paramName));
    }

    public static String getString(HttpServletRequest req, String paramName) throws Exception {
        return _getParameterAsString(req, paramName);
    }
}
