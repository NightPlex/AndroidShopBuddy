package com.school.shopbudd.api.database;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 *
 * Simply abstract to provide same feature to all of the entities
 *
 * @author NightPlex
 */
public class EntityAbstract {
    @DatabaseField(generatedId = true)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

