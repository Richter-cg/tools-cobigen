/*******************************************************************************
 * Copyright © Capgemini 2013. All rights reserved.
 ******************************************************************************/
package com.capgemini.cobigen.javaplugin.merger.libextension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.capgemini.cobigen.javaplugin.util.ParserUtil;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

/**
 * The class <code>CustomModelWriterTest</code> contains tests for the class {@link CustomModelWriter}
 * 
 * @author mbrunnli (12.04.2013)
 * 
 */
public class CustomModelWriterTest {

    /**
     * Root path to all resources used in this test case
     */
    private static String testFileRootPath = "src/test/resources/CustomModelWriterTest/";

    /**
     * Run the ModelWriter writeField(JavaField) method test
     * 
     * @throws FileNotFoundException
     */
    @Test
    public void testWriteField() throws FileNotFoundException {

        File baseFile = new File(testFileRootPath + "ClassFile_field.java");
        CustomModelWriter target = new CustomModelWriter();
        JavaField field = ParserUtil.getJavaClass(new FileReader(baseFile)).getFieldByName("baseFieldUndefined");
        target.writeField(field);
        Assert.assertEquals("private int baseFieldUndefined;", target.getBuffer().toString().trim());
    }

    /**
     * Tests whether the header will be rewritten after parsing and printing with QDox
     * 
     * @throws IOException
     * @author mbrunnli (12.04.2013)
     */
    @Test
    @Ignore("Not yet implemented --> QDox ignores comments while parsing")
    public void testExistenceOfHeader() throws IOException {

        File file = new File(testFileRootPath + "ClassFile_header.java");
        CustomModelWriter target = new CustomModelWriter();
        JavaClass parsedClass = ParserUtil.getJavaClass(new FileReader(file));
        target.writeSource(parsedClass.getSource());
        Assert.assertTrue(target.getBuffer().toString().contains("/* HEADER */"));
    }

    /**
     * Tests whether generics will be rewritten after parsing and printing with QDox
     * 
     * @throws IOException
     * @author mbrunnli (12.04.2013)
     */
    @Test
    public void testCorrectlyWrittenGenerics() throws IOException {

        File file = new File(testFileRootPath + "ClassFile_generics.java");
        CustomModelWriter target = new CustomModelWriter();
        JavaClass parsedClass = ParserUtil.getJavaClass(new FileReader(file));
        target.writeClass(parsedClass);

        Assert.assertTrue(target.getBuffer().toString().contains("class Clazz<T extends Object, Z extends Clazz>"));
        Assert.assertTrue(target.getBuffer().toString().contains("Map<String,T>"));
        Assert.assertTrue(target.getBuffer().toString().contains("private T t;"));
        Assert.assertTrue(target.getBuffer().toString().contains("public T get()"));
        Assert.assertTrue(target.getBuffer().toString().contains("public <U extends Number> void inspect(U u)"));
    }

    /**
     * Tests whether modifiers of classes, fields, methods and method parameters are written correctly
     * 
     * @throws FileNotFoundException
     * @author mbrunnli (17.06.2013)
     */
    @Test
    // @Ignore("Not yet implemented --> QDox defect")
    public void testCorrectlyWrittenModifiers() throws FileNotFoundException {

        File file = new File(testFileRootPath + "ClassFile_modifiers.java");
        CustomModelWriter target = new CustomModelWriter();
        JavaClass parsedClass = ParserUtil.getJavaClass(new FileReader(file));
        target.writeClass(parsedClass);

        Assert.assertTrue(target.getBuffer().toString().contains("public final class FooBar"));
        Assert.assertTrue(target.getBuffer().toString()
                .contains("private static final volatile int baseFieldUndefined"));
        Assert.assertTrue(target.getBuffer().toString().contains("public final void method1(String parameter)"));
        Assert.assertTrue(target.getBuffer().toString().contains("method2(final String parameter)"));
    }

}
