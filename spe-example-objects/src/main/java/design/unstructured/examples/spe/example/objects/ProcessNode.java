package design.unstructured.examples.spe.example.objects;

import design.unstructured.stix.evaluator.mapper.annotations.StixEntity;
import design.unstructured.stix.evaluator.mapper.annotations.StixProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Process
 */
@StixEntity(name = "process")
public class ProcessNode {
    @StixProperty(name = {"parent_ref", "parent_directory_ref"})
    private ProcessNode parent;

    private List<ProcessNode> children = new ArrayList<>();

    private Map<String, Integer> activeEvents = new HashMap<>();

    @StixProperty
    private ProcessDetails info;

    @JsonIgnore
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

    public List<ProcessNode> getChildren() {
        return children;
    }

    public void setChildren(List<ProcessNode> children) {
        this.children = children;

        // Link our child nodes with this
        children.forEach(child -> {
            if (child.getParent() == null) {
                child.setParent(this);
            }
        });
    }

    public Map<String, Integer> getActiveEvents() {
        return activeEvents;
    }

    public void setActiveEvents(Map<String, Integer> activeEvents) {
        this.activeEvents = activeEvents;
    }

    public List<ProcessNode> filter(Predicate<ProcessNode> criteria) {
        List<ProcessNode> matchedNodes = new ArrayList<>();

        if (criteria.test(this)) {
            matchedNodes.add(this);
        }

        if (!this.getChildren().isEmpty()) {
            for (ProcessNode child : this.getChildren()) {
                matchedNodes.addAll(child.filter(criteria));
            }
        }

        return matchedNodes;
    }
}
