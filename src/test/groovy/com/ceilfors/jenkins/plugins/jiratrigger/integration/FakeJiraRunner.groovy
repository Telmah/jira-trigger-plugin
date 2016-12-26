package com.ceilfors.jenkins.plugins.jiratrigger.integration

import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
/**
 * @author ceilfors
 */
class FakeJiraRunner implements JiraRunner {

    private RESTClient restClient
    private int id = 1;
    private Map<String, FakeIssue> issueMap = [:]

    FakeJiraRunner(JenkinsRunner jenkinsRunner) {
        restClient = createRestClient(jenkinsRunner.webhookUrl)
    }

    @Override
    String createIssue() {
        return createIssue('')
    }

    @Override
    String createIssue(String description) {
        String issueKey = "TEST-$id"
        id++
        issueMap[issueKey] = new FakeIssue(issueKey: issueKey, description: description)
        return issueKey
    }

    @Override
    void updateDescription(String issueKey, String description) {
        issueMap[issueKey].description = description
        Map body = new JsonSlurper().parse(new FileReader(new File(this.class.getResource('updateDescription.json').toURI()))) as Map
        body.issue.key = issueKey
        body.issue.fields.description = issueMap[issueKey].description
        body.changelog.items[0].toString = description
        restClient.post(body: body)
    }

    @Override
    void updateStatus(String issueKey, String status) {
        Map body = new JsonSlurper().parse(new FileReader(new File(this.class.getResource('updateStatus.json').toURI()))) as Map
        body.issue.key = issueKey
        body.issue.fields.description = issueMap[issueKey].description
        body.changelog.items[0].toString = status
        restClient.post(body: body)
    }

    @Override
    void shouldBeNotifiedWithComment(String issueKey, String jobName) {

    }

    @Override
    void updateCustomField(String issueKey, String fieldName, String value) {
        Map body = new JsonSlurper().parse(new FileReader(new File(this.class.getResource('updateCustomField.json').toURI()))) as Map
        body.issue.key = issueKey
        body.issue.fields.description = issueMap[issueKey].description
        body.changelog.items[0].field = fieldName
        body.changelog.items[0].toString = value
        restClient.post(body: body)
    }

    @Override
    void addComment(String issueKey, String comment) {
        Map body = new JsonSlurper().parse(new FileReader(new File(this.class.getResource('addComment.json').toURI()))) as Map
        body.issue.key = issueKey
        body.issue.fields.description = issueMap[issueKey].description
        body.comment.body = comment
        restClient.post(body: body)
    }

    @Override
    boolean validateIssueKey(String issueKey, String jqlFilter) {
        return true
    }

    private RESTClient createRestClient(String jenkinsUrl) {
        return new RESTClient(jenkinsUrl, ContentType.JSON)
    }

    private class FakeIssue {
        String issueKey
        String description
    }
}