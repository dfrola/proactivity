package com.proactivity.decision.manager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proactivity.decision.manager.DecisionManager;
import com.proactivity.decision.manager.DecisionManagerStatefull;
import com.proactivity.decision.manager.exception.DecisionManagerException;
import com.proactivity.decision.manager.exception.DecisionManagerPathNotFoudException;
import com.proactivity.decision.manager.persistence.entity.DecisionCase;
import com.proactivity.decision.manager.persistence.entity.DecisionCaseRule;
import com.proactivity.decision.manager.persistence.repository.DecisionCaseRepository;

@Service
public class DecisionManagerService {

	static final Logger logger = LoggerFactory.getLogger(DecisionManagerService.class);

	private DecisionCaseRepository decisionCaseRepository;
	
	private Map<Long, DecisionManagerStatefull> cache = new HashMap();  

	@Autowired
	public DecisionManagerService(DecisionCaseRepository decisionCaseRepository) {
		super();
		this.decisionCaseRepository = decisionCaseRepository;
	}	
	
	public Object modify(Object objToAnalyze, Object objToModify) {

		if (check(objToAnalyze)) {
			objToModify = modify(objToModify);
		}
		return objToModify;
	}
	
	public Object modify(Object objToAnalyze, Object objToModify, String context) {

		if (check(objToAnalyze, context)) {
			objToModify = modify(objToModify, context);
		}
		return objToModify;
	}
	
	public Object modifyByCache(Object obj, String context) {

		logger.info("Input OBJ class name: " + obj.getClass().getName());
		logger.info("Input OBJ value: " + obj);
		List<DecisionCase> decisionCases = null;

//		if (context != null) {
//			decisionCases = decisionCaseRepository.find(obj.getClass().getName(), "MODIFY", context);
//		} else {
//			decisionCases = decisionCaseRepository.find(obj.getClass().getName(), "MODIFY");
//		}
		
		decisionCases = decisionCaseRepository.findByContextAndCategory(context, "MODIFY");
		DecisionManagerStatefull decisionManager;

		for (DecisionCase decisionCase : decisionCases) {

			List<DecisionCaseRule> decisionCaseRules = decisionCase.getRules();

			for (DecisionCaseRule dcr : decisionCaseRules) {
				try {
					if(cache.containsKey(dcr.getDecisionCaseRuleId())) {
						decisionManager = cache.get(dcr.getDecisionCaseRuleId());
					}else {
						decisionManager = new DecisionManagerStatefull(decisionCase.getJsonPath(), decisionCase.getJsonType(),
								dcr.getSearchKey(), dcr.getRuleFile());
						cache.put(dcr.getDecisionCaseRuleId(), decisionManager);
					}
					obj = decisionManager.modify(obj);
				} catch (DecisionManagerException dme) {					
					if(dme.getCause() instanceof DecisionManagerPathNotFoudException) {						
						continue;
					}	
					throw dme;
				}				
			}
		}
		return obj;
	}

	public Object modify(Object obj, String context) {

		logger.info("Input OBJ class name: " + obj.getClass().getName());
		logger.info("Input OBJ value: " + obj);
		List<DecisionCase> decisionCases = null;

//		if (context != null) {
//			decisionCases = decisionCaseRepository.find(obj.getClass().getName(), "MODIFY", context);
//		} else {
//			decisionCases = decisionCaseRepository.find(obj.getClass().getName(), "MODIFY");
//		}
		
		decisionCases = decisionCaseRepository.findByContextAndCategory(context, "MODIFY");

		DecisionManager decisionManager = new DecisionManager();

		for (DecisionCase decisionCase : decisionCases) {

			List<DecisionCaseRule> decisionCaseRules = decisionCase.getRules();

			for (DecisionCaseRule dcr : decisionCaseRules) {
				try {
					obj = decisionManager.modify(obj, decisionCase.getJsonPath(), decisionCase.getJsonType(),
							dcr.getSearchKey(), dcr.getRuleFile());
				} catch (DecisionManagerException dme) {					
					if(dme.getCause() instanceof DecisionManagerPathNotFoudException) {						
						continue;
					}	
					throw dme;
				}				
			}
		}
		return obj;
	}

	public Object modify(Object obj) {
		return modify(obj, null);
	}

	public Boolean analyze(Object obj, String category) {

		logger.info("Input OBJ class name: " + obj.getClass().getName());

		List<DecisionCase> decisionCases = null;

		if (category != null) {
			decisionCases = decisionCaseRepository.find(obj.getClass().getName(), category);
		} else {
			decisionCases = decisionCaseRepository.findByJavaClassName(obj.getClass().getName());
		}

		DecisionManager decisionManager = new DecisionManager();

		Boolean result = false;

		for (DecisionCase decisionCase : decisionCases) {

			List<DecisionCaseRule> decisionCaseRules = decisionCase.getRules();

			for (DecisionCaseRule dcr : decisionCaseRules) {

				try {
					result = decisionManager.analyze(obj, decisionCase.getJsonPath(), decisionCase.getJsonType(),
							dcr.getSearchKey(), dcr.getRuleFile());
					
				} catch (DecisionManagerException dme) {					
					if(dme.getCause() instanceof DecisionManagerPathNotFoudException) {						
						continue;
					}	
					throw dme;
				}	
				if(result != null) {
					if (!result && dcr.getExitOnFailure()) {
						return false;
					} else if (result && dcr.getExitOnSuccess()) {
						return result;
					}
				}else {
					return false;
				}
			}
		}
		return result;
	}
	
	
		
	public Boolean check(Object obj, String context) {

		logger.info("Input OBJ class name: " + obj.getClass().getName());

		List<DecisionCase> decisionCases = null;

//		if (context != null) {
//			decisionCases = decisionCaseRepository.find(obj.getClass().getName(), "CHECK", context);
//		} else {
//			decisionCases = decisionCaseRepository.findByJavaClassName(obj.getClass().getName());
//		}
		
		decisionCases = decisionCaseRepository.findByContextAndCategory(context, "CHECK");

		DecisionManager decisionManager = new DecisionManager();

		Boolean result = false;

		for (DecisionCase decisionCase : decisionCases) {

			List<DecisionCaseRule> decisionCaseRules = decisionCase.getRules();

			for (DecisionCaseRule dcr : decisionCaseRules) {

				try {
					result = decisionManager.check(obj, dcr.getRuleFile());
					
				} catch (DecisionManagerException dme) {					
					if(dme.getCause() instanceof DecisionManagerPathNotFoudException) {						
						continue;
					}	
					throw dme;
				}	
				if(result != null) {
					if (!result && dcr.getExitOnFailure()) {
						return false;
					} else if (result && dcr.getExitOnSuccess()) {
						return result;
					}
				}else {
					return false;
				}
			}
		}
		return result;
	}
	
	public Boolean check(Object obj) {
		return check(obj, null);
	}

	public Boolean analyze(Object obj) {
		return analyze(obj, null);
	}

	public Object route(Object obj) {
		return route(obj, null);
	}

	public Object route(Object obj, String context) {

		logger.info("Input OBJ class name: " + obj.getClass().getName());

		List<DecisionCase> decisionCases = null;

//		if (context != null) {
//			decisionCases = decisionCaseRepository.find(obj.getClass().getName(), "ROUTE", context);
//		} else {
//			decisionCases = decisionCaseRepository.find(obj.getClass().getName(), "ROUTE");
//		}
		
		decisionCases = decisionCaseRepository.findByContextAndCategory(context, "ROUTE");

		DecisionManager decisionManager = new DecisionManager();

		Object result = null;

		for (DecisionCase decisionCase : decisionCases) {

			List<DecisionCaseRule> decisionCaseRules = decisionCase.getRules();

			for (DecisionCaseRule dcr : decisionCaseRules) {

				try {

					if(dcr.getEnabled()) {
//						result = decisionManager.route(obj, decisionCase.getJsonPath(), decisionCase.getJsonType(),
//								dcr.getSearchKey(), dcr.getRuleFile());
						result = decisionManager.route(obj, dcr.getRuleFile());
					}else {
						logger.warn("### Disabled rule: " + dcr.getRuleFile());
					}

				} catch (DecisionManagerException dme) {					
					if(dme.getCause() instanceof DecisionManagerPathNotFoudException) {						
						continue;
					}
					throw dme;
				}				

				if (result == null && dcr.getExitOnFailure()) {
					return null;
				} else if (result != null && dcr.getExitOnSuccess()) {
					return result;
				}
			}
		}
		return result;
	}
}
