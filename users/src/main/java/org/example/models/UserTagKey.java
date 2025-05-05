package org.example.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UserTagKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tag_id")
    private Long tagId;
}
