package com.drizh2.instazoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;
    @JsonIgnore
    private Long profileId;
    @JsonIgnore
    private Long postId;
}
