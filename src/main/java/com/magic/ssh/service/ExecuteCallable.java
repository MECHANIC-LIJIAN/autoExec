package com.magic.ssh.service;

public interface ExecuteCallable<T> {

    boolean IamDone();//在达到一定条件的时候，通过IamDone通知上述程序结束读取。

    ExecuteCallable<T> appendBuffer(String content);//异步追加输出到自定义的Buffer

    String endBuffer();//正常结束Buffer，
}