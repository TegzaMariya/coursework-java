package org.example.model;

public class GovService {
    private int id;
    private int institutionId;
    private String name;
    private String description;
    private String documentsRequired;
    private String executionTime;
    private String cost;
    private String notes;

    public GovService() {
    }

    public GovService(int id, int institutionId, String name, String description,
                      String documentsRequired, String executionTime,
                      String cost, String notes) {
        this.id = id;
        this.institutionId = institutionId;
        this.name = name;
        this.description = description;
        this.documentsRequired = documentsRequired;
        this.executionTime = executionTime;
        this.cost = cost;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentsRequired() {
        return documentsRequired;
    }

    public void setDocumentsRequired(String documentsRequired) {
        this.documentsRequired = documentsRequired;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return name;
    }
}