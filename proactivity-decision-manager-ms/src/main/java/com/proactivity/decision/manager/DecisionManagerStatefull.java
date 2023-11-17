package com.proactivity.decision.manager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.jsfr.json.JsonSurfer;
import org.jsfr.json.compiler.JsonPathCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieContainerSessionsPool;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.proactivity.decision.manager.dto.DecisionData;
import com.proactivity.decision.manager.dto.DecisionResult;
import com.proactivity.decision.manager.exception.DecisionManagerException;
import com.proactivity.decision.manager.exception.DecisionManagerPathNotFoudException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class DecisionManagerStatefull {

	static final Logger logger = LoggerFactory.getLogger(DecisionManagerStatefull.class);

	public static String SEARCH_TYPE_OBJECT = "OBJECT";
	public static String SEARCH_TYPE_ARRAY = "ARRAY";
	
	private JsonSurfer mainSurfer;
	
	private KieSession kSession;
	
	private String jsonPath;
	private String jsonType;
	private String searchKey;
	private String rule;	
	
	public DecisionManagerStatefull(String jsonPath,
			String jsonType, String searchKey, String rule) {
		super();	
		this.kSession = kieSession(Arrays.asList(rule));		
		this.jsonPath = jsonPath;
		this.jsonType = jsonType;
		this.searchKey = searchKey;
		this.rule = rule;
	}

	public DecisionData retrieve(String json) {
		
		// private final ObjectMapper objectMapper; // se ho bisogno di accedere a Jackson
		org.jsfr.json.path.JsonPath JSON_PATH = JsonPathCompiler.compile(jsonPath);
	
		DecisionData dData = new DecisionData();
	
		mainSurfer.configBuilder()
					.bind(JSON_PATH, (value, context) -> dData.setData(value.toString()))	// value è un JsonNode
					.buildAndSurf(json);
					
		return dData;
	}
	
	public Boolean check(Object obj)	throws DecisionManagerException {	
		
		try {
			
			Object result = retrieveResult(new DecisionResult(), obj, Arrays.asList(rule));

			if (result != null) {
				return true;
			}				
		
		} catch (Exception ex) {
			throw new DecisionManagerException("<!!!!!!>", ex);
		}
		return false;
	}	
	
	public Boolean analyze(Object obj)	throws DecisionManagerException {
		
		try {

			logger.debug("<@@@@ - INPUT OBJ  -> " + obj + " - @@@>");

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String jsonString = ow.writeValueAsString(obj);

//			Gson gson = new Gson();
//			String jsonString = gson.toJson(obj);

			logger.debug("@@@@ - INPUT OBJ to json -> " + jsonString + " - @@@>");

			JSONObject jsonObj = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(jsonString);

			if (SEARCH_TYPE_OBJECT.equals(jsonType)) {

				try {
					JSONObject jsonObjValue = JsonPath.read(jsonObj, jsonPath);
					DecisionData fact = new DecisionData(jsonObjValue.get(searchKey));
					Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
	
					if (result != null) {
						return true;
					}
				}catch(PathNotFoundException pnfe) {
					logger.warn("### - JSON PATH NON VALIDO -> " + jsonPath + " - ###>");
					throw new DecisionManagerPathNotFoudException(pnfe);
				};

			} else if (SEARCH_TYPE_ARRAY.equals(jsonType)) {
							
				try {

					JSONArray jsonArray = JsonPath.read(jsonObj, jsonPath);
	
					for (Object arrEl : jsonArray) {
						DecisionData fact = null;
						if (arrEl instanceof JSONObject) {
							Object data = ((JSONObject) arrEl).get(searchKey);
							if (data != null) {
								fact = new DecisionData(data);
								Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
								if (result != null) {
									return true;
								}
							}
						} else {
							fact = new DecisionData(arrEl);
							Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
							if (result != null) {
								return true;
							}
						}
					}
				}catch(PathNotFoundException pnfe) {
					logger.warn("### - JSON PATH NON VALIDO -> " + jsonPath + " - ###>");
					throw new DecisionManagerPathNotFoudException(pnfe);
				};
			}
		} catch (Exception ex) {
			throw new DecisionManagerException("<!!!!!!>", ex);
		}
		return false;
	}

	public Object modify(Object obj) throws DecisionManagerException {
		
		try {

			logger.debug("<@@@@ - INPUT OBJ  -> " + obj + " - @@@>");

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String jsonString = ow.writeValueAsString(obj);

//			Gson gson = new Gson();
//			String jsonString = gson.toJson(obj);

			logger.debug("@@@@ - INPUT OBJ to json -> " + jsonString + " - @@@>");

			JSONObject jsonObj = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(jsonString);

			if (SEARCH_TYPE_OBJECT.equals(jsonType)) {
				
				try {
					JSONObject jsonObjValue = JsonPath.read(jsonObj, jsonPath);
					DecisionData fact = new DecisionData(jsonObjValue.get(searchKey));
					Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
	
					if (result != null) {
						jsonObjValue.replace(searchKey, result);
					}
				}catch(PathNotFoundException pnfe) {
					logger.warn("### - JSON PATH NON VALIDO -> " + jsonPath + " - ###>");
					throw new DecisionManagerPathNotFoudException(pnfe);
				};

			} else if (SEARCH_TYPE_ARRAY.equals(jsonType)) {
				
				try {

					JSONArray jsonArray = JsonPath.read(jsonObj, jsonPath);
	
					Object toRemove = null;
					Object toAdd = null;
					
					for (Object arrEl : jsonArray) {
						DecisionData fact = null;
						if (arrEl instanceof JSONObject) {
							Object data = ((JSONObject) arrEl).get(searchKey);
							if (data != null) {
								fact = new DecisionData(data);
								Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
								if (result != null) {
									((JSONObject) arrEl).replace(searchKey, result);
								}
							}
						} else {
							fact = new DecisionData(arrEl);
							Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
							if (result != null) {
								toRemove = arrEl;
								toAdd = result;
							}
						}
					}
					if (toRemove != null && toAdd != null) {
						jsonArray.remove(toRemove);
						jsonArray.add(toAdd);
						jsonObj.put(searchKey, jsonArray);
					}
				}catch(PathNotFoundException pnfe) {
					logger.warn("### - JSON PATH NON VALIDO -> " + jsonPath + " - ###>");
					throw new DecisionManagerPathNotFoudException(pnfe);
				};
			}
			logger.debug("@@@@ - OUTPUT JSON -> " + jsonObj.toJSONString() + " - @@@>");
			//return gson.fromJson(jsonObj.toJSONString(), obj.getClass());
			return mapper.readValue(jsonObj.toJSONString(), obj.getClass());

		} catch (Exception ex) {
			throw new DecisionManagerException("<!!!!!!>", ex);
		}
	}
	
	public Object modify(String jsonString) throws DecisionManagerException {
		
		try {		

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();		

//			Gson gson = new Gson();
//			String jsonString = gson.toJson(obj);

			logger.debug("@@@@ - INPUT OBJ to json -> " + jsonString + " - @@@>");

			JSONObject jsonObj = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(jsonString);

			if (SEARCH_TYPE_OBJECT.equals(jsonType)) {
				
				try {
					JSONObject jsonObjValue = JsonPath.read(jsonObj, jsonPath);
					DecisionData fact = new DecisionData(jsonObjValue.get(searchKey));
					Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
	
					if (result != null) {
						jsonObjValue.replace(searchKey, result);
					}
				}catch(PathNotFoundException pnfe) {
					logger.warn("### - JSON PATH NON VALIDO -> " + jsonPath + " - ###>");
					throw new DecisionManagerPathNotFoudException(pnfe);
				};

			} else if (SEARCH_TYPE_ARRAY.equals(jsonType)) {
				
				try {

					JSONArray jsonArray = JsonPath.read(jsonObj, jsonPath);
	
					Object toRemove = null;
					Object toAdd = null;
					
					for (Object arrEl : jsonArray) {
						DecisionData fact = null;
						if (arrEl instanceof JSONObject) {
							Object data = ((JSONObject) arrEl).get(searchKey);
							if (data != null) {
								fact = new DecisionData(data);
								Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
								if (result != null) {
									((JSONObject) arrEl).replace(searchKey, result);
								}
							}
						} else {
							fact = new DecisionData(arrEl);
							Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
							if (result != null) {
								toRemove = arrEl;
								toAdd = result;
							}
						}
					}
					if (toRemove != null && toAdd != null) {
						jsonArray.remove(toRemove);
						jsonArray.add(toAdd);
						jsonObj.put(searchKey, jsonArray);
					}
				}catch(PathNotFoundException pnfe) {
					logger.warn("### - JSON PATH NON VALIDO -> " + jsonPath + " - ###>");
					throw new DecisionManagerPathNotFoudException(pnfe);
				};
			}
			logger.debug("@@@@ - OUTPUT JSON -> " + jsonObj.toJSONString() + " - @@@>");
			//return gson.fromJson(jsonObj.toJSONString(), obj.getClass());
			return mapper.readValue(jsonObj.toJSONString(), jsonString.getClass());

		} catch (Exception ex) {
			throw new DecisionManagerException("<!!!!!!>", ex);
		}
	}
	
	public Object route(Object obj) throws DecisionManagerException {
		
		try {
			
			Object result = retrieveResult(new DecisionResult(), obj, Arrays.asList(rule));

			if (result != null) {
				return result;
			}				
		
		} catch (Exception ex) {
			throw new DecisionManagerException("<!!!!!!>", ex);
		}
		return false;
	}
	
	
	public Object retrieveResult(DecisionResult decisionResult, Object data, List<String> rules) {
		

		kSession.setGlobal("decisionResult", decisionResult);
		kSession.insert(data);
		kSession.fireAllRules();
		//kSession.dispose();Invalida la sessione

		return decisionResult.getResult();
	}
	
	
	public void test(List<String> rules) {
		
		KieServices kServices = KieServices.Factory.get();

		KieFileSystem kfs = kServices.newKieFileSystem();
		KieRepository kr = kServices.getRepository();
		
		for (String rule : rules) {
			File file = new File(rule);
			Resource risorsa = kServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
			kfs.write(risorsa);
		}

		KieBuilder kb = kServices.newKieBuilder(kfs);
		kb.buildAll();
		KieContainer kContainer = kServices.newKieContainer(kr.getDefaultReleaseId());
		
		// Obtain a KIE session pool from the KIE container
				KieContainerSessionsPool pool = kContainer.newKieSessionsPool(10);

				// Create KIE sessions from the KIE session pool
				KieSession kSession = pool.newKieSession();
	}

	private KieSession kieSession(List<String> rules) {
		
		

		KieServices kServices = KieServices.Factory.get();

		KieFileSystem kfs = kServices.newKieFileSystem();
		KieRepository kr = kServices.getRepository();
		
		for (String rule : rules) {
			File file = new File(rule);
			Resource risorsa = kServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
			kfs.write(risorsa);
		}

		KieBuilder kb = kServices.newKieBuilder(kfs);
		kb.buildAll();
		KieContainer kContainer = kServices.newKieContainer(kr.getDefaultReleaseId());
		KieSession kSession = kContainer.newKieSession();
		return kSession;
	}
	
	
}
