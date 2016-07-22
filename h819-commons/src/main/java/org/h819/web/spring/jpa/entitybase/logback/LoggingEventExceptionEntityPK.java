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
public class LoggingEventExceptionEntityPK implements Serializable {
    private Long eventId;
    private Short i;

    @Column(name = "event_id", nullable = false, insertable = true, updatable = true)
    @Id
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Column(name = "i", nullable = false, insertable = true, updatable = true)
    @Id
    public Short getI() {
        return i;
    }

    public void setI(Short i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoggingEventExceptionEntityPK that = (LoggingEventExceptionEntityPK) o;

        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (i != null ? !i.equals(that.i) : that.i != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (i != null ? i.hashCode() : 0);
        return result;
    }
}
