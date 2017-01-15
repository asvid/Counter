package asvid.beercounter.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by adam on 15.01.17.
 */

public class CounterItem extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private Double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (this.id == 0)
            this.id = id;
    }
}
