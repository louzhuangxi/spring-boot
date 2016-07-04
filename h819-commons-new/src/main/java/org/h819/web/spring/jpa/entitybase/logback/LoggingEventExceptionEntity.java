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
@Table(name = "logging_event_exception")
@IdClass(LoggingEventExceptionEntityPK.class)
public class LoggingEventExceptionEntity {
    private Long eventId;
    private Short i;
    private String traceLine;

    @Id
    @Column(name = "event_id", nullable = false, insertable = true, updatable = true)
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Id
    @Column(name = "i", nullable = false, insertable = true, updatable = true)
    public Short getI() {
        return i;
    }

    public void setI(Short i) {
        this.i = i;
    }

    @Basic
    @Column(name = "trace_line", nullable = false, insertable = true, updatable = true, length = 254)
    public String getTraceLine() {
        return traceLine;
    }

    public void setTraceLine(String traceLine) {
        this.traceLine = traceLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoggingEventExceptionEntity that = (LoggingEventExceptionEntity) o;

        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (i != null ? !i.equals(that.i) : that.i != null) return false;
        if (traceLine != null ? !traceLine.equals(that.traceLine) : that.traceLine != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (i != null ? i.hashCode() : 0);
        result = 31 * result + (traceLine != null ? traceLine.hashCode() : 0);
        return result;
    }
}
