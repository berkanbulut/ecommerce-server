package com.example.demo.brand.entity;

import com.example.demo.common.base.BaseEntity;
import jakarta.persistence.*;
@Entity
@Table(
        name = "brands",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
public class Brand extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;
    protected Brand() {
    }

    public Brand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
}
