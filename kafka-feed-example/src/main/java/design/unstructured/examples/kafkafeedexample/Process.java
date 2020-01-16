package design.unstructured.examples.kafkafeedexample;

import design.unstructured.stix.evaluator.mapper.annotations.StixEntity;
import design.unstructured.stix.evaluator.mapper.annotations.StixProperty;

/**
 * Process
 */
@StixEntity
public class Process {
    @StixProperty(name = {"parent_ref",})
    private Process parent;

    @StixProperty
    private ProcessInfo info;

    public Process getParent() {
        return parent;
    }

    public void setParent(Process parent) {
        this.parent = parent;
    }

    public ProcessInfo getInfo() {
        return info;
    }

    public void setInfo(ProcessInfo info) {
        this.info = info;
    }
}
