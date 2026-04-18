package com.sarobidy.aetheris.core;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.sarobidy.aetheris.annotations.AetherMethod;
import com.sarobidy.aetheris.enums.HttpMethod;
import com.sarobidy.aetheris.utils.AetherPathUtils;

public class AetherRouteMapping extends AetherClassMethod {

    private final String originalUri;  
    private final String uri;          
    private final boolean dynamic;
    private final Pattern regex;       
    private final List<String> variables;
    private final Set<HttpMethod> httpMethods;


    public AetherRouteMapping(Class<?> controllerClass, Method method, String uri) {
        super(controllerClass, method);
        this.originalUri = uri;
        this.uri = AetherPathUtils.normalize(uri);
        this.dynamic = AetherPathUtils.isDynamic(this.uri);

        if (this.dynamic) {
            this.regex = AetherPathUtils.toRegex(this.uri);
            this.variables = Collections.unmodifiableList(AetherPathUtils.extractVariables(this.uri));
        } else {
            this.regex = Pattern.compile("^" + Pattern.quote(this.uri) + "$");
            this.variables = Collections.emptyList();
        }

        AetherMethod httpAnn = method.getAnnotation(AetherMethod.class);
        if (httpAnn != null) {
            this.httpMethods = Set.of(httpAnn.value());
        } else {
            this.httpMethods = Set.of(HttpMethod.GET);
        }
    }

    public Set<HttpMethod> getHttpMethods() {
        return httpMethods;
    }

    public String getUri() { return uri; }
    public String getOriginalUri() { return originalUri; }
    public boolean isDynamic() { return dynamic; }
    public Pattern getRegex() { return regex; }
    public List<String> getVariables() { return variables; }

    @Override
    public String toString() {
        return "AetherRouteMapping{" +
                "originalUri='" + originalUri + '\'' +
                ", normalizedUri='" + uri + '\'' +
                ", controller=" + getControllerClass().getSimpleName() +
                ", method=" + getMethod().getName() +
                ", dynamic=" + dynamic +
                ", vars=" + variables +
                '}';
    }
}
