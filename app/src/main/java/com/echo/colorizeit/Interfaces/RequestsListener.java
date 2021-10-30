package com.echo.colorizeit.Interfaces;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public interface RequestsListener {
    void success(String data);
    void failure(String info);
}
