package com.proactivity.groovy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.script.ScriptEngine;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proactivity.groovy.exception.GroovyInvocationException;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;

public class GroovyManager {

	static final Logger LOG = LoggerFactory.getLogger(GroovyManager.class);

//	private final GroovyClassLoader loader;
//	private final GroovyShell shell;
//	private final GroovyScriptEngine engine;
//	private final ScriptEngine engineFromFactory;
//	private final String groovyScriptsDir;
	
	static final GroovyClassLoader classLoader = new GroovyClassLoader();

	public GroovyManager(String groovyScriptsDir) {

//		this.groovyScriptsDir = groovyScriptsDir;
//
//		loader = new GroovyClassLoader(this.getClass().getClassLoader());
//		shell = new GroovyShell(loader, new Binding());
//
//		URL url = null;
//		try {
//			url = new File(this.groovyScriptsDir).toURI().toURL();
//		} catch (MalformedURLException e) {
//			LOG.error("Exception while creating url", e);
//		}
//		engine = new GroovyScriptEngine(new URL[] { url }, this.getClass().getClassLoader());
//		engineFromFactory = new GroovyScriptEngineFactory().getScriptEngine();

	}
	
//	public Object invoke(String scriptName, String scriptMethod, Object parameters) {
//
//		try {
//
//			LOG.debug("Executing " + scriptName + " - " + scriptMethod + "(" + parameters + ")");
//			Script script = shell.parse(new File(this.groovyScriptsDir, scriptName));
//			Object result = script.invokeMethod(scriptMethod, parameters);
//			LOG.debug("Result of  " + scriptName + " - " + scriptMethod + "(" + parameters + ") method is: " + result);
//
//			return result;
//
//		} catch (CompilationFailedException | IOException e) {
//			throw new GroovyInvocationException(e);
//		}
//	}
//
//	public Object invoke(String scriptName, String scriptMethod, Object[] parameters) {
//
//		try {
//
//			LOG.debug("Executing " + scriptName + " - " + scriptMethod + "(" + parameters + ")");
//			Script script = shell.parse(new File(this.groovyScriptsDir, scriptName));
//			Object result = script.invokeMethod(scriptMethod, parameters);
//			LOG.debug("Result of  " + scriptName + " - " + scriptMethod + "(" + parameters + ") method is: " + result);
//
//			return result;
//
//		} catch (CompilationFailedException | IOException e) {
//			throw new GroovyInvocationException(e);
//		}
//	}
//	
	public Object invokeClass(String className, String scriptMethod, Object[] parameters) {

		try {

			LOG.debug("Executing " + className + " - " + scriptMethod + "(" + parameters + ")");
			Class groovy = classLoader.parseClass(new File(className));
		    GroovyObject groovyObj = (GroovyObject) groovy.getDeclaredConstructor().newInstance();
			Object result = groovyObj.invokeMethod(scriptMethod, parameters);
			LOG.debug("Result of  " + className + " - " + scriptMethod + "(" + parameters + ") method is: " + result);

			return result;

		} catch (CompilationFailedException | IOException | 
				InstantiationException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException | 
				NoSuchMethodException | SecurityException e) {
			throw new GroovyInvocationException(e);
		}
	}
	
	public Object invokeClass(String className, String scriptMethod, Object parameters) {

		try {

			LOG.debug("Executing " + className + " - " + scriptMethod + "(" + parameters + ")");
			Class groovy = classLoader.parseClass(new File(className));
		    GroovyObject groovyObj = (GroovyObject) groovy.getDeclaredConstructor().newInstance();
			Object result = groovyObj.invokeMethod(scriptMethod, parameters);
			LOG.debug("Result of  " + className + " - " + scriptMethod + "(" + parameters + ") method is: " + result);

			return result;

		} catch (CompilationFailedException | IOException | 
				InstantiationException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException | 
				NoSuchMethodException | SecurityException e) {
			throw new GroovyInvocationException(e);
		}
	}

//	public Object run(String scriptName) {
//		
//		try {
//			
//			LOG.debug("Executing script " + scriptName + " run method");
//			Script script = shell.parse(new File(this.groovyScriptsDir, scriptName));			
//			Object result = script.run();
//			LOG.debug("Result of " + scriptName + " run method is :" + result);
//						
//			return result;
//
//		} catch (CompilationFailedException | IOException e) {
//			throw new GroovyInvocationException(e);
//		}
//	}
}
