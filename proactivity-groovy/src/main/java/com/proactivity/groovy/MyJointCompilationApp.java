package com.proactivity.groovy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.script.ScriptEngine;

import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import groovy.lang.*;

import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class MyJointCompilationApp {
	
	static final Logger LOG = LoggerFactory.getLogger(MyJointCompilationApp.class);
//    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(MyJointCompilationApp.class);
    private final GroovyClassLoader loader;
    private final GroovyShell shell;
    private final GroovyScriptEngine engine;
    private final ScriptEngine engineFromFactory;

    public MyJointCompilationApp() {
        loader = new GroovyClassLoader(this.getClass().getClassLoader());
        shell = new GroovyShell(loader, new Binding());

        URL url = null;
        try {
            url = new File("src/main/groovy/").toURI().toURL();
        } catch (MalformedURLException e) {
            LOG.error("Exception while creating url", e);
        }
        engine = new GroovyScriptEngine(new URL[] {url}, this.getClass().getClassLoader());
        engineFromFactory = new GroovyScriptEngineFactory().getScriptEngine();
    }

//    private void addWithCompiledClasses(int x, int y) {
//        LOG.info("Executing {} + {}", x, y);
//        Object result1 = new CalcScript().calcSum(x, y);
//        LOG.info("Result of CalcScript.calcSum() method is {}", result1);
//
//        Object result2 = new CalcMath().calcSum(x, y);
//        LOG.info("Result of CalcMath.calcSum() method is {}", result2);
//    }

    private void addWithGroovyShell(int x, int y) throws IOException {
        Script script = shell.parse(new File("src/main/groovy/", "CalcScript.groovy"));
        LOG.info("Executing {} + {}", x, y);
        Object result = script.invokeMethod("calcSum", new Object[] { x, y });
        LOG.info("Result of CalcScript.calcSum() method is {}", result);
        System.out.println("Result of CalcScript.run() method is :" + result);
    }

    private void addWithGroovyShellRun() throws IOException {
        Script script = shell.parse(new File("src/main/groovy/", "CalcScript.groovy"));
        LOG.info("Executing script run method");
        Object result = script.run();
        LOG.info("Result of CalcScript.run() method is {}", result);
        System.out.println("Result of CalcScript.run() method is :" + result);
    }

    private void addWithGroovyClassLoader(int x, int y) throws IllegalAccessException, InstantiationException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class calcClass = loader.parseClass(
          new File("src/main/groovy/", "CalcMath.groovy"));
        GroovyObject calc = (GroovyObject) calcClass.getDeclaredConstructor().newInstance();
        Object result = calc.invokeMethod("calcSum", new Object[] { x + 14, y + 14 });
        LOG.info("Result of CalcMath.calcSum() method is {}", result);
        System.out.println("Result of CalcMath.calcSum() method is :" + result);
    }

    private void addWithGroovyScriptEngine(int x, int y) throws IllegalAccessException,
      InstantiationException, ResourceException, ScriptException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<GroovyObject> calcClass = engine.loadScriptByName("CalcMath.groovy");
        GroovyObject calc = calcClass.getDeclaredConstructor().newInstance();
        //WARNING the following will throw a ClassCastException
        //((CalcMath)calc).calcSum(1,2);
        Object result = calc.invokeMethod("calcSum", new Object[] { x, y });
        LOG.info("Result of CalcMath.calcSum() method is {}", result);
        System.out.println("Result of CalcMath.calcSum() method is :" + result);
    }

    private void addWithEngineFactory(int x, int y) throws IllegalAccessException,
      InstantiationException, javax.script.ScriptException, FileNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class calcClass = (Class) engineFromFactory.eval(
          new FileReader(new File("src/main/groovy/", "CalcMath.groovy")));
        GroovyObject calc = (GroovyObject) calcClass.getDeclaredConstructor().newInstance();
        Object result = calc.invokeMethod("calcSum", new Object[] { x, y });
        LOG.info("Result of CalcMath.calcSum() method is {}", result);
        System.out.println("Result of CalcMath.calcSum() method is :" + result);
    }

//    private void addWithStaticCompiledClasses() {
//        LOG.info("Running the Groovy classes compiled statically...");
//        addWithCompiledClasses(5, 10);
//
//    }

    private void addWithDynamicCompiledClasses() throws IOException, IllegalAccessException, InstantiationException,
      ResourceException, ScriptException, javax.script.ScriptException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        LOG.info("Invocation of a dynamic groovy script...");
        addWithGroovyShell(5, 10);
        LOG.info("Invocation of the run method of a dynamic groovy script...");
        addWithGroovyShellRun();
        LOG.info("Invocation of a dynamic groovy class loaded with GroovyClassLoader...");
        addWithGroovyClassLoader(10, 30);
        LOG.info("Invocation of a dynamic groovy class loaded with GroovyScriptEngine...");
        addWithGroovyScriptEngine(15, 0);
        LOG.info("Invocation of a dynamic groovy class loaded with GroovyScriptEngine JSR223...");
        addWithEngineFactory(5, 6);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException,
      ResourceException, ScriptException, IOException, javax.script.ScriptException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        MyJointCompilationApp myJointCompilationApp = new MyJointCompilationApp();
        LOG.info("Example of addition operation via Groovy scripts integration with Java.");
        //myJointCompilationApp.addWithStaticCompiledClasses();
        myJointCompilationApp.addWithDynamicCompiledClasses();
    }
}
