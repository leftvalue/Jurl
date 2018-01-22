package net;

import model.Request;

/**
 * @author linxi
 * www.leftvalue.top
 * net
 * Date 22/11/2017 7:50 PM
 */
public abstract class BaseNetTool {
    protected Request request;

    public BaseNetTool(Request request) {
        this.request = request;
    }
}
