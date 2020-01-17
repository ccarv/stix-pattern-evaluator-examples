package design.unstructured.examples.platform.objects;

import design.unstructured.stix.evaluator.mapper.annotations.StixEntity;
import design.unstructured.stix.evaluator.mapper.annotations.StixProperty;

/**
 * Process
 */
@StixEntity
public class ProcessNode {
    @StixProperty(name = {"parent_ref"})
    private ProcessNode parent;

    private List<ProcessNode> children = new ArrayList<>();

    @StixProperty
    private ProcessDetails info;

    private String testValue;

    public ProcessNode getParent() {
        return parent;
    }

    public void setParent(ProcessNode parent) {
        this.parent = parent;
    }

    public ProcessDetails getInfo() {
        return info;
    }

    public void setInfo(ProcessDetails info) {
        this.info = info;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }
}