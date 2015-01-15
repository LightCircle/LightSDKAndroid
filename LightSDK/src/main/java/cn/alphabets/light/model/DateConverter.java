package cn.alphabets.light.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * GSON serialiser/deserialiser for converting Date
 */
public class DateConverter implements JsonSerializer<Date>, JsonDeserializer<Date>
{

  @Override
  public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context)
  {
    final DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
    return new JsonPrimitive(fmt.print(new DateTime(src)));
  }

  @Override
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException
  {
    final DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
    return fmt.parseDateTime(json.getAsString()).toDate();
  }
}
