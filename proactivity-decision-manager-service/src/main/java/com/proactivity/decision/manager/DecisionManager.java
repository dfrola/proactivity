package com.proactivity.decision.manager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
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

public class DecisionManager {

	static final Logger logger = LoggerFactory.getLogger(DecisionManager.class);

	public static String SEARCH_TYPE_OBJECT = "OBJECT";
	public static String SEARCH_TYPE_ARRAY = "ARRAY";
	
	public Boolean check(Object obj, String rule)	throws DecisionManagerException {	
		
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
	
	public Boolean analyze(Object obj, String jsonPath, String jsonType, String searchKey, String rule)	throws DecisionManagerException {
		
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

	public Object modify(Object obj, String jsonPath, String jsonType, String searchKey, String rule) throws DecisionManagerException {
		
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
	
	public Object route(Object obj, String rule)	throws DecisionManagerException {
		
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
	
	public Object route(Object obj, String jsonPath, String jsonType, String searchKey, String rule)	throws DecisionManagerException {
		
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
						return result;
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
									return result;
								}
							}
						} else {
							fact = new DecisionData(arrEl);
							Object result = retrieveResult(new DecisionResult(), fact, Arrays.asList(rule));
							if (result != null) {
								return result;
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
		return null;
	}
	
	public Object retrieveResult(DecisionResult decisionResult, Object data, List<String> rules) {
		KieSession kieSession;

		kieSession = kieSession(rules);

		kieSession.setGlobal("decisionResult", decisionResult);
		kieSession.insert(data);
		kieSession.fireAllRules();
		kieSession.dispose();

		return decisionResult.getResult();
	}

	public KieSession kieSession(List<String> rules) {

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
	
	@Deprecated
	public Boolean analyze(Object obj, String jsonPath, String jsonType, String searchKey, List<String> rules)
			throws DecisionManagerException {
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

				JSONObject jsonObjValue = JsonPath.read(jsonObj, jsonPath);
				DecisionData fact = new DecisionData(jsonObjValue.get(searchKey));
				Object result = retrieveResult(new DecisionResult(), fact, rules);

				if (result != null) {
					return true;
				}

			} else if (SEARCH_TYPE_ARRAY.equals(jsonType)) {

				JSONArray jsonArray = JsonPath.read(jsonObj, jsonPath);

				for (Object arrEl : jsonArray) {
					DecisionData fact = null;
					if (arrEl instanceof JSONObject) {
						Object data = ((JSONObject) arrEl).get(searchKey);
						if (data != null) {
							fact = new DecisionData(data);
							Object result = retrieveResult(new DecisionResult(), fact, rules);
							if (result != null) {
								return true;
							}
						}
					} else {
						fact = new DecisionData(arrEl);
						Object result = retrieveResult(new DecisionResult(), fact, rules);
						if (result != null) {
							return true;
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new DecisionManagerException("<!!!!!!>", ex);
		}
		return false;
	}
	
	@Deprecated
	public String route(Object obj, String jsonPath, String jsonType, String searchKey, Object searchValue,
			List<String> rules) throws DecisionManagerException {

		try {
			logger.debug("<@@@@ - INPUT OBJ  -> " + obj + " - @@@>");

			Gson gson = new Gson();
			String jsonString = gson.toJson(obj);

			logger.debug("@@@@ - INPUT OBJ to json -> " + jsonString + " - @@@>");

			JSONObject jsonObj = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(jsonString);

			if (SEARCH_TYPE_OBJECT.equals(jsonType)) {
				JSONObject jsonObjValue = JsonPath.read(jsonObj, jsonPath);
				DecisionData fact = new DecisionData(searchValue);
				Object result = retrieveResult(new DecisionResult(), fact, rules);
				logger.debug("<@@@@ - RESULT VALUE -> " + result + " - @@@>");
				return result.toString();
			} else if (SEARCH_TYPE_ARRAY.equals(jsonType)) {
				JSONArray jsonArray = JsonPath.read(jsonObj, jsonPath);
				DecisionData fact = new DecisionData(searchValue);
				for (Object arrEl : jsonArray) {
					Object result = retrieveResult(new DecisionResult(), fact, rules);
					if (result != null) {
						logger.debug("<@@@@ - RESULT VALUE -> " + result + " - @@@>");
						return result.toString();
					}
				}
			}
		} catch (Exception ex) {
			throw new DecisionManagerException("<!!!!!!>", ex);
		}
		return null;
	}
	
	@Deprecated
	public Object modify(Object obj, String jsonPath, String jsonType, String searchKey, List<String> rules)
			throws DecisionManagerException {
		try {

			logger.debug("<@@@@ - INPUT OBJ  -> " + obj + " - @@@>");

			Gson gson = new Gson();
			String jsonString = gson.toJson(obj);

			logger.debug("@@@@ - INPUT OBJ to json -> " + jsonString + " - @@@>");

			JSONObject jsonObj = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(jsonString);

			if (SEARCH_TYPE_OBJECT.equals(jsonType)) {

				JSONObject jsonObjValue = JsonPath.read(jsonObj, jsonPath);
				DecisionData fact = new DecisionData(jsonObjValue.get(searchKey));
				Object result = retrieveResult(new DecisionResult(), fact, rules);

				if (result != null) {
					jsonObjValue.replace(searchKey, result);
				}

			} else if (SEARCH_TYPE_ARRAY.equals(jsonType)) {

				JSONArray jsonArray = JsonPath.read(jsonObj, jsonPath);

				Object toRemove = null;
				Object toAdd = null;
				;

				for (Object arrEl : jsonArray) {
					DecisionData fact = null;
					if (arrEl instanceof JSONObject) {
						Object data = ((JSONObject) arrEl).get(searchKey);
						if (data != null) {
							fact = new DecisionData(data);
							Object result = retrieveResult(new DecisionResult(), fact, rules);
							if (result != null) {
								((JSONObject) arrEl).replace(searchKey, result);
							}
						}
					} else {
						fact = new DecisionData(arrEl);
						Object result = retrieveResult(new DecisionResult(), fact, rules);
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
			}

			return gson.fromJson(jsonObj.toJSONString(), obj.getClass());

		} catch (Exception ex) {
			throw new DecisionManagerException("<!!!!!!>", ex);
		}
	}
}
