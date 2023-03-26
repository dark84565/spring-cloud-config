package com.ericchang.repository;

import com.ericchang.listener.JpaListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@EntityListeners(JpaListener.class)
@Entity
@Getter
@Setter
@Table(name = "properties")
public class EnvironmentPropertiesModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String application;
    private String profile;
    private String label;
    @Column(name = "PROP_KEY")
    private String key;
    private String value;
}
