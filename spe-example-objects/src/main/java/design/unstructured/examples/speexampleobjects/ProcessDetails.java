package design.unstructured.examples.speexampleobjects;
import design.unstructured.stix.evaluator.mapper.annotations.StixObject;
import design.unstructured.stix.evaluator.mapper.annotations.StixProperty;

@StixObject
public class ProcessDetails {

    private String name;

    private String path;

    @StixProperty(name = {"pid", "id"})
    private Integer id;

    private String commandLine;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer pid) {
        this.id = pid;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }
}
