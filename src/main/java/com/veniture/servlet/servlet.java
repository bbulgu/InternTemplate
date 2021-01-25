package com.veniture.servlet;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scanned
public class servlet extends HttpServlet {

    @JiraImport
    public IssueManager issueManager;
    @JiraImport
    private ProjectService projectService;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private TemplateRenderer templateRenderer;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private ConstantsManager constantsManager;
    @JiraImport
    public RequestFactory requestFactory;
    private String action;

    private static final String LIST_ISSUES_TEMPLATE = "/templates/frontend.vm";
    public static final Logger logger = LoggerFactory.getLogger(servlet.class);

    public servlet(IssueManager issueManager, ProjectService projectService,
                   SearchService searchService,
                   TemplateRenderer templateRenderer,
                   JiraAuthenticationContext authenticationContext,
                   ConstantsManager constantsManager,
                   RequestFactory requestFactory) {
        this.issueManager = issueManager;
        this.searchService = searchService;
        this.authenticationContext = authenticationContext;
        this.templateRenderer = templateRenderer;
        this.constantsManager = constantsManager;
        this.requestFactory = requestFactory;
        this.projectService = projectService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> context = new HashMap<>();

        context.put("projects", getProjects());
        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());
    }

    private List<Project> getProjects() {
        ApplicationUser user = authenticationContext.getLoggedInUser();
        return projectService.getAllProjects(user).get();
        /*
        JqlClauseBuilder jqlClauseBuilder = JqlQueryBuilder.newClauseBuilder();
        Query query = jqlClauseBuilder.project("TUTORIAL").buildQuery();
        PagerFilter pagerFilter = PagerFilter.getUnlimitedFilter();

        SearchResults searchResults = null;
        try {
            searchResults = searchService.search(user, query, pagerFilter);
        } catch (SearchException e) {
            e.printStackTrace();
        }
        return searchResults != null ? searchResults.getIssues() : null;
        */
    }
}
