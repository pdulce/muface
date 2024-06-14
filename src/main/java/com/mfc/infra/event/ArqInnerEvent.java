package com.mfc.infra.event;

import com.mfc.infra.utils.ArqConversionUtils;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

@Data
public class ArqInnerEvent<T> implements Serializable {

    private String typeOfEvent;
    private Timestamp occurredOn;
    private String occurredOnFormatted;
    private String classname;
    private T data;

    public ArqInnerEvent() {}
    public ArqInnerEvent(String typeOfEvent, T data) {
        this.typeOfEvent = typeOfEvent;
        this.occurredOn = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.occurredOnFormatted = ArqConversionUtils.formatTimestampToSpanish(this.occurredOn);
        this.data = data;
        this.classname = data.getClass().getName();
    }

    public void setNewData(Object object) {
        this.classname = object.getClass().getName();
        this.data = (T) object;
    }

}
