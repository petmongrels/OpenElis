package us.mn.state.health.lims.common.util;

import org.apache.log4j.Logger;
import org.owasp.encoder.Encode;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class SafeRequest extends HttpServletRequestWrapper {
    private final Logger logger = Logger.getLogger(SafeRequest.class);

    public SafeRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        HttpServletRequest request = (HttpServletRequest) this.getRequest();
        return getEncodedValue(request.getParameter(name));
    }

    private String getEncodedValue(String value) {
        if (value == null) return null;
        return Encode.forHtml(value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        logger.debug("getParameterMap");
        Map<String, String[]> newParameterMap = new HashMap<>();
        Map<String, String[]> existingParameterMap = super.getParameterMap();
        for (String key : existingParameterMap.keySet()) {
            newParameterMap.put(key, getParameterValues(key));
        }
        return newParameterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        logger.debug("getParameterValues");
        String[] existingValues = super.getParameterValues(name);
        String[] newValues = new String[existingValues.length];
        for (int i = 0; i < existingValues.length; i++) {
            newValues[i] = getEncodedValue(existingValues[i]);
        }
        return newValues;
    }
}
