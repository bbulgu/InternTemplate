package com.veniture.rest;


import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.net.RequestFactory;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Path("/main")
public class rest {
    @JiraImport
    private RequestFactory requestFactory;
    //private ApplicationProperties applicationProperties;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private IssueManager issueManager;


    public rest(RequestFactory requestFactory, SearchService searchService, JiraAuthenticationContext authenticationContext){
        this.requestFactory = requestFactory;
        this.issueManager= ComponentAccessor.getIssueManager();
        this.searchService = searchService;
        this.authenticationContext = authenticationContext;
    }

    @GET
    @Path("/child")
    public String child(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws IOException {
        String urlString = "https://api.exchangeratesapi.io/latest?base=USD";
        URL url = new URL(urlString);

        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(request.getInputStream()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        return jsonobj.get("rates").getAsJsonObject().get("TRY").toString();
    }
}
