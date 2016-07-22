package org.h819.web.spring.jpa.entitybase.logback;

import javax.persistence.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/5/12
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "logging_event_property")
@IdClass(LoggingEventPropertyEntityPK.class)
public class LoggingEventPropertyEntity {
    private Long eventId;
    private String mappedKey;
    private String mappedValue;

    @Id
    @Column(name = "event_id", nullable = false, insertable = true, updatable = true)
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Id
    @Column(name = "mapped_key", nullable = false, insertable = true, updatable = true, length = 254)
    public String getMappedKey() {
        return mappedKey;
    }

    public void setMappedKey(String mappedKey) {
        this.mappedKey = mappedKey;
    }

    @Lob
    @Basic
    @Column(name = "mapped_value", nullable = true, insertable = true, updatable = true)
    public String getMappedValue() {
        return mappedValue;
    }

    public void setMappedValue(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoggingEventPropertyEntity that = (LoggingEventPropertyEntity) o;

        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (mappedKey != null ? !mappedKey.equals(that.mappedKey) : that.mappedKey != null) return false;
        if (mappedValue != null ? !mappedValue.equals(that.mappedValue) : that.mappedValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (mappedKey != null ? mappedKey.hashCode() : 0);
        result = 31 * result + (mappedValue != null ? mappedValue.hashCode() : 0);
        return result;
    }
}
