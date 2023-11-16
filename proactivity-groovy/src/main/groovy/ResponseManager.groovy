import com.proactivity.groovy.dto.request.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

class ResponseManager {

    ISPData modifyISPData(ISPData ispData) {

        List<CatalogEl> catalogs = ispData.getCatalog();
        int namesCounter = 0;
        for(CatalogEl catalog : catalogs){
            if(modifyCataolgEl(catalog)){
                List<String> names = ispData.getNames();
                if(names == null){
                    names = new ArrayList();
                    ispData.setNames(names);
                }
                names.add("Name" + (++namesCounter));
            }
        }
        return ispData;
    }

    Boolean modifyCataolgEl(CatalogEl catalog) {

        if(catalog.getUrl().equals("/core/bonifico-v1")){
            catalog.setUrl("/core/bonifico-v2");
            return true;
        }
        return false;
    }

    Object modify(Object data) {

        if(data instanceof ISPData){
            return modifyISPData(data);
        }else if(data instanceof CatalogEl){
            return modifyCataolgEl(data);
        }
        return data;
    }

     Object modify(HttpServletRequest httpRequest, Object body) {

        if(checkHeaders(httpRequest)){

            if(body instanceof ISPData){
                modifyISPData(body);
            }else if(body instanceof CatalogEl){
                modifyCataolgEl(body);
            }
        }
        return body;
    }

    Boolean checkHeaders(HttpServletRequest httpRequest){

        HttpHeaders httpHeaders = Collections.list(httpRequest.getHeaderNames())
                                    .stream()
                                    .collect(Collectors.toMap(
                    Function.identity(),
                    h -> Collections.list(httpRequest.getHeaders(h)),
                    (oldValue, newValue) -> newValue,
                    HttpHeaders::new
                ));

        List<String> sos = httpHeaders.get("X-SO");
        List<String> appVers = httpHeaders.get("X-APP-VERSION");
        if(sos != null && sos.contains("android")) {
            if(appVers != null && ( ! appVers.isEmpty())) {
                BigDecimal appV = new BigDecimal(appVers.get(0));
                if(appV.compareTo(new BigDecimal("1.0")) > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
