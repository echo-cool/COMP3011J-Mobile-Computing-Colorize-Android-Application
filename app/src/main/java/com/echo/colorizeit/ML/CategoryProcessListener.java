package com.echo.colorizeit.ML;

import org.tensorflow.lite.support.label.Category;

import java.util.List;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/31-16:00
 * @Project: My Application
 * @Package: com.echo.photo_editor.ML
 * @Description:
 **/
public interface CategoryProcessListener {
    void start();
    void success(List<Category> result);
    void failed(String message);
    void complete();
}
