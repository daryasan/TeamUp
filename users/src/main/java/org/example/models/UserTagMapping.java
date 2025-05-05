package org.example.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users_tags_")
@NoArgsConstructor
@AllArgsConstructor
public class UserTagMapping {

    @EmbeddedId
    private UserTagKey id;

    public UserTagMapping(Long userId, Long tagId) {
        this.id = new UserTagKey();
        this.id.setUserId(userId);
        this.id.setTagId(tagId);
    }
}
