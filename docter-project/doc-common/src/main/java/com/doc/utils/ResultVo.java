package com.doc.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
//封装返回结果的类
@Data
@AllArgsConstructor
public class ResultVo<T> {
    private String msg;
    private int code;
    private T data;
}
