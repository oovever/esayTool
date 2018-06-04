package com.Oovever.esayTool.io;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * @author OovEver
 * 2018/6/5 0:43
 */
public class IORuntimeExceptionTest {

    @Test
    public void getTrace(){
        try {
            File f= new File("d:/LOL.exe");
            new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new IORuntimeException(e);
        }
    }
}