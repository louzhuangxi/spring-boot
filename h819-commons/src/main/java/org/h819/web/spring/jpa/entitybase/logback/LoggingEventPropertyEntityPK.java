package org.h819.web.spring.jpa.entitybase.logback;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/5/12
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class LoggingEventPropertyEntityPK implements Serializable {
    private Long eventId;
    private String mappedKey;

    @Column(name = "event_id", nullable = false, insertable = true, updatable = true)
    @Id
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Column(name = "mapped_key", nullable = false, insertable = true, updatable = true, length = 254)
    @Id
    public String getMappedKey() {
        return mappedKey;
    }

    public void setMappedKey(String mappedKey) {
        this.mappedKey = mappedKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoggingEventPropertyEntityPK that = (LoggingEventPropertyEntityPK) o;

        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (mappedKey != null ? !mappedKey.equals(that.mappedKey) : that.mappedKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (mappedKey != null ? mappedKey.hashCode() : 0);
        return result;
    }
}
