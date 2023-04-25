package com.medicapp.medicappprojectcomp.models;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Patient {
    private Date birthday;
    private Date dateDiagnostic;
    private String email;
    private String height;
    private String imageUrl;
    private String lastName;
    private String name;
    private String numberDocument;
    private String typeDocument;
    private String weight;

    public Date getBirthday() {
        return birthday;
    }

    public Date getDateDiagnostic() {
        return dateDiagnostic;
    }

    public String getEmail() {
        return email;
    }

    public String getHeight() {
        return height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getNumberDocument() {
        return numberDocument;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public String getWeight() {
        return weight;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setDateDiagnostic(Date dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberDocument(String numberDocument) {
        this.numberDocument = numberDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
